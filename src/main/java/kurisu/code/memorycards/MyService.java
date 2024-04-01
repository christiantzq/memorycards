package kurisu.code.memorycards;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    private final String PATH = "/home/chris/doc/code/design-patterns";
    
    
    private ContentReader contentReader = new MyContentReader();


    public List<MyEntry> retrieveEntries() throws IOException {
        return contentReader.getEntries(PATH);
    }
}
