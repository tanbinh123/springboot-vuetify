package springboot.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.domain.entity.AccessToken;
import springboot.domain.entity.User;
import springboot.repository.AccessTokenRepository;
import springboot.repository.UserRepository;
import springboot.security.JsonWebToken;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

@Service
public class LoginService {

    private UserRepository userRepository;

    private AccessTokenRepository accessTokenRepository;

    @Autowired
    public LoginService(UserRepository userRepository, AccessTokenRepository accessTokenRepository) {
        this.userRepository = userRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    public LoginResult login(String email, String password, String deviceId, String language) {
        User user = userRepository.findUserByEmail(email);
        Locale locale = new Locale.Builder().setLanguage(language).build();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AccessToken accessToken;
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResult(ResourceBundle.getBundle("i18n.messages", locale).getString("user.notFound"), false);
        } else {
            if (deviceId == null || deviceId.equals("") || deviceId.equals("null")) {
                accessToken = createNewAccessToken(user, UUID.randomUUID().toString());
                accessTokenRepository.save(accessToken);
                return new LoginResult(ResourceBundle.getBundle("i18n.messages", locale).getString("login.successful"), true, accessToken);
            } else {
                accessTokenRepository.deleteExpiredTokens(user.getId(), LocalDateTime.now());
                accessToken = accessTokenRepository.findAccessTokenByDeviceId(deviceId);
            }
            if (accessToken == null) {
                accessToken = createNewAccessToken(user, deviceId);
                accessTokenRepository.save(accessToken);
                return new LoginResult(ResourceBundle.getBundle("i18n.messages", locale).getString("login.successful"), true, accessToken);
            } else {
                return new LoginResult(ResourceBundle.getBundle("i18n.messages", locale).getString("login.successful"), true, accessToken);
            }
        }
    }

    private AccessToken createNewAccessToken(User user, String deviceId) {
        String token = JsonWebToken.getInstance().generatePrivateKey(user.getEmail(), user.getPassword(), LocalDateTime.now().toString());
        if (deviceId.length() > 36) {
            //eternal token for mobile devices.
            return new AccessToken(token, deviceId, LocalDateTime.now().plusYears(99), user);
        } else {
            return new AccessToken(token, deviceId, LocalDateTime.now().plusDays(14), user);
        }
    }

    public boolean checkTokenExist(String token) {
        return accessTokenRepository.checkTokenExist(token);
    }
}
