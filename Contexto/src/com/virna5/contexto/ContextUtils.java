/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author opus
 */
public class ContextUtils {
    
    public static ArrayList<String> errors = new ArrayList<>();
    
    public static String CONTEXTDIR = "/Bascon/BSW1/Testbench/";
    
     
    public  static final String OIPATH = "/Bascon/BSW1/Odir/";
    public static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
    
    
    public static long getUID(){
        return (System.currentTimeMillis() << 20) | (System.nanoTime() & ~9223372036854251520L);
    }
    
    public static void clearErrors(){
        errors = new ArrayList<>();
    }
    
    public static boolean hasErrors(){
        return (errors.size() !=0);
    }
    
    
//    public static void updateCSVDescriptorFields(CSVFilterDescriptor cfd, CSVFilterService cf){
//        
//        ArrayList<CSVField> dfields = cfd.getCSVfields();
//        ArrayList<CSVField> ffields = cf.csvfields;
//        
//        for (CSVField f : ffields){
//            dfields.add(f);
//        }
//    }
    
    
    public static String loadFile (String filename) throws IOException{
               
        Path p = Paths.get( filename);

        byte[] bytes = Files.readAllBytes(p);
        String content = new String(bytes, StandardCharsets.UTF_8);
        return content;
   
    }
    
    public static void saveJson(String filename, String payload) throws IOException{
        
        Path p = Paths.get(filename);
        Files.write(p, payload.getBytes(StandardCharsets.UTF_8));
    }
    
    
    public static String readCSV(String filename) throws IOException{
        
        Path p = Paths.get(filename);

        byte[] bytes = Files.readAllBytes(p);
        String content = new String(bytes, StandardCharsets.UTF_8);

        return content;
    }
    
    
    
}
