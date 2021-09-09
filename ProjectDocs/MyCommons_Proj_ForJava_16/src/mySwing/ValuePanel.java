package mySwing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.border.EtchedBorder;

class ValueText extends JPanel
    {

    JLabel label;
    JTextField text;

    public ValueText(String name)
        {
        setLayout(new BorderLayout(5, 0));
        label = new JLabel();
        label.setFont(new Font(getFont().getFamily(), Font.BOLD, getFont().getSize()));
        text = new JTextField();
        add(label, BorderLayout.WEST);
        add(text, BorderLayout.EAST);
        label.setText(" " + name);
        //setBorder(new BevelBorder(0));
        setMinimumSize(new Dimension(200, 35));
        setPreferredSize(new Dimension(200, 35));
        setMaximumSize(new Dimension(200, 35));
        text.setMinimumSize(new Dimension(120, 25));
        text.setPreferredSize(new Dimension(120, 25));
        text.setMaximumSize(new Dimension(120, 25));

        }

    void setValue(double value)
        {
        text.setText("" + value);
        }

    void setValue(int value)
        {
        text.setText("" + value);
        }
    
    void setValue(String value)
        {
        text.setText(value);
        }
    

    double getValue()
        {
        return Double.parseDouble(text.getText().replaceFirst(",", "."));
        }
    
    String getStrValue()
        {
        return text.getText();
        }

    }

public class ValuePanel extends JPanel
    {

    ValueText[] valueTexts;
    JLabel header;
    int c;

    public ValuePanel(int count, String headerText, String[] signalheader, int minimumSize)
        {
        c = count;
        if (!"".equals(headerText))
            {
            header = new JLabel(headerText);
            header.setFont(new Font(getFont().getFamily(), Font.BOLD, getFont().getSize()));
            header.setHorizontalAlignment(CENTER);
            c++;
            }
        if (c < minimumSize)
            {
            c = minimumSize;
            }

        setLayout(new GridLayout(c, 1, 5, 0));
        valueTexts = new ValueText[count];

        if (!"".equals(headerText))
            {
            add(header);
            }
        for (int i = 0; i < count; i++)
            {
            valueTexts[i] = new ValueText(signalheader[i]);
            add(valueTexts[i]);
            }
        setBorder(new EtchedBorder());
        setWidth(200);
        }

    public void setValue(int i, double value)
        {
        valueTexts[i].setValue(value);
        }

    public void setValue(int i, int value)
        {
        valueTexts[i].setValue(value);
        }

    public void setValue(int i, String value)
        {
        valueTexts[i].setValue(value);
        }
    
    public double getValue(int i)
        {
        return valueTexts[i].getValue();
        }
    
    public String getStrValue(int i)
        {
        return valueTexts[i].getStrValue();
        }
    

    public final void setWidth(int width)
        {
        Dimension dimension = new Dimension(width, c * 35);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        }

    public final void setFieldWidth(int width)
        {
        Dimension dimension = new Dimension(width, 35);
        for (ValueText valueText : valueTexts)
            {
            valueText.text.setMinimumSize(dimension);
            valueText.text.setPreferredSize(dimension);
            valueText.text.setMaximumSize(dimension);
            }
        }

    }
