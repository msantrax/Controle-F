/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import org.openide.util.NbPreferences;

/**
 *
 * @author opus
 */
public class ContextUtils {
    
    
    
    public static ArrayList<String> errors = new ArrayList<>();
    
    public static String CONTEXTDIR = "/Bascon/BSW1/Testbench/";
    public static String OIPATH = "/Bascon/BSW1/Odir/";
    
    public static String TASKSDIR = CONTEXTDIR + "Ctx/";
    public static String TEMPLATESDIR = CONTEXTDIR + "Templates/";
    
    public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
    public static final DateFormat datefullFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"); 
    public static final DateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss"); 
    
    public static final String timestamp_format = "%1$td-%1$tm-%1$tY %1$tH:%1$tM:%1$tS:%1$tL";
    
    public static String OWNER = "BSW";
    
    private static final Preferences config = NbPreferences.root();
    private static String netbeans_user;
    private static String file_separator;
    
    private static String user_dir;
    private static String netbeans_dirs;
    
    private static String java_exec;
    private static String javadb_libs;
    private static String javadb_root;
    
    private static boolean release;
    
    
    
    
    public static long getUID(){
        return (System.currentTimeMillis() << 20) | (System.nanoTime() & ~9223372036854251520L);
    }
    
    public static void clearErrors(){
        errors = new ArrayList<>();
    }
    
    public static boolean hasErrors(){
        return (errors.size() !=0);
    }
    

    public static String getTimestamp(){
        
        return String.format(ContextUtils.timestamp_format,Calendar.getInstance());
    }
    
    
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
    
    
    public static void saveFile(String filename, String payload) throws IOException{
        
        Path p = Paths.get(filename);
        Files.write(p, payload.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String readCSV(String filename) throws IOException{
        
        Path p = Paths.get(filename);

        byte[] bytes = Files.readAllBytes(p);
        String content = new String(bytes, StandardCharsets.UTF_8);

        return content;
    }
    
    
    public static String selectFile(boolean save, String dir, String type){
        
        String wd = (dir != null) ? dir : "/Bascon/BSW1/Testbench";
        
        JFileChooser fc = new JFileChooser(wd);

        FileNameExtensionFilter defaultFilter = new FileNameExtensionFilter("Tarefas", type); 
        
        fc.addChoosableFileFilter(defaultFilter);      
        fc.setFileFilter(defaultFilter);

        int rc;
        if (save){
            rc = fc.showDialog(null, "Gravar Arquivo");
        }
        else{
            rc = fc.showDialog(null, "Abrir Arquivo");
        }
        

        if (rc == JFileChooser.APPROVE_OPTION) {
            //lastDir = fc.getSelectedFile().getParent();
            return fc.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    
    
    
    /**
     * Carrega config inicial do aplicativo
     */
    private static boolean loadConfig(){
        
        Properties sprop = System.getProperties();
        
        // Determine o separador 
        file_separator= sprop.getProperty("file.separator");
        // Local onde está o config
        netbeans_user= sprop.getProperty("netbeans.user");
        // Diretorio onde está a aplicação
        user_dir= sprop.getProperty("user.dir");
        // Complemento do diretório da aplicação
        netbeans_dirs = sprop.getProperty("netbeans.dirs");
        
        // Estamos operando no IDE ?
        if (netbeans_dirs.contains("cluster")){
            release = false;
            //notifyLog("Virna2 - 0.6 - Versão de Desenvolvimento");
        }
        else{
            release = true;
            //notifyLog("Virna2 - 0.6  Versão Operacional");
        }
        
        //notifyLog("Diretório do usuário = " + netbeans_user);
        //notifyLog("Diretórios da plataforma = " + netbeans_dirs);        
        //notifyLog("Diretorio raiz do aplicativo = " + user_dir);
            
        if (!isConfigPresent()) initPreferences();
        
        return true;
    }
    
    /**
     * Verifica se o arquivo de config geral (Preferences.properties) está presente
     * @return
     *  True se ele existe em {netbeans_user}/config
     */
    private static boolean isConfigPresent(){
        
        boolean config_ok = false;
        
        if (new File(netbeans_user + "/config/Preferences.properties").exists()){
            //notifyLog("Arquivo de config primário está presente");
            config_ok=true;
        }
        else{
            //notifyLog( "O arquivo de config primário não foi localizado !!");
        }
        
        return config_ok;
    }
    
    
     /**
     * Armazena as preferencias do aplicativo
     */
    private static void initPreferences(){
     
        //notifyLog( "Tentando estabelecer uma configuração primária usável ...");
        
        config.putBoolean("release", release);
        if (release){
            config.put("app.root", user_dir);
            config.put("javadb.libs", user_dir + "/virna2/modules/ext");
            config.put("javadb.data", user_dir + "/virna2/modules/ext/vega.jar"); 
        }
        else{
           config.put("app.root", user_dir);
           config.put("javadb.libs", user_dir + "/DBAccess/release/modules/ext");
           config.put("javadb.data", user_dir + "/DBAccess/release/modules/ext/vega.jar"); 
        }
             
        
        
        try {
            config.flush();
            //notifyLog( "Arquivo de config primário criado em : " + netbeans_user + "/config/Preferences.properties");
        } catch (BackingStoreException ex) {
            //notifyLog("Falha no update das preferencias : " + ex.getMessage());
        }
        
    }
    
//    /**
//     * Identifica caminho para o executavel java
//     * @return 
//     *  String com o path para o executavel no jre
//     */
//    private boolean JavaExec(){
//        
//        java_exec = null;
//        final String abort = " Não foi possivel determinar o caminho para o executável java, abortando ...";
//               
//        File javaExecuble = new File(System.getProperty("java.home"), "/bin/java" + (Utilities.isWindows() ? ".exe" : ""));
//        if (javaExecuble.exists()) {
//            if (javaExecuble.canExecute()) { 
//                java_exec = javaExecuble.getAbsolutePath();
//                log.log(Level.FINE, "Encontrei uma JVM em : {0}" , java_exec);
//                login_dlg.setInfo ( "Encontrei uma JVM em : " + java_exec + "\n");
//            }
//        }
//        
//        if (java_exec == null) {
//            //StatusDisplayer.getDefault().setStatusText(abort);
//            log.log(Level.SEVERE, abort);
//            login_dlg.setInfo ( abort + "\n");
//            return false;
//        }
//        return true;
//        
//    }
    
    
    /**
     * Copia recursivamente os arquivos de um Jar para um subdiretório
     * @param source_folder
     * @param dest_folder
     * @throws IOException 
     */
    private void copyJarFolder(FileObject source_folder, FileObject dest_folder) throws IOException {
        
        FileObject fo;
        
        FileObject childrens[] =  source_folder.getChildren();
         
        for (int i = 0; i < childrens.length; i++) {
            fo = childrens[i];
            if (fo.isFolder()){
                if(fo.getName().equals("META-INF")) continue;
                FileObject new_folder = dest_folder.createFolder(fo.getName());
                copyJarFolder(fo, new_folder);
            }
            else{
                FileUtil.copyFile(fo, dest_folder, fo.getName());
            }          
        }
    }
    
    
    
    
    
    
    
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
    