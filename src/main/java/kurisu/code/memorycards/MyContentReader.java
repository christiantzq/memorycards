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
    private final String CHEAT_WRD_TOKEN_F = "[//]: # (";
    private final String CHEAT_WRD_TOKEN_E = ")";
    private final String TITLE_TOKEN = "# ";
    private final String SPACE = " ";
    private final String LIST_SEPARATOR = ",";

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
            } else if (file.isDirectory()) {
                readPathRecursively(file);
            }

        }
    }

    private void readMarkdown(File file) throws IOException {
        List<String> contentLines = new ArrayList<>();
        List<String> lines = readFileInReverse(file);
        String wordsLine = "";
        for (String line : lines) {
            if (line.trim().startsWith(TITLE_TOKEN)) {                
                MyEntry entry = new MyEntry(line.replace(TITLE_TOKEN, ""), formatContent(contentLines));
                entry.setCheatWords(splitCheatWords(wordsLine));
                entries.add(entry);
                contentLines = new ArrayList<>();
                wordsLine = "";
            } else if(line.trim().startsWith(CHEAT_WRD_TOKEN_F))  {
                wordsLine = line;
            } else {
                contentLines.add(line);
            }
        }
    }
    
    private String[] splitCheatWords(String wordsLine){
        return wordsLine
            // removes front and back token
            .replace(CHEAT_WRD_TOKEN_F, "").replace(CHEAT_WRD_TOKEN_E, "")
            // removes spaces
            .replace(SPACE, "").trim()
            // splits the word list
            .split(LIST_SEPARATOR);
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

    private String formatContent(List<String> contentLines){
        Collections.reverse(contentLines);
        StringBuilder content = new StringBuilder("");
        for (String line : contentLines) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

}
