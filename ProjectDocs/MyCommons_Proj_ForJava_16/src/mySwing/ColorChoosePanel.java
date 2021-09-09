/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

/**
 *
 * @author JURI
 */
public class ColorChoosePanel extends JPanel
    {

    MouseListener ml;

    public ColorChoosePanel()
        {
        this.ml = new MouseListener()
            {
            @Override
            public void mouseClicked(MouseEvent me)
                {
                Color color = getBackground();
                color = JColorChooser.showDialog(null, "Select color", color);

                if (color != null)
                    {
                     setBackground(color);
                    }

                }

            @Override
            public void mousePressed(MouseEvent me)
                {

                }

            @Override
            public void mouseReleased(MouseEvent me)
                {

                }

            @Override
            public void mouseEntered(MouseEvent me)
                {

                }

            @Override
            public void mouseExited(MouseEvent me)
                {

                }
            };
        addMouseListener(ml);
        }

    }
