package kurisu.code.memorycards;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    
    private ContentReader contentReader = new MyContentReader();

    public List<MyEntry> retrieveEntries(String path) throws IOException {
        return contentReader.getEntries(path);
    }
}
