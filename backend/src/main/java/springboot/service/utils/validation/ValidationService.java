package springboot.service.utils.validation;

import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
public class ValidationService {

    public boolean validatePassword(String password) {
        if (password == null) {
            return false;
        } else {
            String passwordPattern = ResourceBundle.getBundle("validation.regex").getString("password");
            return password.matches(passwordPattern);
        }
    }

    public boolean validateEmail(String email) {
        if (email == null) {
            return false;
        } else {
            String emailPattern = ResourceBundle.getBundle("validation.regex").getString("email");
            return email.matches(emailPattern);
        }
    }
}

