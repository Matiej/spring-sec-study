package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.database.EmailEntityRepository;
import com.matiej.springsecstudy.email.domain.EmailEntity;
import com.matiej.springsecstudy.email.domain.EmailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendingScheduler {
    private final EmailEntityRepository emailEntityRepository;
    private final SendingService sendingService;
    @Value("${email.resending.interval.minutes}")
    private int resendingInterval;
    @Value("${email.resending.attemptsLimit}")
    private int attemptsLimit;

    @Scheduled(cron = "${email.resending.cron}")
    public void resendingErrorEmails() {
        log.info("Starting scheduled job: " + this.getClass().getSimpleName());
        List<EmailEntity> emails = getEmailsFromDataBase(EmailStatus.ERROR);
        log.info("Found: " + emails.size() + " emails to resend.");
        emails.stream()
                .map(ResendingRequest::new)
                .forEach(sendingService::reSendEmail);
        log.info("Finished scheduled job: " + this.getClass().getSimpleName());
    }

    private List<EmailEntity> getEmailsFromDataBase(EmailStatus status) {
        return emailEntityRepository.findEmailsToResendByStatusAndLimit(status.name(), this.attemptsLimit).stream()
                .filter(p -> p.getLastAttemptDate().isBefore(LocalDateTime.now().minusMinutes(resendingInterval)))
                .collect(Collectors.toList());
    }

}
