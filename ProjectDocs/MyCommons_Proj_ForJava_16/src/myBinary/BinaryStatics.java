/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBinary;

/**
 *
 * @author KOCMOC
 */
public class BinaryStatics {

    public static String decimal_to_binary(int nr) {
        return String.format("%16s", Integer.toBinaryString(nr)).replace(' ', '0');
    }
}
