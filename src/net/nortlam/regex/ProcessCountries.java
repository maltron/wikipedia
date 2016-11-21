package net.nortlam.regex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import net.nortlam.ssl.SSLIgnoreCertificate;

public class ProcessCountries extends BaseRegex {
    
    public static final String FILENAME = "wikipedia_countries.txt";
    public static final String FILENAME_OUTPUT = "wikipedia_detail_countries.txt";

    public static final String TOP = "(?s)^(.*?)<td class=\"maptable\" colspan=\"3\" style=\"text-align:center;padding:0.5em 0;\">";
    public static final String BOTTOM = "(?s)<p><b>Brazil</b>(.*?)$";
    public static final String LIST = "(?s)<tr(.*?)</tr>";
    
    public static void main(String[] args) {
        ProcessCountries app = new ProcessCountries();
    }
    
    public ProcessCountries() {
        try {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME_OUTPUT))) {
                for(Country country: listCountries()) {
                    System.out.printf(">>> Country: %s\n", country.toString());
                    System.out.printf(">>> Country's URL: |%s|\n", country.getUrl());

                    URI uri = UriBuilder.fromUri("https://en.wikipedia.org").path(country.getUrl()).build();
                    System.out.printf(">>> Fetching country:%s\n", uri.toString());

                    Response response = null; int index = 0;
                    try {
                        response = ClientBuilder.newBuilder()
                                .sslContext(new SSLIgnoreCertificate().createContext())
                                .build().target(uri)
                                .request(MediaType.TEXT_HTML)
                                .accept(MediaType.TEXT_HTML).get();
                        System.out.printf(">>> RESPONSE: %d \n", response.getStatus(),
                                response.getStatusInfo().getReasonPhrase());
                        if(response.getStatus() == Response.Status.OK.getStatusCode()) {
                            String content = response.readEntity(String.class);
                            String detailCountry = parseDetailCountry(country, content);
                            // Writing the content into the file
                            writer.append(detailCountry); writer.newLine();
                        }

                    } finally {
                        if(response != null) response.close();
                    }
                }
            }
        } catch(IOException ex) {
            System.err.printf("### IO EXCEPTION:%s\n", ex.getMessage());
        }
    }
    
    
    private List<Country> listCountries() throws IOException {
        List<Country> result = new ArrayList<Country>();
        
        try(BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = null;
            while((line = reader.readLine()) != null) {
//                System.out.printf(">>> %s\n", line);
                
                if(line == null) continue;
                if(line != null && line.trim().isEmpty()) continue;
                
                Country country = new Country();
                for(StringTokenizer token = new StringTokenizer(line, ","); token.hasMoreTokens();) {
                    country.setName(token.nextToken());
                    country.setNameOfficially(token.nextToken());
                    country.setUrl(token.nextToken());
                    country.setFlag(token.nextToken());
                    break;
                }
                result.add(country);
                
//                System.out.printf(">>> %s\n", country.toString());
                
            }
        }
        
        return result;
    }
    
    private String parseDetailCountry(Country country, String content) {
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
        
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", 
                country.getName(), urlOrthographicProjection, capital, 
                officialLanguage, deFactoLanguage, nationalLanguage,
                demonym, area, water, population, currency, currencyCode, dateFormat, drivesOnthe, callingCode,
                iso3166code, internetCode);
    }
}
