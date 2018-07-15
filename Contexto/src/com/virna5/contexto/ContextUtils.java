/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
    
    private static final Logger LOG = Logger.getLogger(ContextUtils.class.getName());
    
    
    
    
    public static String CONTEXTDIR;// = "/Bascon/BSW1/Testbench/";
    public static String TASKSDIR;//= CONTEXTDIR + "Ctx/";
    public static String TEMPLATESDIR;// = CONTEXTDIR + "Templates/";
    public static String CONFIGFILE;//
    public static String SAMPLESDIR;
    public static String AUTOLOAD;
    
    
    public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
    public static final DateFormat datefullFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"); 
    public static final DateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss"); 
    
    public static final String timestamp_format = "%1$td-%1$tm-%1$tY %1$tH:%1$tM:%1$tS:%1$tL";
    
    public static String OWNER = "BSW";
    
    
    
    
    
    private static final Preferences config = NbPreferences.root();
        
    // Config variables  -------------------------------
    public static String netbeans_config;
    public static String file_separator;
    public static String app_dir;
    public static String netbeans_dirs;
    
    private static boolean release;
    private static LoginDLG ldlg;
    
    
    // Methods ----------------------------------------------------------------------------------------
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
        
        String wd = (dir != null) ? dir : CONTEXTDIR;
        
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
        
    
    public static void setLoginDLG (LoginDLG dlg) { ldlg = dlg;}
    
    public static void publishDLGLog (String mes){
        
        if (ldlg != null){
            ldlg.setInfo(mes + "\n\r");
        }
        LOG.info(mes);
    } 
    
    
    public static boolean loadConfig(){
        
        Properties sprop = System.getProperties();
        
        // Determine o separador 
        file_separator= sprop.getProperty("file.separator");
        // Local onde está o config
        netbeans_config= sprop.getProperty("netbeans.user");
        // Diretorio onde está a aplicação
        app_dir= sprop.getProperty("user.dir");
        // Complemento do diretório da aplicação
        netbeans_dirs = sprop.getProperty("netbeans.dirs");
        
        // Estamos operando no IDE ?
        if (netbeans_dirs.contains("cluster")){
            release = false;
            publishDLGLog("Virna5 - 0.7 - Versão de Desenvolvimento");
        }
        else{
            release = true;
            publishDLGLog("Virna5 - 0.7  Versão Operacional");
        }
        
        if (file_separator.equals("/")){
            // Linux
            //CONTEXTDIR = "/Bascon/BSW1/Testbench/";
            CONTEXTDIR = app_dir+"/Data";
            TASKSDIR = CONTEXTDIR + "/Ctx";
            TEMPLATESDIR = CONTEXTDIR + "/Templates";
            SAMPLESDIR = CONTEXTDIR + "/area3";
            AUTOLOAD = TASKSDIR + "/task19.tsk" ;
            CONFIGFILE = netbeans_config + "/config/Preferences.properties";         
        }
        else{
            // Windows
            CONTEXTDIR = app_dir + "\\Data";
            TASKSDIR = CONTEXTDIR + "\\Ctx";
            TEMPLATESDIR = CONTEXTDIR + "\\Templates";
            SAMPLESDIR = CONTEXTDIR + "\\area3";
            AUTOLOAD = TASKSDIR + "\\task19.tsk" ;
            CONFIGFILE = netbeans_config + "\\config\\Preferences.properties";
        }
        
        publishDLGLog ("Diretório de configuração = " + netbeans_config);
        //publishDLGLog ("Diretórios da plataforma = " + netbeans_dirs);        
        publishDLGLog ("Diretorio raiz da Plataforma = " + app_dir);
     
        if (!isConfigPresent()) {
            initPreferences();
        }
        else {
            CONTEXTDIR = config.get("context_dir", CONTEXTDIR);
            TASKSDIR = config.get("tasks_dir", TASKSDIR);
            TEMPLATESDIR = config.get("templates_dir", TEMPLATESDIR);
        }
        
        Path p = Paths.get(CONTEXTDIR);
        if (!Files.exists(p, LinkOption.NOFOLLOW_LINKS)){
            //String furl = app_dir+"/release/modules/ext/Testbench.zip";
            //String fout = CONTEXTDIR + file_separator;
            publishDLGLog ("Não há uma estrutura de dados usável : criando uma default");
            copyZipToFolder(app_dir+"/release/modules/ext/Testbench.zip",
                            CONTEXTDIR + file_separator);
        }
        else {
            publishDLGLog ("Usando estrutura de dados em " + CONTEXTDIR);
        }
        return true;
    }
    
    
    /**
     * Copia recursivamente os arquivos de um Jar para um subdiretório
     * @param source_folder
     * @param dest_folder
     * @throws IOException 
     */
    private static void copyJarFolder(FileObject source_folder, FileObject dest_folder) throws IOException {
        
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
    
    
    
    private static void copyZipToFolder(String source, String destination){
        
        
        ZipEntry entry;
        
        try {
            ZipFile file = new ZipFile(source);
            FileSystem fileSystem = FileSystems.getDefault();
            Enumeration<? extends ZipEntry> entries = file.entries();          
            Files.createDirectory(fileSystem.getPath(destination));
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (entry.isDirectory()) {
                    //System.out.println("Creating Directory:" + destination + entry.getName());
                    Files.createDirectories(fileSystem.getPath(destination + entry.getName()));
                }
                else {
                    InputStream is = file.getInputStream(entry);
                    //System.out.println("File :" + entry.getName());
                    BufferedInputStream bis = new BufferedInputStream(is);
                    
                    String uncompressedFileName = destination + entry.getName();
                    Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                    Files.createFile(uncompressedFilePath);
                    FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);

                    while (bis.available() > 0) {
                        fileOutput.write(bis.read());
                    }
                    fileOutput.close();
                }
            }
            LOG.info("Startup Structure creted @ " + destination);
        } catch (IOException ex) {
            LOG.info("Bypassing Startup Structure creation : " + ex.getMessage());
            //Exceptions.printStackTrace(ex);
        }
        
    }
    
    
    
    
    /**
     * Verifica se o arquivo de config geral (Preferences.properties) está presente
     * @return
     *  True se ele existe em {netbeans_user}/config
     */
    public static boolean isConfigPresent(){
        
        boolean config_ok = false;
        
        if (new File(CONFIGFILE).exists()){
            publishDLGLog ("Arquivo de config primário está presente");
            config_ok=true;
        }
        else{
            publishDLGLog ( "O arquivo de config primário não foi localizado, criando um novo...");
        }
        
        return config_ok;
    }
    
    
     /**
     * Armazena as preferencias do aplicativo
     */
    public static void initPreferences(){
     
        publishDLGLog ( "Tentando estabelecer uma configuração primária usável ...");
        
        config.putBoolean("release", release);
        if (release){
                  
        }
        else{
           
        }
        
        config.put("app.root", app_dir);
        config.put("context_dir", CONTEXTDIR);
        config.put("tasks_dir", TASKSDIR);
        config.put("templates_dir", TEMPLATESDIR);
        config.put("samples_dir", SAMPLESDIR);
        config.put("AUTOLOAD", AUTOLOAD);
        
        
       
        try {
            config.flush();
            publishDLGLog ( "Arquivo de config primário criado em : " + CONFIGFILE);
        } catch (BackingStoreException ex) {
            publishDLGLog ("Falha no update das preferencias : " + ex.getMessage());
        }
    
    }

    
    public static String getResourceFileAsString(String fileName) {
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
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
    

//try {
//                FileObject cdir = FileUtil.createFolder(new File(CONTEXTDIR));
//                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//                URL url = classLoader.getResource(app_dir+"/release/modules/ext/Testbench.jar");
//                String furl = app_dir+"/release/modules/ext/Testbench.jar";
//                File jfile = new File(app_dir+"/release/modules/ext/Testbench.jar");
//                FileObject jarfile =  FileUtil.toFileObject(jfile);
//                copyJarFolder(jarfile, cdir);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
////            } catch (URISyntaxException ex) {
////                Exceptions.printStackTrace(ex);
//            }