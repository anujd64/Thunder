//package com.theflexproject.thunder.fragments;
//
//import android.content.Context;
//
//import com.google.gson.Gson;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.util.List;
//
//public class ExportCSV extends BaseFragment{
//
//    private static List<File> list;
//
//    public void exportJSON(Context context){
//                        Gson gson = new Gson();
//                        String listJson = gson.toJson(list);
//                        // Call function to write file to external directory
//                        writeToStorage(context , "ExportJson", listJson);
//    }
//
//    //Create a Method in your Activity
//    public static void writeToStorage(Context mContext , String fileName , String jsonContent){
//        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//        File file = new File(baseDir,"exportStudentJson");
//        file.mkdir();
//        try{
//            FileWriter writer = new FileWriter(file);
//            writer.append(jsonContent);
//            writer.flush();
//            writer.close();
//
//        }catch (Exception e){
//            e.printStackTrace();
//
//        }
//    }
//
//}
