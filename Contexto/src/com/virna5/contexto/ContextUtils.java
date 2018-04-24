/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;


import com.mxgraph.util.mxResources;
import com.virna5.grapheditor.DefaultFileFilter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author opus
 */
public class ContextUtils {
    
    public static ArrayList<String> errors = new ArrayList<>();
    
    public static String CONTEXTDIR = "/Bascon/BSW1/Testbench/";
    
     
    public  static final String OIPATH = "/Bascon/BSW1/Odir/";
    public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
    public static final DateFormat datefullFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"); 
    
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
    
    
    public static String selectInputFile(String lastDir){
        
        String wd = (lastDir != null) ? lastDir : "/Bascon/BSW1/Testbench";
        JFileChooser fc = new JFileChooser(wd);

        DefaultFileFilter defaultFilter = new DefaultFileFilter(
                "", mxResources.get("allSupportedFormats")) {

            public boolean accept(File file) {
                String lcase = file.getName().toLowerCase();
                return super.accept(file);
            }
        };
        fc.addChoosableFileFilter(defaultFilter);      
        fc.setFileFilter(defaultFilter);

        int rc = fc.showDialog(null, "Abrir Arquivo");

        if (rc == JFileChooser.APPROVE_OPTION) {
            //lastDir = fc.getSelectedFile().getParent();
            return fc.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    
    
    
}
