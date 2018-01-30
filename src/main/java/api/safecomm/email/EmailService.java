package api.safecomm.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
 * public EmailResult sendEmail(@RequestBody EmailImpl emailImpl)
 * Request Type: POST Request Object, required:
 *  {"emailAddress":"address", "emailSubject":"subject", "emailContent":"content"}
 * Description: Sends an email Returns: JSON with email status
 */
@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}