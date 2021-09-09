/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myRegex;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author KOCMOC
 */
public class MyRegex {

// SOME BASIC AND USEFUL EXAMPLES:   
//    
// (\d\d\d\d\d\d\d\d-\d\d\d\d) or (\d{8}-\d{4}) = 19850130-0777
//
// \w{10}\b = any word length 10
//
// (\w{1,20}.\w{1,20}@\w{1,20}.\w{1,3}) = mixcont.support@gmail.com
//
// (\d{4}\s) = 1234(any digits of length = 4 with whitespace after)
//
// (\d{4})$ = (any digits of length = 4, with end of line)
//
// \[\d\/\d\/\d{2}\s\d{2}\:\d{2}\:\d{2}\s\w{2}\]\s{3} = [6/6/19 10:48:38 AM] 
    public static final Pattern EMAIL_PROPER = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern DATE_YYYY_MM_DD = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    public static String IP_PORT = "([1-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9]|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
    public static String IP_ADDRESS = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static String MAC_ADDRESS = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
    public static String OPC_UA_URL__IGT = "(?i)(opc\\.tcp)(:)(\\/)(\\/)((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(?![\\d])(:)(\\d+)";
    public static String OPC_UA_URL__WALTERHAUSEN = "(opc)(\\.)(tcp)(:)(\\/).*?((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(?![\\d])(:)(\\d)(\\d)(\\d)(\\d)(\\/)(\\w{0,200}\\b)";
    public static String DATE_YYYY_MM_dd = "\\d{4}-\\d{2}-\\d{2}";
    public static String TEMPERATURE = "\\d{3}.\\d{1}"; // Checks against format 100.0 (5 chars)
    public static String CHECK_IF_BETWEEN_0_TO_100 = "^[1-9][0-9]?$|^100$";
    //
    public static String INGRED_REGEX_QEW = "\\d{5}"; //5 digits no spaces: 00000
    public static String RECIPE_REGEX_QEW = "(\\d{2})(-)(\\d{1})(-)(\\w{1})(\\d{3})"; // 00-8-N752: two digits - one digit - digit or letter - 3 digits
    //
    //071/891 33 06
    public static String TEL_FAX_NUMBER = "(\\d{3})(/)(\\d{3})( )(\\d{2})( )(\\d{2})";

    /**
     * @deprecated [since 2020-04-02]
     * I think validate(...) with "Pattern" shall be used in future
     * @param toCheck
     * @param regex
     * @return 
     */
    public static boolean check(String toCheck, String regex) {
        if (toCheck.matches(regex)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param pattern
     * @param stringToCheck
     * @return 
     */
    public static boolean validate(Pattern pattern, String stringToCheck) {
        Matcher matcher = pattern.matcher(stringToCheck);
//        return matcher.find(); // OBS! This works not as expected [2020-07-31]
        return matcher.matches();
    }

    public static void main(String[] args) {
//        System.out.println("" + check("B4:99:BA:5A:D8:F4", MAC_ADDRESS));
        //
//        System.out.println("" + validate(EMAIL_PROPER, "aSk@.mixcont.com"));
        //
        System.out.println("" + check("55555", INGRED_REGEX_QEW));
    }
    
    //==========================================================================
    /**
     * [2020-04-15]
     * Can be very useful
     * @param p
     * @param mac
     * @return 
     */
    public static boolean containsMAC(Properties p, String mac) {
        //
        String mac_ = mac.toUpperCase();
        //
        if (p.containsValue(mac_)) {
            return true;
        }
        //
        mac_ = mac.replaceAll("-", ":");
        //
        if (p.containsValue(mac_)) {
            return true;
        }
        //
        mac_ = mac.replaceAll(":", "-");
        //
        if (p.containsValue(mac_)) {
            return true;
        }
        //
        return false;
    }
    //==========================================================================
}
