package kurisu.code.memorycards;

import java.io.IOException;
import java.util.List;

/**
 * Reads a directory and subdirectories and retrieves the contents
 * of every .md file inside
 */
public interface ContentReader {
    
    // Title -> Content
    List<MyEntry> getEntries(String path) throws IOException;

}
