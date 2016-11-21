package net.nortlam.regex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mauricio
 */
public class BaseRegex {


    protected String loadFile(String filename, String extractTop, String extractBottom) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while((line = reader.readLine()) != null) builder.append(line);
        }
        
        return builder.toString().replaceAll(extractTop, "").replaceAll(extractBottom, "");
    }
    
    protected List<String> list(String regex, String content) {
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) result.add(matcher.group());
        
        return result;
    }
    
    protected String seek(List<String> list, String contains, String regex, String[] extract) {
        return seek(list, contains, regex, 0, extract);
    }
    
    protected String seek(List<String> list, String contains, String regex, int index, String[] extract) {
        boolean found = false; String work = null;
        for(String scan: list)
            if(scan.contains(contains)) {
                work = scan; break;
            }
        
        // Didn't find anything
        if(work == null) return null;
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(work); 
        String contentFound = null; int position=0;
        while(matcher.find()) {
            contentFound = matcher.group();
            if(position == index) break;
            
            position++;
        }
        
        // If we were unable to find something right at the first one, then quit
        if(contentFound == null) return null;
        
        // Found it. Now try to remove all possible extra marks
        if(extract != null)
            for(String ex: extract)
                contentFound = contentFound.replaceAll(ex, "");
        
        return contentFound;
    }
}
