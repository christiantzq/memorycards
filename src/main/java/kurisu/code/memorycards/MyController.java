package kurisu.code.memorycards;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MyController {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${DOC_PATH}")
    private String DOC_PATH;

    private MyService service = new MyService();

    @GetMapping("/")
    String home(Model model) {
        return "page1";
    }

    @GetMapping("/entries")
    String entries(Model model) {
        List<MyEntry> entries;

        try {
            entries = service.retrieveEntries(DOC_PATH + "/code/design-patterns");
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        Collections.shuffle(entries);
        model.addAttribute("entries", entries);
        String jsonString = "{}";        
        try {
            jsonString = objectMapper.writeValueAsString(entries);
            // System.out.println("-> " + jsonString);
        } catch (JsonProcessingException e) {
            System.out.println("could not convert Entries toi Json");
        }
        model.addAttribute("jsonString", jsonString);
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
