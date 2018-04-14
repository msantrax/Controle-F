/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.logging.Logger;


public class CSVFilterService {

    private static final Logger log = Logger.getLogger(CSVFilterService.class.getName());

    
    private ArrayList<String> input_fields;
    
    public String rawpayload;
    
    public CSVFilterDescriptor csvd;
    
    public ArrayList<String> lines;
    
    public ArrayList<CSVField> csvfields;
    
    
    public CSVFilterService() {
        lines = new ArrayList<>();
        csvfields = new ArrayList<>();
    }
    
    
    
    public void updateDescriptor(CSVFilterDescriptor csvfd){
        
        ArrayList<CSVField> dfields = csvfd.getCSVfields();
        
        for (CSVField csvf : csvfields){
            dfields.add(csvf);
        }
        
    }
    
    public void buildHeader(){
        
        int count = 0;
        String[] lines = rawpayload.split("\n");
        
        String header = lines[0];
        header = header.replace("\r", "");
        String[] fields = header.split(";");
                
        for (String s : fields){
            CSVField csvf = new CSVField();
            csvf.setCSVfield(s);
            csvf.setId(filteredName(s));
            csvf.setExclude(false);
            if (count < 6){
                csvf.setRealm("header");
                csvf.setType("string");
            }
            else{
                csvf.setRealm("value");
                csvf.setType("number");
            }
            csvfields.add(csvf);
        }       
    }
    
    public String getJson(){
        
        int count = 0;
        StringBuilder sb = new StringBuilder();
        
        String[] lines = rawpayload.split("\n");
        String header = lines[0];
        header = header.replace("\r", "");
        String[] fields = header.split(";");
        
        sb.append("fields: [\n");        
                
        for (String s : fields){
            
            sb.append("\t{\n");
            
            sb.append("\t    \"csvfield\": \"" + s + "\",\n");
            sb.append("\t    \"id\":\"" + filteredName(s) + "\",\n");
            
            if (count < 6){
                sb.append("\t    \"realm\": \"header\",\n");
                sb.append("\t    \"type\": \"string\",\n");
            }
            else{
                sb.append("\t    \"realm\": \"value\",\n");
                sb.append("\t    \"type\": \"number\",\n");
            }
            count++;    
            sb.append("\t},\n");
        }
        sb.append("]\n");    
        
        return sb.toString();
    }
    
    
    
    private String filteredName(String fname){
        
        // Filtre ruídos e normalize o nome    
        fname = fname.toUpperCase().trim();
        fname = fname.replace(" ", "_");
        fname = fname.replace(".", "");
        fname = fname.replace("'", "");
        fname = fname.replace(":", "");
        
        fname = Normalizer.normalize(fname, Normalizer.Form.NFC);
        fname = fname.toUpperCase();
        
        fname = fname.replace("Ã", "A");
        fname = fname.replace("Õ", "O");
        fname = fname.replace("Á", "A");
        fname = fname.replace("Ó", "O");
        fname = fname.replace("Â", "A");
        fname = fname.replace("Õ", "O");
        fname = fname.replace("É", "E");
        fname = fname.replace("Ê", "E");
        fname = fname.replace("Ç", "C");
        
        return fname;
        
    }
    
    
    
}
