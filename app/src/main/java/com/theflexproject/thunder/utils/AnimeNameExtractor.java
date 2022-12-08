package com.theflexproject.thunder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimeNameExtractor {

   private static final Pattern ANIME_NAME_PATTERN = Pattern.compile("\\[(.+)?\\] (.+?) (S\\d* )?- (\\d+) (\\[(.+)\\])?");


    public static String[] getAnimeName (String matchString) {
        String animeName;
        String seasonNumber;
        String episodeNumber;
        Matcher m = ANIME_NAME_PATTERN.matcher(matchString);
        if (m.matches()) {
            animeName= m.group(2);
            seasonNumber= m.group(3);
            episodeNumber= m.group(4);
            System.out.println("In animeExtractor"+animeName +seasonNumber+ episodeNumber);
            return new String[]{animeName , seasonNumber , episodeNumber};
        }
        return null;
    }
}
