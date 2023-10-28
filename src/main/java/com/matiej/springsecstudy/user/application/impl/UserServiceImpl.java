package com.matiej.springsecstudy.user.application.impl;

import com.matiej.springsecstudy.email.application.EmailService;
import com.matiej.springsecstudy.email.application.SendEmailCommand;
import com.matiej.springsecstudy.email.domain.EmailType;
import com.matiej.springsecstudy.report.AsyncBeanRun;
import com.matiej.springsecstudy.security.ActiveUserTrackingService;
import com.matiej.springsecstudy.user.application.AllUsersResponse;
import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.command.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.command.ModifyUserCommand;
import com.matiej.springsecstudy.user.database.RolesRepository;
import com.matiej.springsecstudy.user.database.UserRepository;
import com.matiej.springsecstudy.user.database.VerificationTokenRepository;
import com.matiej.springsecstudy.user.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AsyncBeanRun ascc;
    private final ActiveUserTrackingService activeUserTrackingService;

    @Override
    public AllUsersResponse findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Outside Async: " + auth);
//        System.out.println("Before async outside ->>>>>> thread name: " + Thread.currentThread().getName());
//        ascc.assync();
//        System.out.println("After async outside ->>>>>> thread name: " + Thread.currentThread().getName());
        List<String> activeUsers = activeUserTrackingService.getAllActiveUsers();
        List<UserQueryResponse> userQueryResponses = UserQueryResponse.convertToResponse(userRepository.findAll());

        return new AllUsersResponse(userQueryResponses, activeUsers);
    }

    @Override
    public UserQueryResponse save(CreateUserCommand user) {
        Set<Role> roles = user.getRoles()
                .stream()
                .map(RoleType::valueOf)
                .map(this::findRole)
                .collect(Collectors.toSet());
        UserEntity savedUser = userRepository.save(user.convertToUserEntity(passwordEncoder, true, roles));
        return UserQueryResponse.convertToResponse(savedUser);
    }

    @Override
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
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
    public UserQueryResponse registerNewUser(CreateUserCommand registerUserCommand, HttpServletRequest request) {
        if (isUserExist(registerUserCommand.getEmail())) {
            throw new IllegalArgumentException("There is an account with email: " + registerUserCommand.getEmail());
        }

        UserEntity userEntity = registerUserCommand.convertToUserEntity(passwordEncoder, false, findRole(RoleType.USER));

        String randomToken = UUID.randomUUID().toString();
        UserToken userToken = new UserToken(randomToken, userEntity, LocalDateTime.now().plusYears(2), TokenType.ACTIVATION);
        userEntity.addToken(userToken);
        UserEntity savedUser = userRepository.save(userEntity);

        final String urlToClick = generateVerifyLink(request, userToken.getTokenName());
        emailService.sendEmail(new SendEmailCommand(EmailType.ACTIVATE, savedUser.getUserEmail(), urlToClick, savedUser));

        return UserQueryResponse.convertToResponse(savedUser);
    }

    private Role findRole(RoleType roleType) {
        return rolesRepository.findAll().stream()
                .filter(p -> p.getRoleName().equals(roleType.name()))
                .findAny()
                .orElseGet(() -> new Role(roleType));
    }

    private String generateVerifyLink(HttpServletRequest request, String token) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reg/registerConfirm?token=" + token;
    }

    @Override
    public Optional<UserEntity> findByName(String username) {
        return userRepository.findByName(username);

    }

    @Override
    public Optional<UserQueryResponse> findByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .map(UserQueryResponse::convertToResponse);
    }

    @Override
    public UserQueryResponse update(ModifyUserCommand user) {
        Optional<UserEntity> foundUser = userRepository.findById(user.getId());
        return foundUser.map(userEntity -> {
            userEntity.setName(user.getUsername());
            userEntity.setUserEmail(user.getEmail());
            UserEntity savedUser = userRepository.save(userEntity);
            return UserQueryResponse.convertToResponse(savedUser);
        }).orElseThrow(() -> new UsernameNotFoundException("Can't find user: " + user.getUsername()));
    }

    @Override
    public void confirmRegistration(String token) {
        tokenRepository.findByTokenName(token)
                .ifPresentOrElse(tk -> {
                    UserEntity user = tk.getUser();
                    user.setEnabled(true);
                    userRepository.save(user);
                    log.info("Activation success for user: " + user.getName());
                }, () -> {
                    throw new IllegalArgumentException("Can't find given token, user not activated");
                });
    }

    @Override
    @Transactional
    public void resetPassword(String userEmail, HttpServletRequest request) {
        userRepository.findByUserEmail(userEmail).ifPresentOrElse(user -> {
            String token = UUID.randomUUID().toString();
            UserToken userToken = new UserToken(token, user, LocalDateTime.now().plusMinutes(30), TokenType.PASSWORD_RESET_TOKEN);
            tokenRepository.save(userToken);
            user.addToken(userToken);
            userRepository.save(user);
            String ulrToClick = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +
                    "/reg/changePassword?id=" + user.getId() + "&token=" + token;
            emailService.sendEmail(new SendEmailCommand(EmailType.PASSWORD_RESET, user.getUserEmail(), ulrToClick, user));
        }, () -> {
            throw new IllegalArgumentException("User: " + emailService + " not found");
        });
    }

    @Override
    public Optional<UserToken> getPasswordResetToken(String token) {
        return tokenRepository.findByTokenName(token);
    }

    @Override
    public void changeUserPassword(UserEntity user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setMatchingPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void logout(String name, HttpSession session) {
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }

        activeUserTrackingService.cleanSessionRegistry(name);

    }

    private boolean isUserExist(String email) {
        return userRepository.findByUserEmail(email).isPresent();
    }
}
