package springboot.service.registration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.domain.entity.User;
import springboot.repository.UserRepository;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class RegistrationConfirmService {

    private UserRepository userRepository;

    @Autowired
    public RegistrationConfirmService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger LOGGER = LogManager.getLogger(RegistrationConfirmService.class);

    public RegistrationResult registrationConfirm(String code, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        try {
            String[] codes = code.split(":");
            String encodedEmail = codes[0];
            String encodedPassword = codes[1];
            String encodedLifeTime = codes[2];
            Base64.Decoder decoder = Base64.getDecoder();
            String email = new String(decoder.decode(encodedEmail));
            String password = new String(decoder.decode(encodedPassword));
            long dateInMillis = ByteBuffer.wrap(decoder.decode(encodedLifeTime)).getLong();
            LocalDateTime lifeTime = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (lifeTime.isBefore(LocalDateTime.now())) {
                return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("linkExpired"), false);
            } else {
                return addUser(email, password, locale);
            }
        } catch (RuntimeException e) {
            LOGGER.fatal("Failed to confirm registration.", e);
            return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("somethingWrong"), false);
        }
    }

    private RegistrationResult addUser(String email, String password, Locale locale) {
        int index = email.indexOf('@');
        String username = email.substring(0, index);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encodedPassword);
        if (userRepository.checkExistenceEmail(user.getEmail())) {
            return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("emailExist"), false);
        } else {
            user = userRepository.saveAndFlush(user);
            if (user.getId() != 0) {
                return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("registrationSuccess"), true);
            } else {
                return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("somethingWrong"), false);
            }
        }
    }

}
