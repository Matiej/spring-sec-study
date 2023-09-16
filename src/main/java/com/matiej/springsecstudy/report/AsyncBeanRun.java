package com.matiej.springsecstudy.report;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AsyncBeanRun {

    @Async
    public void assync() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String name = authentication.getName();
            System.out.println("THIISSS IS TEST->>>>>> thread name: " + Thread.currentThread().getName() + " authUser: " + name);
            System.out.println("================Another async annotation is in sendingEmailService -------------");
        } else {
            System.out.println("THIISSS IS TEST->>>>>> thread name: " + Thread.currentThread().getName());

            System.out.println("auto for new thread is null!!!! no sharing of auth between threads!!!");
        }

        //todo doesn't work for me. There should be visible auth from getContext.
    }
}
