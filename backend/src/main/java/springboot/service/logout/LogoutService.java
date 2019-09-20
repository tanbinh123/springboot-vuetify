package springboot.service.logout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.repository.AccessTokenRepository;

@Service
public class LogoutService {

    private AccessTokenRepository accessTokenRepository;

    @Autowired
    public LogoutService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    public void deleteTokenByDeviceId(String deviceId) {
        accessTokenRepository.deleteTokenByDeviceId(deviceId);
    }
}
