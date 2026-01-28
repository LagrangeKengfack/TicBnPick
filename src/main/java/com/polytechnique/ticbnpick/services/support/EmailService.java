package com.polytechnique.ticbnpick.services.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Service for sending emails.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    /**
     * Sends a simple email message.
     *
     * This method sends a synchronous email using the configured JavaMailSender,
     * wrapped in a reactive Mono for non-blocking execution.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the body text of the email
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@ticbnpick.com");
            emailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Sends a simple email message reactively.
     *
     * Wraps the blocking email send operation in a Mono that executes
     * on the bounded elastic scheduler to avoid blocking the event loop.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the body text of the email
     * @return a Mono<Void> signaling completion
     */
    public Mono<Void> sendSimpleMessageReactive(String to, String subject, String text) {
        return Mono.fromRunnable(() -> sendSimpleMessage(to, subject, text))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    /**
     * Sends a registration received notification email.
     *
     * @param to the recipient's email address
     */
    public void sendRegistrationReceived(String to) {
        sendSimpleMessage(
                to,
                "TicBnPick - Inscription reçue",
                "Bonjour,\n\n" +
                "Votre demande d'inscription en tant que livreur a bien été reçue.\n\n" +
                "Votre compte est en attente de validation par notre équipe administrative.\n" +
                "Un email vous sera envoyé lorsque votre demande aura été examinée.\n\n" +
                "Cordialement,\n" +
                "L'équipe TicBnPick"
        );
    }

    /**
     * Sends an account approved notification email.
     *
     * @param to the recipient's email address
     */
    public void sendAccountApproved(String to) {
        sendSimpleMessage(
                to,
                "TicBnPick - Compte approuvé",
                "Bonjour,\n\n" +
                "Félicitations ! Votre compte livreur a été approuvé.\n\n" +
                "Vous pouvez maintenant vous connecter à l'application et commencer à effectuer des livraisons.\n\n" +
                "Cordialement,\n" +
                "L'équipe TicBnPick"
        );
    }

    /**
     * Sends an account rejected notification email.
     *
     * @param to the recipient's email address
     * @param reason optional reason for rejection
     */
    public void sendAccountRejected(String to, String reason) {
        String reasonText = (reason != null && !reason.isEmpty()) 
                ? "\nRaison : " + reason + "\n" 
                : "";
        sendSimpleMessage(
                to,
                "TicBnPick - Demande d'inscription refusée",
                "Bonjour,\n\n" +
                "Nous avons le regret de vous informer que votre demande d'inscription " +
                "en tant que livreur n'a pas été approuvée.\n" +
                reasonText +
                "\nSi vous pensez qu'il s'agit d'une erreur, veuillez nous contacter.\n\n" +
                "Cordialement,\n" +
                "L'équipe TicBnPick"
        );
    }
}
