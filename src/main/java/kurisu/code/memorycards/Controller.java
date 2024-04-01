package kurisu.code.memorycards;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/")
    String home(Model model){
        return "page1";
    }

    @GetMapping("/test")
    String test(Model model, @RequestParam String param){

        if(param == null || param.equals("")){
            return "error";
        }
        return "page1";
    }
}
