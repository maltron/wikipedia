package net.nortlam.regex;

import java.io.IOException;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public class ProcessLanguages extends BaseRegex {
    
    public static final String FILENAME_INPUT = "list_wikipedias.html";
    public static final String TOP = "(?s)^(.*?)<table class=\"wikitable sortable\" style=\"text-align:right;\">";
    public static final String BOTTOM = "(?s)<h3><span class=\"mw-headline\" id=\"Grand_total\">(.*?)$";
    public static final String LIST = "(?s)<tr(.*?)</tr>";
    
    public static final String[] DEFAULT_ANCHOR = {"<a(.*?)>","</a>"};
    

    public static void main(String[] args) throws IOException {
        ProcessLanguages app = new ProcessLanguages();
    }
    
    public ProcessLanguages() throws IOException {
        String content = loadFile(FILENAME_INPUT, TOP, BOTTOM); int position=0;
//        System.out.printf(">>> CONTENT:%s\n", content);
        for(String tr: list(LIST, content)) {
            
//            System.out.printf(">>> TR:%s\n", tr);
            
            String name = firstMatch(tr, "(?i)<a href=\"[\\w/]+\" title=\"[\\w\\s]+language\">\\w+</a>", DEFAULT_ANCHOR);
            if(name == null) continue;
            String original = indexMatch(tr, "(?i)<a(.*?)/a>", 1,
                    new String[] {"<a(.*?)>","</a>"});
            String code = indexMatch(tr, "(?i)<a(.*?)/a>", 2, 
                    new String[] {"<a(.*?)>","</a>", "</td><td>"});
            
            System.out.printf("{\"name\":\"%s\", \"original\":\"%s\", \"code\":\"%s\"}\n", name, original, code);
            
//            if(position == 50) break; position++;
            
        }
        
        
    }
}
