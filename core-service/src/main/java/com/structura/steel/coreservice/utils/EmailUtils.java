package com.structura.steel.coreservice.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtils {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String username, String tempPassword) {
        String subject = "Welcome to Structura Steel Application!";
        StringBuilder body = new StringBuilder();

        body.append("Dear User,\n\n");
        body.append("Welcome to Structura Steel Application - The modern steel construction management system.\n\n");
        body.append("Your account credentials:\n");
        body.append("▸ Username: ").append(username).append("\n");
        body.append("▸ Password: ").append(tempPassword).append("\n\n");
        body.append("**IMPORTANT:**\n");
        body.append("1. You will be required to change your password on first login\n");
        body.append("2. Your new password must contain:\n");
        body.append("   - Minimum 6 characters\n");
        body.append("   - At least 1 uppercase letter\n");
        body.append("   - At least 1 lowercase letter\n");
        body.append("   - At least 1 digit\n");
        body.append("   - At least 1 special character (!@#$%^&*)\n\n");
        body.append("SECURITY NOTES:\n");
        body.append("✖ Do not share your password with anyone\n");
        body.append("✖ Do not reuse passwords from other services\n");
        body.append("✖ Change your password every 3 months\n\n");
        body.append("If you need assistance, please contact:\n");
        body.append("▸ Hotline: +84 98 5274 643\n");
        body.append("▸ Email: anhnguyen.052003@gmail.com.vn\n\n");
        body.append("Best regards,\n");
        body.append("The Structura Steel Application Team");

        sendEmail(toEmail, subject, body.toString());
    }
}