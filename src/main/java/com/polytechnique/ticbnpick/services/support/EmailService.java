package com.polytechnique.ticbnpick.services.support;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    /**
     * Sends a simple email message.
     *
     * This method sends a synchronous email using the configured JavaMailSender.
     * It is designed for simple text-based notifications.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the body text of the email
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        // TODO:
        // Purpose: Send simple text email
        // Inputs: recipient, subject, text body
        // Outputs: void (fire and forget or Mono<Void> if reactive wrap)
        // Steps:
        //  1. Create SimpleMailMessage
        //  2. Set fields
        //  3. emailSender.send(message)
        // Validations: email format, non-empty subject/body
        // Errors / Exceptions: MailException
        // Reactive Flow: Blocking call, should be wrapped in Mono.fromRunnable().subscribeOn(Schedulers.boundedElastic())
        // Side Effects: Email sent
        // Security Notes: None
    }
}
