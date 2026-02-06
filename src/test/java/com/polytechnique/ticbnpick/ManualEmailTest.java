package com.polytechnique.ticbnpick;

import com.polytechnique.ticbnpick.services.support.EmailService;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = ManualEmailTest.EnvInitializer.class)
public class ManualEmailTest {

    @Autowired
    private EmailService emailService;

    static class EnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .ignoreIfMalformed()
                    .load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        }
    }

    @Test
    void sendTestEmail() {
        String to = "michelekenmeugne@gmail.com";
        String subject = "Test Email from TicBnPick";
        String body = "cavalier";

        System.out.println("Sending email to " + to + "...");
        emailService.sendSimpleMessage(to, subject, body);
        System.out.println("Email send triggered.");
    }
}
