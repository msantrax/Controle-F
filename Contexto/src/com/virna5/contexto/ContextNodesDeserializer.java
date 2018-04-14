/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.openide.util.Lookup;

/**
 *
 * @author opus
 */
public class ContextNodesDeserializer implements JsonDeserializer<ContextNodes>{
    
    public ContextNodes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                                        throws JsonParseException {
      
        ContextNodes cnodes =  new ContextNodes();
        Gson gson = new Gson();
        
        JsonArray jarray = json.getAsJsonArray();
        for (JsonElement elm : jarray){
            JsonObject jobj = (JsonObject) elm;
            JsonElement telm = jobj.get("nodetype");
            String sclazz = "com.virna5."+ telm.getAsString();
            try {
                //Lookup lk = Lookup.getDefault();
                //BaseDescriptor fod = lk.lookup(FileObserverDescriptor.class);
                
                Class clazz = Class.forName(sclazz);
                Object o = clazz.newInstance();
                String stype = o.toString();
                o = gson.fromJson(elm, clazz );
                cnodes.add((BaseDescriptor)o);                
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                throw new JsonParseException(sclazz);
 
            }
            
            
        }
 
        return cnodes;
    
    }
  
}
