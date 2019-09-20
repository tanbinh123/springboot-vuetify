package springboot.controller.passwordupdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.passwordupdate.PasswordUpdateResult;
import springboot.service.passwordupdate.PasswordUpdateService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class PasswordUpdateCtrl {

    private PasswordUpdateService passwordUpdateService;

    @Autowired
    public PasswordUpdateCtrl (PasswordUpdateService passwordUpdateService) {
        this.passwordUpdateService = passwordUpdateService;
    }

    @GetMapping(path = "/sendPasswordUpdateEmail/{email}")
    @ResponseBody
    public PasswordUpdateResult sendPasswordUpdateLink(@PathVariable("email") String email, HttpServletRequest request) {
        return passwordUpdateService.sendPasswordUpdateLink(email, request.getParameter("locale"));
    }

    @PutMapping(path = "/updatePassword/{code}/{password}")
    @ResponseBody
    public PasswordUpdateResult updatePassword(@PathVariable("code") String code, @PathVariable("password") String password, HttpServletRequest request) {
        return passwordUpdateService.updatePassword(code, password, request.getParameter("locale"));
    }
}
