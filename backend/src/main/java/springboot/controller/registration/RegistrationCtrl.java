package springboot.controller.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.registration.RegistrationResult;
import springboot.service.registration.RegistrationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class RegistrationCtrl {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationCtrl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping(path = "/registration/{email}/{password}")
    @ResponseBody
    public RegistrationResult register(@PathVariable("email") String email, @PathVariable("password") String password, HttpServletRequest request) {
        return registrationService.register(email, password, request.getParameter("locale"));
    }
}
