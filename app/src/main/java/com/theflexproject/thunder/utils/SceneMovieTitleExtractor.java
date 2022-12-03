package com.theflexproject.thunder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SceneMovieTitleExtractor {

    public static SceneMovieTitleExtractor instance() {
        return INSTANCE;
    }

    static final SceneMovieTitleExtractor INSTANCE =
            new SceneMovieTitleExtractor();

    SceneMovieTitleExtractor() {
        // singleton
    }

    public static String[] getTitleYear(String matchString) {
        Matcher m = NAME_YEAR_SCENE_PATTERN.matcher(matchString);
        if (m.matches()) {
            String name = StringUtils.replaceAll(m.group(1), " ", JUNK_PATTERN);
            name = ParseUtils.removeInnerAndOutterSeparatorJunk(name);
            String year = m.group(2);
            String[] nameYear = {name,year};
            return nameYear;
        }
        return null;
    }

    // some junk that sometimes appears before the year
    private static final String JUNK =
            "(?i)(?:(?:DIR(?:ECTORS)?|EXTENDED)[\\s\\p{Punct}]?CUT|UNRATED|THEATRICAL[\\s\\p{Punct}]?EDITION)";
    private static final Pattern JUNK_PATTERN = Pattern.compile(JUNK);

    // NOT ( whitespace | punctuation), matches A-Z, 0-9, localized characters etc
    private static final String CHARACTER = "[^\\s\\p{Punct}]";
    // ( whitespace | punctuation), matches dots, spaces, brackets etc
    private static final String NON_CHARACTER = "[\\s\\p{Punct}]";
    // matches "word"
    private static final String CHARACTER_GROUP = CHARACTER + "+";
    // matches shortest "word.word.word."
    private static final String SEPARATED_CHARTER_GROUPS = "(?:" + CHARACTER_GROUP + NON_CHARACTER + ")+?";
    // matches "19XX" and "20XX" - capture group
    private static final String YEAR_GROUP = "((?:19|20)\\d{2})";
    // matches "word.word.word." - capture group
    private static final String MOVIENAME_GROUP = "(" + SEPARATED_CHARTER_GROUPS + ")";
    // matches ".junk.junk.junk"
    private static final String REMAINING_JUNK = "(?:" + NON_CHARACTER + CHARACTER_GROUP + ")+";
    // matches "Movie.Name.2011.JUNK.JUNK.avi"
    private static final String MOVIENAME_YEAR_JUNK = MOVIENAME_GROUP + YEAR_GROUP + REMAINING_JUNK;

    private static final Pattern NAME_YEAR_SCENE_PATTERN = Pattern.compile(MOVIENAME_YEAR_JUNK);

}