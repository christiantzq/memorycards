package kurisu.code.memorycards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MyContentReader implements ContentReader {
    List<MyEntry> entries = new ArrayList<>();

    @Override
    public List<MyEntry> getEntries(String path) throws IOException {
        File directory = new File(path);
        if (!directory.isDirectory())
            throw new IOException("Path " + path + " is not a directory.");        
        readPathRecursively(directory);
        return entries;
    }

    private void readPathRecursively(File directory) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".md")) {
                try {
                    //System.out.println("Reading file: " + file.getName());
                    readMarkdown(file);
                } catch (Exception e) {
                    throw new IOException("Error reading file: " + file.getName() + ". Exception: " + e.getMessage());
                }
            }
        }
    }

    private void readMarkdown(File file) throws IOException {
        StringBuilder currentContent = new StringBuilder("");
        List<String> lines = readFileInReverse(file);
        for (String line : lines) {
            if (line.trim().startsWith("# ")) {
                MyEntry entry = new MyEntry(line, currentContent.toString());
                //System.out.println("Entry: " + entry.getTitle() + " - " + entry.getContent());
                entries.add(entry);
                currentContent = new StringBuilder("");
            } else {
                currentContent.append(line).append("\n");
            }
        }
    }

    private List<String> readFileInReverse(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        Collections.reverse(lines);
        return lines;
    }

}
