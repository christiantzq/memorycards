package kurisu.code.memorycards;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {

    private MyService service = new MyService();

    @GetMapping("/")
    String home(Model model) {
        return "page1";
    }

    @GetMapping("/entries")
    String entries(Model model) {
        List<MyEntry> entries;

        try {
            entries = service.retrieveEntries();
        } catch (IOException e) {
            return "error";
        }
        Collections.shuffle(entries);
        model.addAttribute("entries", entries);
        return "entries";
    }

    @GetMapping("/test")
    String test(Model model, @RequestParam String param) {

        if (param == null || param.equals("")) {
            return "error";
        }
        return "page1";
    }
}
