/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplementary;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author MCREMOTE
 */
public class ICON {
    
    public static final URL IMAGE_ICON_URL_ = ICON.class.getResource("1.png");
    
    public static Image getPrimIcon() {
        return new ImageIcon(IMAGE_ICON_URL_).getImage();
    }
}
