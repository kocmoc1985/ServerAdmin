/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myBinary;

/**
 *
 * @author Juri1
 */
public class MyStream {

    int pos;
    byte[] buf;
    int l;

    public MyStream(byte[] Buf, int len) {
        buf = Buf;
        pos = 0;
        l = len;
    }

    public String GetString(int l) {
        byte[] temp = new byte[l];
        read(temp, l);
        String s = CutToString(temp, l);
        return s;

    }

    public short GetShort() {
        byte[] temp = new byte[2];
        read(temp, 2);
        return BitConverter.toInt16(temp, 0);
        //return byteArrayToShortLE(temp);
    }

    public int GetInt() {
        byte[] temp = new byte[4];
        read(temp, 4);
        return BitConverter.toInt32(temp, 0);
    }

    public double GetDouble() {
        byte[] temp = new byte[8];
        read(temp, 8);
        return BitConverter.toDouble(temp, 0);
    }

    private void read(byte[] temp, int len) {
        System.arraycopy(buf, pos, temp, 0, len);
        pos += len;

    }

    private String CutToString(byte[] temp, int l) {
        int c = l;
        for (int i = 0; i < l; i++) {
            if (temp[i] == 0) {
                c = i;
                break;
            }
        }
        byte[] str = new byte[c];
        System.arraycopy(temp, 0, str, 0, c);
        return new String(str);
    }

    private short byteArrayToShortLE(final byte[] b) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            value |= (b[1 - i] & 0x000000FF) << (i * 8);
        }

        return value;
    }

    public int GetColor() {
        byte[] temp = new byte[4];
        read(temp, 4);
        byte red=temp[0];
        byte green=temp[1];
        byte blue=temp[2];
        //blue, green, and red C++
        //red green blue    Java
        temp[0]=blue;
        temp[1]=green;
        temp[2]=red;
        return BitConverter.toInt32(temp, 0);
    }
};
