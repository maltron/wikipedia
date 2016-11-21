package net.nortlam.regex;

import java.io.IOException;
import java.util.List;

public class DetailCountry extends BaseRegex {
    
    public static final String FILENAME_INPUT = "venezuela.html";
    public static final String TOP = "(?s)^(.*?)<td class=\"maptable\" colspan=\"3\" style=\"text-align:center;padding:0.5em 0;\">";
    public static final String BOTTOM = "(?s)<p><b>Brazil</b>(.*?)$";
    public static final String LIST = "(?s)<tr(.*?)</tr>";

    public static void main(String[] args) throws IOException {
        DetailCountry app = new DetailCountry();
    }
    
    public DetailCountry() throws IOException {
        System.out.printf(">>> Loading File:%s\n", FILENAME_INPUT);
        String content = loadFile(FILENAME_INPUT, TOP, BOTTOM);
        
        List<String> list = list(LIST, content);
        String urlFlag = seek(list, "Flag", "(?s)src=\"//(.*?)\\.png", new String[] {"src=","\"", "//"});
        System.out.printf(">>> Flag: %s\n", urlFlag);
        
        String urlOrthographicProjection = seek(list, "orthographic", "src=\"(.*?)\"", 
                new String[] {"src=\"//", "\""});
        System.out.printf(">>> Orthographic Projection: %s\n", urlOrthographicProjection);
        
        String capital = seek(list, "Capital", "<a(.*?)>.*?</a>", 
                new String[] {"<a(.*?)>", "</a>"});
        System.out.printf(">>> Capital: %s\n", capital);
        
        String officialLanguage = seek(list, "Official language", "(?i)(?s)<td(.*)>\\w+(.*?)/td>", 
                new String[] {"<a(.*?)>", "</a>", "<sup(.*?)>", ",</sup>", "</a>", "</td>", "<td(.*?)>"});
        System.out.printf(">>> Official Language: %s\n", officialLanguage);
        
        String deFactoLanguage = seek(list, "De facto language", "(?i)(?s)<td(.*)>\\w+(.*?)/td>", 
                new String[] {"<a(.*?)>", "</a>", "<sup(.*?)>", ",</sup>", "</a>", "</td>", "<td(.*?)>"});
        System.out.printf(">>> De facto languages: %s\n", deFactoLanguage);

        String nationalLanguage = seek(list, "National language", "(?i)(?s)<td(.*)>\\w+(.*?)/td>", 
                new String[] {"<a(.*?)>", "</a>", "<sup(.*?)>", ",</sup>", "</a>", "</td>", "<td(.*?)>"});
        System.out.printf(">>> National language: %s\n", nationalLanguage);
        
        String demonym = seek(list, "Demonym", "<a(.*?)>.*?</a>",1, 
                new String[] {"<a(.*?)>", "</a>"});
        System.out.printf(">>> Demonym: %s\n", demonym);
        
        String area = seek(list, "List_of_countries_and_dependencies_by_area", "<td>(.*?)<sup>",
                new String[] {"<td>", "<sup>"});
        System.out.printf(">>> Area: %s\n", area);
        
        String water = seek(list, "Water", "<td>[\\d\\.]*</td>", new String[] {"<td>", "</td>"});
        System.out.printf(">>> Water: %s\n", water);
        
        String population = seek(list, "List of countries by population", "<td>[\\d\\,]*<sup",
                new String[] {"<td>", "<sup"});
        System.out.printf(">>> Population: %s\n", population);
        
        String currency = seek(list, "Currency",  "<a(.*?)>.*?</a>", 
                new String[] {"<a(.*?)>", "</a>"});
        System.out.printf(">>> Currency: %s\n", currency);
        
        String currencyCode = seek(list, "Currency", "<a href=\"/wiki/ISO_4217(.*?)>[A-Z]*</a>",
                new String[] {"<a(.*?)>", "</a>"});
        System.out.printf(">>> Currency Code: %s\n", currencyCode);
        
        String dateFormat = seek(list, "Date format", "(?i)(?s)<td(.*?)>[dmy\\W-]+",
                new String[] {"<td>", "</"});
        System.out.printf(">>> Date format: %s\n", dateFormat);
        
        String drivesOnthe = seek(list, "Drives on the", "<td(.*?)(right|left)(.*?)td>", 
                new String[] {"<td(.*?)>", "<sup(.*?)>", "</td>", "<a(.*?)>", "</a>", "</sup>"});
        System.out.printf(">>> Drives on the: %s\n", drivesOnthe);
        
        String callingCode = seek(list, "Calling code", "[+\\d]+</a>", 
                new String[] {"</a>"});
        System.out.printf(">>> Calling code: %s\n", callingCode);
        
        String iso3166code = seek(list, "ISO 3166 code", "[A-Z]+</a>", 
                new String[] {"</a>"});
        System.out.printf(">>> ISO 3166 code: %s\n", iso3166code);
        
        String internetCode = seek(list, "Internet TLD", "<a(.*?)>.*?</a>",1, 
                new String[] {"<a(.*?)>", "</a>"});
        System.out.printf(">>> Internet Code: %s\n", internetCode);
        
//        for(String tr: list(LIST, content)) {
//            System.out.printf(">>> TR: %s\n\n", tr);
//            
//        }
        
        
    }
}
