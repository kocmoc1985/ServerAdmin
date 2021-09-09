/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myCollections;

import java.util.LinkedHashMap;

/**
 *
 * @author KOCMOC
 */
public class MyLinkedHashMapDefault extends LinkedHashMap<String, String>  {

    public Object get(String o, String defaultValue) {
        //
        Object val = super.get(o);
        //
        if (val != null) {
            return val;
        } else {
            return defaultValue;
        }
        //
    }
    
    public Object getOrDefault(String o, String defaultValue) {
        //
        Object val = super.get(o);
        //
        if (val != null) {
            return val;
        } else {
            return defaultValue;
        }
        //
    }

}
