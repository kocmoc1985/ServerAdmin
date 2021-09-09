/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

/**
 *
 * @author KOCMOC
 */
public class CBoxParam {

    private String param;
    private String param2;
    private boolean isNumber;

    public CBoxParam(String param, String param2, boolean isNumber) {
        this.param = param;
        this.param2 = param2;
        this.isNumber = isNumber;
    }

    public CBoxParam(String param, boolean isNumber) {
        this.param = param;
        this.isNumber = isNumber;
    }

    public String getParam() {
        return param;
    }

    public String getParam2() {
        return param2;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public boolean isMultipleParam() {
        if (param != null && param2 != null) {
            return true;
        } else {
            return false;
        }

    }
}
