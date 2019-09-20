package springboot.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.login.LoginService;
import springboot.service.login.LoginResult;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class LoginCtrl {

    private LoginService loginService;

    @Autowired
    private LoginCtrl(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping(path = "/login/{email}/{password}/{deviceId}")
    @ResponseBody
    public LoginResult login(@PathVariable("email") String email, @PathVariable("password") String password, @PathVariable("deviceId") String deviceId, HttpServletRequest request) {
        return loginService.login(email, password, deviceId, request.getParameter("locale"));
    }

    @GetMapping(path = "/checkTokenExist/{token}")
    @ResponseBody
    public boolean checkTokenExist(@PathVariable("token") String token) {
        return loginService.checkTokenExist(token);
    }
}
