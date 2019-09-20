package springboot.service.passwordupdate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.repository.UserRepository;
import springboot.service.email.EmailService;
import springboot.service.utils.validation.ValidationService;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class PasswordUpdateService {

    private UserRepository userRepository;
    private EmailService emailService;
    private ValidationService validationService;
    private static final Logger LOGGER = LogManager.getLogger(PasswordUpdateService.class);

    @Autowired
    public PasswordUpdateService(UserRepository userRepository, EmailService emailService, ValidationService validationService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.validationService = validationService;
    }

    public PasswordUpdateResult sendPasswordUpdateLink(String email, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        if (!validationService.validateEmail(email)) {
            return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("validationError"), false);
        } else {
            if (userRepository.checkExistenceEmail(email)) {
                if (emailService.sendPasswordUpdateLink(email)) {
                    return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("passwordUpdateLink"), true);
                } else {
                    return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("somethingWrong"), false);
                }
            } else {
                return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("emailNotFound"), false);
            }
        }
    }

    public PasswordUpdateResult updatePassword(String code, String password, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        if (!validationService.validatePassword(password)) {
            return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("validationError"), false);
        } else {
            try {
                String[] codes = code.split(":");
                String encodedEmail = codes[0];
                String encodedLifeTime = codes[1];
                Base64.Decoder decoder = Base64.getDecoder();
                String email = new String(decoder.decode(encodedEmail));
                long dateInMillis = ByteBuffer.wrap(decoder.decode(encodedLifeTime)).getLong();
                LocalDateTime lifeTime = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (lifeTime.isBefore(LocalDateTime.now())) {
                    return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("linkExpired"), false);
                } else {
                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encodedPassword = passwordEncoder.encode(password);
                    if (userRepository.updatePassword(email, encodedPassword) == 1) {
                        return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("passwordUpdated"), true);
                    } else {
                        return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("somethingWrong"), false);
                    }
                }
            } catch (RuntimeException e) {
                LOGGER.fatal("Failed to update the password", e);
                return new PasswordUpdateResult(ResourceBundle.getBundle("i18n.messages", locale).getString("somethingWrong"), false);
            }
        }
    }
}
