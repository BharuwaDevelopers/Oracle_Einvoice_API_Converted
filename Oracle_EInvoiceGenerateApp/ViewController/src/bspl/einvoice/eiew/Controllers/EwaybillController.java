package bspl.einvoice.eiew.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EwaybillController {

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}

