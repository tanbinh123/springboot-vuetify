package springboot.controller.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.registration.RegistrationConfirmService;
import springboot.service.registration.RegistrationResult;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class RegistrationConfirmCtrl {

    private RegistrationConfirmService registrationConfirmService;

    @Autowired
    public RegistrationConfirmCtrl(RegistrationConfirmService registrationConfirmService) {
        this.registrationConfirmService = registrationConfirmService;
    }

    @PostMapping(path = "/registrationConfirm/{code}")
    @ResponseBody
    public RegistrationResult registrationConfirm(@PathVariable String code, HttpServletRequest request){
        return registrationConfirmService.registrationConfirm(code, request.getParameter("locale"));
    }
}
