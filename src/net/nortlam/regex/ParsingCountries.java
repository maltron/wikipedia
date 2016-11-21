package net.nortlam.regex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingCountries {

    public static final String FILENAME_INPUT = "list_of_sovereign_states";
    public static final String FILENAME_OUTPUT = "output.txt";
    public static final String TOP = "(?s)^.*<table class=\"sortable wikitable\" style=\"background:white; text-align:left;\">";
    public static final String BOTTOM = "(?s)<tr style=\"background:Lightgrey;\">\\n<td style=\"text-align:center;\"><span style=\"display:none\">ZZZ</span>↑ Other states ↑</td>(.*)$";
    public static final String LIST = "(?s)<tr>(.*?)</tr>";
    
    public static final String NAME = "(?s)<a(.*?)</a>";
    public static final String NAME_OFFICIALLY = "(?s)– (.*?)</td>";
    public static final String URL = "(?s)href=\"(.*?)\"";
    public static final String FLAG = "(?s)src=\"(.*?)\"";
    
    public static void main(String[] args) throws IOException {
        ParsingCountries app = new ParsingCountries();
    }

    public ParsingCountries() throws IOException {

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME_INPUT))) {
            String line = null;
            System.out.printf(">>> Reading File:%s\n", FILENAME_INPUT);
            while ((line = reader.readLine()) != null) builder.append(line);
            System.out.printf(">>> DONE\n");
        }
       
        // Remove TOP AND BOTTOM
        String content = builder.toString().replaceAll(TOP, "").replaceAll(BOTTOM, "");
        
        // List all the TR's only
        Pattern pattern = Pattern.compile(LIST);
        Matcher matcher = pattern.matcher(content);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME_OUTPUT))) {
            while(matcher.find()) {
                String line = matcher.group();

                // Name
                String name = firstMatch(NAME, line, new String[] {"<a(.*?)>","</a>"});

                // Officially Name
                String officiallyName = firstMatch(NAME_OFFICIALLY, line, new String[] {"– ", "</td>", "<sup(.*?)</sup>"});

                // URL
                String url = firstMatch(URL, line, new String[] {"href=\"", "\""});

                // Flag
                String flag = firstMatch(FLAG, line, new String[] {"src=\"", "\""});

                System.out.printf(">>> Name:%s, Officially:%s, URL:%s, Flag:%s\n", name, officiallyName, url, flag);
                writer.write(String.format("%s, %s, %s, %s", name, officiallyName, url, flag));
                writer.newLine();
            }
        }
    }
    
    private String firstMatch(String regex, String content, String[] extract) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        String result = matcher.find() ? matcher.group() : null;
        if(result != null)
            if(extract != null)
                for(String value: extract)
                    result = result.replaceAll(value, "");
            
        return result;
    }
}
