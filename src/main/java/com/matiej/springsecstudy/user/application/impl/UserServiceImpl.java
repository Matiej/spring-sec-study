package com.matiej.springsecstudy.user.application.impl;

import com.matiej.springsecstudy.email.EmailType;
import com.matiej.springsecstudy.email.application.EmailService;
import com.matiej.springsecstudy.email.application.SendEmailCommand;
import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.command.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.command.ModifyUserCommand;
import com.matiej.springsecstudy.user.controller.command.RegisterUserCommand;
import com.matiej.springsecstudy.user.database.UserRepository;
import com.matiej.springsecstudy.user.database.VerificationTokenRepository;
import com.matiej.springsecstudy.user.domain.UserEntity;
import com.matiej.springsecstudy.user.domain.VerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserQueryResponse save(CreateUserCommand user) {
        UserEntity savedUser = userRepository.save(user.convertToUserEntity(passwordEncoder, true));
        return UserQueryResponse.convertToResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserQueryResponse> findById(Long id) {

        return userRepository.findById(id).map(UserQueryResponse::convertToResponse);
    }

    @Override
    @Transactional
    public UserQueryResponse registerNewUser(RegisterUserCommand user) {
        if (isUserExist(user.getEmail())) {
            throw new IllegalArgumentException("There is an account with email: " + user.getEmail());
        }
        UserEntity savedUser = userRepository.save(user.convertToUserEntity(passwordEncoder, false));

        String randomToken = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(randomToken, savedUser, LocalDateTime.now().plusYears(2));
        VerificationToken savedToken = tokenRepository.save(verificationToken);
        final String urlToClick = generateVerifyLink(user.getRequest(), savedToken.getToken());

        emailService.sendEmail(new SendEmailCommand(EmailType.ACTIVATE, savedUser.getEmail(), urlToClick));

        //todo to delete loging here!!
        log.info("SavedToken id: " + savedToken.getId() + ", token: " + savedToken.getToken() + "verify url: "
                + urlToClick);
        return UserQueryResponse.convertToResponse(savedUser);
    }

    private String generateVerifyLink(HttpServletRequest request, String token) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/registrationConfirm?token=" + token;
    }

    @Override
    public Optional<UserEntity> findByName(String username) {
        return userRepository.findByUsername(username);

    }

    @Override
    public Optional<UserQueryResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserQueryResponse::convertToResponse);
    }

    @Override
    public UserQueryResponse update(ModifyUserCommand user) {
        Optional<UserEntity> foundUser = userRepository.findById(user.getId());
        return foundUser.map(userEntity -> {
            userEntity.setUsername(user.getUsername());
            userEntity.setEmail(user.getEmail());
            UserEntity savedUser = userRepository.save(userEntity);
            return UserQueryResponse.convertToResponse(savedUser);
        }).orElseThrow(()-> new UsernameNotFoundException("Can't find user: " + user.getUsername()));
    }

    private boolean isUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
