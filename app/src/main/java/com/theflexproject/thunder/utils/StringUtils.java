package com.theflexproject.thunder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String replaceAll(String input, String replacement, Pattern pattern) {
        return pattern.matcher(input).replaceAll(replacement);
    }

    public static String replaceAllChars(String input, char[] badChars, char newChar) {
        if (badChars == null || badChars.length == 0)
            return input;
        int inputLength = input.length();
        int replacementLenght = badChars.length;
        boolean modified = false;
        char[] buffer = new char[inputLength];
        input.getChars(0, inputLength, buffer, 0);
        for (int inputIdx = 0; inputIdx < inputLength; inputIdx++) {
            char current = buffer[inputIdx];
            for (int replacementIdx = 0; replacementIdx < replacementLenght; replacementIdx++) {
                if (current == badChars[replacementIdx]) {
                    buffer[inputIdx] = newChar;
                    modified = true;
                    break;
                }
            }
        }
        return modified ? new String(buffer) : input;
    }

    public static String runtimeIntegerToString(int runtime){

        StringBuilder sb = new StringBuilder();
        double f = (double)(runtime)/60;
        String hour = Integer.toString((int)(Math.floor(f)));
        String mins = Integer.toString(runtime%60);
        if(hour.equals("0")){
            sb.append(mins).append("m");
        }else {
            sb.append(hour).append("h ").append(mins).append("m");

        }
        return sb.toString();
    }
    private StringUtils() {
    }

    final public static Pattern TMDB_LINK_PATTERN_MOVIE = Pattern.compile("(.*)(themoviedb\\.org\\/movie\\/(\\d+))");
    public static long tmdbIdExtractor_FromLink(String input) {
        long id = 0;
        System.out.println("input to change tmdb"+input);
        Matcher m = TMDB_LINK_PATTERN_MOVIE.matcher(input);
        if (m.matches()) {
            id = Long.parseLong(m.group(3));
            System.out.println("input to change tmdb"+id);
            return id;
        }
        return id;
    }
    final public static Pattern TMDB_LINK_PATTERN_TV = Pattern.compile("(.*)(themoviedb\\.org\\/tv\\/(\\d+))");

    public static long tmdbIdExtractor_FromLink_TV(String input) {
        long id = 0;
        System.out.println("input to change tmdb"+input);
        Matcher m = TMDB_LINK_PATTERN_TV.matcher(input);
        if (m.matches()) {
            id = Long.parseLong(m.group(3));
            System.out.println("input to change tmdb"+input);
            return id;
        }
        return id;
    }
}

