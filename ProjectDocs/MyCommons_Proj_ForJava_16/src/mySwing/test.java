/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author KOCMOC
 */
public class test {

    public static void main(String[] args) {
//        double d = 1.234567;
//        DecimalFormat df = new DecimalFormat("#.00");
//        System.out.print(df.format(d).replaceAll(",", "."));
        
        System.out.println("" + roundDouble(2.51, "%2.8f"));
        System.out.println("" + roundDouble("1.25", "#.000"));
    }
    
    private static synchronized String roundDouble(double number, String format) {
        return String.format(format, number).replace(",", ".");
    }

    /**
     * 
     * @param obj
     * @param format - "#.00"
     * @return 
     */
    public static Object roundDouble(Object obj,String format){
        if(obj instanceof String){
            if(isDouble(obj)){
                String val = (String) obj;
                double d = Double.parseDouble(val);
                DecimalFormat df = new DecimalFormat(format);
                return df.format(d).replaceAll(",", ".");
            }else{
                return obj;
            }
        }else{
            return obj;
        }
    }
    
    /**
     * 
     * @param format - %2.3f
     * @return 
     */
//    public static String defineFormat(String format){
//        
//    }
    
    
    
    private static synchronized boolean isDouble(Object obj) {
        if (obj instanceof String) {
            String val = (String) obj;
            //
            //
            try {
                Double.parseDouble(val);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }
}
