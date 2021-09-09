/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://txt2re.com/
 * @author KOCMOC
 */
public class Txt2re_Website_Build_Here {

    public static void main(String[] args) {
        String txt = "opc.tcp://10.87.0.3:4980/CAG_CT_CCWH_MixCont";

        String re1 = "(opc)";	// Word 1
        String re2 = "(\\.)";	// Any Single Character 1
        String re3 = "(tcp)";	// Word 2
        String re4 = "(:)";	// Any Single Character 2
        String re5 = "(\\/)";	// Any Single Character 3
        String re6 = ".*?";	// Non-greedy match on filler
        String re7 = "((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(?![\\d])";	// IPv4 IP Address 1
        String re8 = "(:)";	// Any Single Character 4
        String re9 = "(\\d)";	// Any Single Digit 1
        String re10 = "(9)";	// Any Single Digit 2
        String re11 = "(\\d)";	// Any Single Digit 3
        String re12 = "(\\d)";	// Any Single Digit 4
        String re13 = "(\\/)";	// Any Single Character 5
        String re14 = "((?:[a-z][a-z0-9_]*))";	// Variable Name 1

        Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6 + re7 + re8 + re9 + re10 + re11 + re12 + re13 + re14, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        //  
        System.out.println("" + p); // ---------> THE REGEX IS PRINTED HERE
        //
        Matcher m = p.matcher(txt);
        if (m.find()) {
            String word1 = m.group(1);
            String c1 = m.group(2);
            String word2 = m.group(3);
            String c2 = m.group(4);
            String c3 = m.group(5);
            String ipaddress1 = m.group(6);
            String c4 = m.group(7);
            String d1 = m.group(8);
            String d2 = m.group(9);
            String d3 = m.group(10);
            String d4 = m.group(11);
            String c5 = m.group(12);
            String var1 = m.group(13);
            System.out.print("(" + word1.toString() + ")" + "(" + c1.toString() + ")" + "(" + word2.toString() + ")" + "(" + c2.toString() + ")" + "(" + c3.toString() + ")" + "(" + ipaddress1.toString() + ")" + "(" + c4.toString() + ")" + "(" + d1.toString() + ")" + "(" + d2.toString() + ")" + "(" + d3.toString() + ")" + "(" + d4.toString() + ")" + "(" + c5.toString() + ")" + "(" + var1.toString() + ")" + "\n");
        }
    }
}
