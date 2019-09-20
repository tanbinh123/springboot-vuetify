package springboot.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.repository.UserRepository;
import springboot.service.email.EmailService;
import springboot.service.utils.validation.ValidationService;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class RegistrationService {

    private UserRepository userRepository;
    private EmailService emailService;
    private ValidationService validationService;

    @Autowired
    public RegistrationService(UserRepository userRepository, EmailService emailService, ValidationService validationService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.validationService = validationService;
    }

    public RegistrationResult register(String email, String password, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        if (!validationService.validateEmail(email) || !validationService.validatePassword(password)) {
            return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("validationError"), false);
        } else {
            if (userRepository.checkExistenceEmail(email)) {
                return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("emailExist"), false);
            } else {
                if (emailService.sendRegistrationConfirmLink(email, password)) {
                    return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("emailSent"), true);
                } else {
                    return new RegistrationResult(ResourceBundle.getBundle("i18n.messages", locale).getString("somethingWrong"), false);
                }
            }
        }
    }
}
