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
    private final String CHEAT_WRD_START = "[//]: # (";
    private final String CHEAT_WRD_END = ")";
    private final String TITLE_TOKEN = "# ";
    private final String SPACE = " ";
    private final String LIST_SEPARATOR = ",";
    private final int lineLength = 89;

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
                } catch (IOException e) {
                    throw new IOException("Error reading file: [" + file.getName() + "] Cause: "+ e.getCause() + "\n\n Exception: " + e.getMessage());
                }
            } else if (file.isDirectory()) {
                readPathRecursively(file);
            }

        }
    }

    private void readMarkdown(File file) throws IOException {
        List<String> contentLines = new ArrayList<>();
        List<String> lines = readFileInReverse(file);
        String cheatWords = "";
        for (String line : lines) {
            if (line.trim().startsWith(TITLE_TOKEN)) {                
                MyEntry entry = new MyEntry(line.replace(TITLE_TOKEN, ""), formatContent(contentLines));
                entry.setCheatWords(splitCheatWords(cheatWords));
                entries.add(entry);
                contentLines = new ArrayList<>();
                cheatWords = "";
            } else if(line.trim().startsWith(CHEAT_WRD_START))  {
                cheatWords = line;
            } else {
                contentLines.add(line);
            }
        }
    }
    
    private String[] splitCheatWords(String wordsLine){
        return wordsLine
            // removes front and back token
            .replace(CHEAT_WRD_START, "").replace(CHEAT_WRD_END, "")
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
            String formattedLine = formatLine(line);
            content.append(formattedLine).append("\n");
        }
        return content.toString();
    }

    // Shortens the length of the lines to 89 chars long
    private String formatLine(String line) {
        StringBuilder formattedText = new StringBuilder(line);
        int lineStart = 0;
        while ((formattedText.length() - lineStart) > lineLength + 1 ) {
            int eolChar = lineStart + lineLength;

            if(formattedText.charAt(eolChar) == ' '){
                formattedText.setCharAt(eolChar, '\n');
                lineStart = lineStart + lineLength + 1;
            }
            else if (formattedText.charAt(eolChar + 1) == ' '){ // Edge-case 1 char words
                formattedText.setCharAt(eolChar + 1, '\n');
                lineStart = lineStart + lineLength + 2;
            } else {
                for(int i = eolChar - 1; i >= 0; i--){
                    if(formattedText.charAt(i) == ' '){
                        formattedText.setCharAt(i, '\n');
                        lineStart = i + 1;
                        break;
                    }
                }
            }
        }
        return formattedText.toString();
    }
}
