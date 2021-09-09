/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import com.michaelbaranov.microba.calendar.DatePicker;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import static statics.HelpMy.dateToMillisConverter;
import static statics.HelpMy.millisToDateConverter;

/**
 *
 * @author JURI
 */
public class MyDatePicker extends JPanel
    {

    DatePicker button;
    JFormattedTextField text;
    String format;

    public MyDatePicker(String format1) throws ParseException
        {
        format = format1;
        button = new DatePicker();
        text = new JFormattedTextField();
        setMask();
        button.setDateFormat(new SimpleDateFormat(format));
        button.showButtonOnly(true);
        setTextDate();

        setLayout(new BorderLayout());
        add(text, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        button.addActionListener(new ActionListener()
            {
            @Override
            public void actionPerformed(ActionEvent ae)
                {
                setTextDate();
                }

            });

        text.addKeyListener(new KeyListener()
            {
            @Override
            public void keyTyped(KeyEvent ke)
                {
                }

            @Override
            public void keyPressed(KeyEvent ke)
                {
                }

            @Override
            public void keyReleased(KeyEvent ke)
                {
                try
                    {
                    String text1 = text.getText();
                    if (text1.contains(" "))
                        {
                        return;
                        }
                    long l = dateToMillisConverter(text1, format);
                    Date date = new Date(l);

                    button.setDate(date);
                    }
                catch (PropertyVetoException ex)
                    {
                    //Logger.getLogger(MyDatePicker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        }

    private void setTextDate()
        {
        Date date = button.getDate();
        if (date != null)
            {
            long time = date.getTime();
            String date1 = millisToDateConverter("" + time, format);
            text.setText(date1);
            }
        }

    public Date getDate()
        {
        return button.getDate();
        }

    public void setDateFormat(DateFormat dateFormat) throws ParseException
        {
        button.setDateFormat(dateFormat);
        format = ((SimpleDateFormat) dateFormat).toPattern();
        setMask();
        }

    private void setMask() throws ParseException
        {
        String mask = format;
        mask = mask.replaceAll("y", "#");
        mask = mask.replaceAll("M", "#");
        mask = mask.replaceAll("d", "#");

        MaskFormatter maskFormatter = new MaskFormatter(mask);
        maskFormatter.install(text);
        }

    @Override
    public void setFont(Font font)
        {
        if (text != null)
            {
            text.setFont(font);
            }
        }

    @Override
    public void setEnabled(boolean enabled)
        {
        button.setEnabled(enabled);
        text.setEnabled(enabled);
        }

    }
