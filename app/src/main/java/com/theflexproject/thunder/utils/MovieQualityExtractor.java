package com.theflexproject.thunder.utils;

import java.util.regex.Pattern;

public class MovieQualityExtractor {
    public static String extractQualtiy(String name){
        if(Pattern.compile(Pattern.quote("DOVI"), Pattern.CASE_INSENSITIVE).matcher(name).find() && name.contains("2160")){return "4K Dolby Vision";}
        if(Pattern.compile(Pattern.quote("DOVI"), Pattern.CASE_INSENSITIVE).matcher(name).find() && name.contains("1080")){return "1080p Dolby Vision";}
        if(Pattern.compile(Pattern.quote("HDR"), Pattern.CASE_INSENSITIVE).matcher(name).find() && name.contains("2160")) return "4K HDR";
        if(Pattern.compile(Pattern.quote("HDR"), Pattern.CASE_INSENSITIVE).matcher(name).find() && name.contains("1080")) return "1080p HDR";

        if(Pattern.compile(Pattern.quote("Dolby Vision"), Pattern.CASE_INSENSITIVE).matcher(name).find() ||
                Pattern.compile(Pattern.quote("DVSUX"), Pattern.CASE_INSENSITIVE).matcher(name).find()) return "Dolby Vision";

        if(name.contains("2160")) return "2160p";
        if(name.contains("1080")) return "1080p";
        if(name.contains("720")) return "720p";
        if(name.contains("480")) return "480p";
        if(name.contains("XviD")||name.contains("XVID")) return "XVID";
        if(name.contains("ION10")) return "ION10";
        return null;
    }
}
