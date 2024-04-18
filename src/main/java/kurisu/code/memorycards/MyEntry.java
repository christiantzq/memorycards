package kurisu.code.memorycards;

import lombok.Getter;
import lombok.Setter;

public class MyEntry {
    
    public MyEntry(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    @Setter @Getter private String title;

    @Setter @Getter private String content;

    @Setter @Getter private String cheatWords;
    
}
