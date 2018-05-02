/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.io.PrintWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author opus
 */
public class OutHandler extends StreamHandler{

    
    private static OutHandler instance;    
    public static OutHandler getInstance(){
        if (instance == null) {instance = new OutHandler();}
        return instance;
    }
    
    private PrintWriter report_data;
    private PrintWriter report_error;
    private Formatter formatter;
    
    public OutHandler(){
        super();
        InputOutput io = IOProvider.getDefault().getIO("Report", false);
        report_data=io.getOut();
        report_error=io.getErr();
        formatter = this.getFormatter();
        report_error.println("============ OutHandler Static Initialized ==================");
    }
    
    
    public OutHandler(PrintWriter report_error, PrintWriter report_data) {
        super();
        this.report_error = report_error;
        this.report_data = report_data;
        formatter = this.getFormatter();
        report_error.println("============ OutHandler Direct Initialized ==================");
    }
    
    
    
    
    public void publish(LogRecord rec){
        
        String logname = rec.getLoggerName();
        
        if (logname !=null && logname.contains("com.virna5")){
            String mes = formatter.formatMessage(rec);
            if (rec.getLevel() == Level.WARNING ||rec.getLevel() == Level.SEVERE){
                report_error.println(mes);
            }
            else{
                report_data.println(mes);
            }
        }
    }
    
    
    
}
