package springboot.controller.redirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectCtrl {

    @RequestMapping({"/login", "/registration", "/registration-confirm/*"})
    public String redirect() {
        return "forward:/index.html";
    }
}
