package com.theflexproject.thunder.utils;

import android.util.Log;
import android.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtils {

    /* ( whitespace | punctuation)+, matches dots, spaces, brackets etc */
    private static final Pattern MULTI_NON_CHARACTER_PATTERN = Pattern.compile("[\\s\\p{Punct}&&[^']]+");
    /*
     * Matches dots in between Uppercase letters e.g. in "E.T.", "S.H.I.E.L.D." but not a "a.b.c."
     * Last dot is kept "a.F.O.O.is.foo" => "a.FOO.is.foo"
     **/
    private static final Pattern ACRONYM_DOTS = Pattern.compile("(?<=(\\b|[._])\\p{Lu})[.](?=\\p{Lu}([.]|$))");

    /* Matches "1. ", "1) ", "1 - ", "1.-.", "1._"... but not "1.Foo" (could be a starting date with space) or "1-Foo" ..*/
    private static final Pattern LEADING_NUMBERING = Pattern.compile("^(\\d+([.)][\\s\\p{Punct}]+|\\s+\\p{Punct}[\\p{Punct}\\s]*))*");
    // Matches "1-Foo" to be used with movies only because clashes with 24-s01e01 check with find . -type f -regex '.*/[0-9]+-[^/]*'
    private static final Pattern LEADING_NUMBERING_DASH = Pattern.compile("^(\\d+([-]|\\s+\\p{Punct}[\\p{Punct}\\s]*))*");

    /** besides the plain ' there is the typographic ’ and ‘ which is actually not an apostrophe */
    private static final char[] ALTERNATE_APOSTROPHES = new char[] {
            '’', '‘'
    };

    public static final Pattern BRACKETS = Pattern.compile("[<({\\[].+?[>)}\\]]");

    // matches "[space or punctuation/brackets etc]year", year is group 1
    private static final Pattern YEAR_PATTERN = Pattern.compile("(.*)[\\s\\p{Punct}]((?:19|20)\\d{2})(?!\\d)");

    // Strip out everything after empty parenthesis (after year pattern removal)
    // i.e. movieName (1969) garbage -> movieName () garbage -> movieName
    private static final Pattern EMPTY_PARENTHESIS_PATTERN = Pattern.compile("(.*)[\\s\\p{Punct}]+([(][)])");

    private static final Pattern COUNTRY_OF_ORIGIN = Pattern.compile("(.*)[\\s\\p{Punct}]+\\(((US|UK|FR))\\)");

    private static final Pattern PARENTHESIS_YEAR_PATTERN = Pattern.compile("(.*)[\\s\\p{Punct}]+\\(((?:19|20)\\d{2})\\)");

    public static String removeNumbering(String input) {
        return StringUtils.replaceAll(input, "", LEADING_NUMBERING);
    }

    public static String removeNumberingDash(String input) {
        return StringUtils.replaceAll(input, "", LEADING_NUMBERING_DASH);
    }

    /** replaces "S.H.I.E.L.D." with "SHIELD", only uppercase letters */
    public static String replaceAcronyms(String input) {
        return StringUtils.replaceAll(input, "", ACRONYM_DOTS);
    }

    /** replaces alternative apostrophes with a simple ' */
    public static String unifyApostrophes(String input) {
        return StringUtils.replaceAllChars(input, ALTERNATE_APOSTROPHES, '\'');
    }

    /** removes all punctuation characters besides ' Also does apostrophe and Acronym replacement */
    public static String removeInnerAndOutterSeparatorJunk(String input) {
        // replace ’ and ‘ by ' - both could be used as apostrophes
        String result = unifyApostrophes(input);
        result = replaceAcronyms(result);
        return StringUtils.replaceAll(result, " ", MULTI_NON_CHARACTER_PATTERN).trim();
    }

    public static String removeAfterEmptyParenthesis(String input) {
        Log.i("ERROR","removeAfterEmptyParenthesis input: " + input);
        Matcher matcher = EMPTY_PARENTHESIS_PATTERN.matcher(input);
        int start = 0;
        int stop = 0;
        boolean found = false;
        while (matcher.find()) {
            Log.i("ERROR","removeAfterEmptyParenthesis: pattern found");
            found = true;
            start = matcher.start(1);
        }
        // get the first match and extract it from the string
        if (found)
            input = input.substring(0, start);
        Log.i("ERROR","removeAfterEmptyParenthesis remove junk after (): " + input);
        return input;
    }

    private ParseUtils() {
        // static utilities
    }

    // matches "[space or punctuation/brackets etc]year", year is group 1
    // "[\\s\\p{Punct}]((?:19|20)\\d{2})(?!\\d)"
    public static Pair<String, String> yearExtractor(String input) {
        Log.i("ERROR","yearExtractor input: " + input);
        return twoPatternExtractor2(input, YEAR_PATTERN);
    }

    public static Pair<String, String> twoPatternExtractor2(String input, Pattern pattern) {
        Log.i("ERROR","twoPatternExtractor2 input: " + input);
        String isolated = null;
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            input =  matcher.group(1);
            isolated = matcher.group(2);
        }
        Log.i("ERROR","twoPatternExtractor output: " + input + " isolated: " + isolated);
        return new Pair<>(input, isolated);
    }

    public static Pair<String, String> getCountryOfOrigin(String input) {
        String countryOfOrigin = null;

        return twoPatternExtractor2(input, COUNTRY_OF_ORIGIN);
    }

    public static Pair<String, String> parenthesisYearExtractor(String input) {
        return twoPatternExtractor2(input, PARENTHESIS_YEAR_PATTERN);
    }

}
