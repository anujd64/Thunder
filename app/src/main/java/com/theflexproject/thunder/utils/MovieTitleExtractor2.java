package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.utils.ParseUtils.BRACKETS;
import static com.theflexproject.thunder.utils.ParseUtils.removeAfterEmptyParenthesis;
import static com.theflexproject.thunder.utils.ParseUtils.yearExtractor;

import android.util.Pair;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieTitleExtractor2 {

    public static MovieTitleExtractor2 instance() {
        return INSTANCE;
    }

    private static final MovieTitleExtractor2 INSTANCE =
            new MovieTitleExtractor2();

    private MovieTitleExtractor2() {
    }


     public static String[] getTitle2(String input) {
        String name = input;

        // extract the last year from the string
        String year = null;
        // matches "[space or punctuation/brackets etc]year", year is group 1
        // "[\\s\\p{Punct}]((?:19|20)\\d{2})(?!\\d)"
        Pair<String, String> nameYear = yearExtractor(name);
        name = nameYear.first;
        year = nameYear.second;

        // remove junk behind () that was containing year
        // applies to movieName (1928) junk -> movieName () junk -> movieName
        name = removeAfterEmptyParenthesis(name);

        // Strip out starting numbering for collections "1. ", "1) ", "1 - ", "1.-.", "1._"... but not "1.Foo" or "1-Foo"
        name = ParseUtils.removeNumbering(name);
        // Strip out starting numbering for collections "1-"
        name = ParseUtils.removeNumberingDash(name);

        // Strip out everything else in brackets <[{( .. )})>, most of the time teams names, etc
        name = StringUtils.replaceAll(name, "", BRACKETS);

        // strip away known case sensitive garbage
        name = cutOffBeforeFirstMatch(name, GARBAGE_CASESENSITIVE_PATTERNS);

        // replace all remaining whitespace & punctuation with a single space
        name = ParseUtils.removeInnerAndOutterSeparatorJunk(name);

        // append a " " to aid next step
        // > "Foo bar 1080p AC3 " to find e.g. " AC3 "
        name = name + " ";

        // try to remove more garbage, this time " garbage " syntax
        // method will compare with lowercase name automatically
        name = cutOffBeforeFirstMatch(name, GARBAGE_LOWERCASE);

        name = name.trim();
        String[] nameYear2 ={name,year};
        return nameYear2;
    }

    // Common garbage in movies names to determine where the garbage starts in the name
    // tested against strings like "real movie name dvdrip 1080p power "
    private static final String[] GARBAGE_LOWERCASE = {
            " dvdrip ", " dvd rip ", "dvdscreener ", " dvdscr ", " dvd scr ",
            " brrip ", " br rip ", " bdrip", " bd rip ", " blu ray ", " bluray ",
            " hddvd ", " hd dvd ", " hdrip ", " hd rip ", " hdlight ", " minibdrip ",
            " webrip ", " web rip ",
            " 720p ", " 1080p ", " 1080i ", " 720 ", " 1080 ", " 480i ", " 2160p ", " 4k ", " 480p ", " 576p ", " 576i ", " 240p ", " 360p ", " 4320p ", " 8k ",
            " hdtv ", " sdtv ", " m hd ", " ultrahd ", " mhd ",
            " h264 ", " x264 ", " aac ", " ac3 ", " ogm ", " dts ", " hevc ", " x265 ", " av1 ",
            " avi ", " mkv ", " xvid ", " divx ", " wmv ", " mpg ", " mpeg ", " flv ", " f4v ",
            " asf ", " vob ", " mp4 ", " mov ",
            " directors cut ", " dircut ", " readnfo ", " read nfo ", " repack ", " rerip ", " multi ", " remastered ",
            " truefrench ", " srt ", " extended cut ",
            " sbs ", " hsbs ", " side by side ", " sidebyside ", /* Side-By-Side 3d stuff */
            " 3d ", " h sbs ", " h tb " , " tb ", " htb ", " top bot ", " topbot ", " top bottom ", " topbottom ", " tab ", " htab ", /* Top-Bottom 3d stuff */
            " anaglyph ", " anaglyphe ", /* Anaglyph 3d stuff */
            " truehd ", " atmos ", " uhd ", " hdr10+ ", " hdr10 ", " hdr ", " dolby ", " dts-x ", " dts-hd.ma ",
            " hfr ",
    };
    // denoise filter Default = @"(([\(\{\[]|\b)((576|720|1080)[pi]|dir(ectors )?cut|dvd([r59]|rip|scr(eener)?)|(avc)?hd|wmv|ntsc|pal|mpeg|dsr|r[1-5]|bd[59]|dts|ac3|blu(-)?ray|[hp]dtv|stv|hddvd|xvid|divx|x264|dxva|(?-i)FEST[Ii]VAL|L[iI]M[iI]TED|[WF]S|PROPER|REPACK|RER[Ii]P|REAL|RETA[Ii]L|EXTENDED|REMASTERED|UNRATED|CHRONO|THEATR[Ii]CAL|DC|SE|UNCUT|[Ii]NTERNAL|[DS]UBBED)([\]\)\}]|\b)(-[^\s]+$)?)")]
    // stuff that could be present in real names is matched with tight case sensitive syntax
    // strings here will only match if separated by any of " .-_"
    private static final String[] GARBAGE_CASESENSITIVE = {
            "FRENCH", "TRUEFRENCH", "DUAL", "MULTISUBS", "MULTI", "MULTi", "SUBFORCED", "SUBFORCES", "UNRATED", "UNRATED[ ._-]DC", "EXTENDED", "IMAX",
            "COMPLETE", "PROPER", "iNTERNAL", "INTERNAL",
            "SUBBED", "ANiME", "LIMITED", "REMUX", "DCPRip",
            "TS", "TC", "REAL", "HD", "DDR", "WEB",
            "EN", "ENG", "FR", "ES", "IT", "NL", "VFQ", "VF", "VO", "VOF", "VOSTFR", "Eng",
            "VOST", "VFF", "VF2", "VFI", "VFSTFR",
    };

    private static final Pattern[] GARBAGE_CASESENSITIVE_PATTERNS = new Pattern[GARBAGE_CASESENSITIVE.length];
    static {
        for (int i = 0; i < GARBAGE_CASESENSITIVE.length; i++) {
            // case sensitive string wrapped in "space or . or _ or -", in the end either separator or end of line
            // end of line is important since .foo.bar. could be stripped to .foo and that would no longer match .foo.
            GARBAGE_CASESENSITIVE_PATTERNS[i] = Pattern.compile("[ ._-]" + GARBAGE_CASESENSITIVE[i] + "(?:[ ._-]|$)");
        }
    }

    /**
     * assumes title is always first
     * @return substring from start to first finding of any garbage pattern
     */
    private static String cutOffBeforeFirstMatch(String input, Pattern[] patterns) {
        String remaining = input;
        for (Pattern pattern : patterns) {
            if (remaining.isEmpty()) return "";

            Matcher matcher = pattern.matcher(remaining);
            if (matcher.find()) {
                remaining = remaining.substring(0, matcher.start());
            }
        }
        return remaining;
    }

    public static final String cutOffBeforeFirstMatch(String input, String[] garbageStrings) {
        // lower case input to test against lowercase strings
        String inputLowerCased = input.toLowerCase(Locale.US);

        int firstGarbage = input.length();

        for (String garbage : garbageStrings) {
            int garbageIndex = inputLowerCased.indexOf(garbage);
            // if found, shrink to 0..index
            if (garbageIndex > -1 && garbageIndex < firstGarbage)
                firstGarbage = garbageIndex;
        }

        // return substring from input -> keep case
        return input.substring(0, firstGarbage);
    }

}