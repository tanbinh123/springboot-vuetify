package springboot.controller.logout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.logout.LogoutService;

@RestController
@RequestMapping("/api")
public class LogoutCtrl {

    private LogoutService logoutService;

    @Autowired
    public LogoutCtrl (LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PostMapping(path = "/deleteTokenByDeviceId/{deviceId}")
    public void deleteTokenByDeviceId(@PathVariable("deviceId") String deviceId) {
        logoutService.deleteTokenByDeviceId(deviceId);
    }
}
