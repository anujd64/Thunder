package com.theflexproject.thunder.database;

import androidx.room.TypeConverter;

import com.theflexproject.thunder.model.Data;
import com.theflexproject.thunder.model.IndexLink;
import com.theflexproject.thunder.model.ResFormat;
import com.google.gson.Gson;

import java.util.Date;


public class Converters {
    @TypeConverter
    public static String fromResFormat(ResFormat target) {
        return target.toString();
    }
    @TypeConverter
    public static ResFormat fromString(String string) {
        Gson gson = new Gson();
        ResFormat resFormat = gson.fromJson(string, ResFormat.class);
        return resFormat;
    }
    @TypeConverter
    public static String fromData(Data data){
        return data.toString();
    }
    @TypeConverter
    public static Data fromString2(String string){
        Gson gson = new Gson();
        Data data = gson.fromJson(string, Data.class);
        return data;
    }
    @TypeConverter
    public static Long fromModifiedTime(Date date){
        return date.getTime();
    }
    @TypeConverter
    public static Date fromLong(Long date){
        return new Date(date) ;
    }

//    @TypeConverter
//    public static String fromGenreIds(int[] genre_ids){
//        return Arrays.toString(genre_ids);
//    }
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @TypeConverter
//    public static int[] fromString4(String str){
//
//        if(str.equals("null") || str.length()<=1){return new int[1];}
//        int[] arr = Arrays.stream(str.substring(1, str.length()-1).split(","))
//                .map(String::trim).mapToInt(Integer::parseInt).toArray();
//        return arr;
//    }

    @TypeConverter
    public static String fromString3(IndexLink indexLink){
        return indexLink.toString();
    }
    @TypeConverter
    public static IndexLink fromIndexLink(String indexLink){
        Gson gson = new Gson();
        IndexLink ind = gson.fromJson(indexLink, IndexLink.class);
        return ind;
    }

}
