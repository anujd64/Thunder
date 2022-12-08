package com.theflexproject.thunder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlexMovieExtractor {
    private static final String ANYTHING = "(.*)";
    private static final String TMDB_TAG = ANYTHING+"(tmdb-([0-9]+))"+ANYTHING;
    private static final String TMDB_TAG_SIMPLE_PROGRAM = ANYTHING+"(TMDB([0-9]+))"+ANYTHING;
    private static final String VIDEO_DETAILS_TAG = ANYTHING+"(\\[([\\d]{3,4})p, ([a-zA-z\\d]{3,4}), ([a-zA-z\\d]{3,4})])"+ANYTHING;
    private static final Pattern NAME_TMDB_ID_PATTERN = Pattern.compile(TMDB_TAG);
    private static final Pattern NAME_TMDB_ID_PATTERN_SIMPLE_PROGRAM = Pattern.compile(TMDB_TAG_SIMPLE_PROGRAM);
    private static final Pattern NAME_VIDEO_DETAILS_PATTERN = Pattern.compile(VIDEO_DETAILS_TAG);

    public static String getTMDBId (String matchString) {
        String tmdb_id = "";
        System.out.println("Before tmdb_id in plexExtractor matchString"+matchString);
        System.out.println("Before tmdb_id in plexExtractor"+tmdb_id);
        Matcher m = NAME_TMDB_ID_PATTERN.matcher(matchString);
        Matcher m2 = NAME_TMDB_ID_PATTERN_SIMPLE_PROGRAM.matcher(matchString);
        if (m.matches()) {
            System.out.println(m.group(3));
            tmdb_id= m.group(3);
            System.out.println("After tmdb_id in plexExtractor"+tmdb_id);
            return tmdb_id;
        }else if(m2.matches()){
            System.out.println(m2.group(3));
            tmdb_id= m2.group(3);
            System.out.println("After tmdb_id in plexExtractor"+tmdb_id);
            return tmdb_id;
        }
        return null;
    }
    public static String[] getVideoDetails(String matchString){
        Matcher m = NAME_VIDEO_DETAILS_PATTERN.matcher(matchString);
        if (m.matches()) {
            String video_res = m.group(3);
            String video_codec = m.group(4);
            String audio_codec = m.group(5);
            return new String[]{video_res,video_codec,audio_codec};
        }
        return null;
    }

}
