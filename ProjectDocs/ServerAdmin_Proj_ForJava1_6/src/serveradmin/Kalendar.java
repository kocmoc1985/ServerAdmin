/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Bean to display a month calendar in a JPanel. Only works for the Western
 * calendar.
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: Kalendar.java,v 1.5 2004/02/09 03:33:45 ian Exp $
 */
public class Kalendar extends JPanel {
    
    /** The currently-interesting year (not modulo 1900!) */
    protected int yy;
    /** Currently-interesting month and day */
    protected int mm, dd;
    /** The buttons to be displayed */
    protected JButton labs[][];
    /** The number of day squares to leave blank at the start of this month */
    protected int leadGap = 0;
    /** A Calendar object used throughout */
    Calendar calendar = new GregorianCalendar();
    /** Today's year */
    protected final int thisYear = calendar.get(Calendar.YEAR);
    /** Today's month */
    protected final int thisMonth = calendar.get(Calendar.MONTH);
    /** One of the buttons. We just keep its reference for getBackground(). */
    private JButton b0;
    /** The month choice */
    private JComboBox monthChoice;
    /** The year choice */
    private JComboBox yearChoice;
    //==================================
    private String CURRENT_DATE_YYYY_MM_DD = "";

    //==================================
    /**
     * Construct a Kalendar, starting with today.
     */
    public Kalendar() {
        super();
        setYYMMDD(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        buildGUI();
        set_current_date();
        recompute();
    }

    /**
     * Construct a Kalendar, given the leading days and the total days
     * 
     * @exception IllegalArgumentException
     *                If year out of range
     */
    public Kalendar(int year, int month, int today) {
        super();
        setYYMMDD(year, month, today);
        buildGUI();
        recompute();
    }

    private void setYYMMDD(int year, int month, int today) {
        yy = year;
        mm = month;
        dd = today;
    }
    String[] months = {"January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"};

    /** Build the GUI. Assumes that setYYMMDD has been called. */
    private void buildGUI() {
        getAccessibleContext().setAccessibleDescription(
                "Calendar not accessible yet. Sorry!");
        setBorder(BorderFactory.createEtchedBorder());

        setLayout(new BorderLayout());

        JPanel tp = new JPanel();
        tp.add(monthChoice = new JComboBox());
        for (int i = 0; i < months.length; i++) {
            monthChoice.addItem(months[i]);
        }
        monthChoice.setSelectedItem(months[mm]);
        monthChoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int i = monthChoice.getSelectedIndex();
                if (i >= 0) {
                    mm = i;
                    // System.out.println("Month=" + mm);
                    recompute();
                }
            }
        });
        monthChoice.getAccessibleContext().setAccessibleName("Months");
        monthChoice.getAccessibleContext().setAccessibleDescription(
                "Choose a month of the year");

        tp.add(yearChoice = new JComboBox());
        yearChoice.setEditable(true);
        for (int i = yy - 5; i < yy + 5; i++) {
            yearChoice.addItem(Integer.toString(i));
        }
        yearChoice.setSelectedItem(Integer.toString(yy));
        yearChoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int i = yearChoice.getSelectedIndex();
                if (i >= 0) {
                    yy = Integer.parseInt(yearChoice.getSelectedItem().toString());
                    // System.out.println("Year=" + yy);
                    recompute();
                }
            }
        });
        add(BorderLayout.CENTER, tp);

        JPanel bp = new JPanel();
        bp.setLayout(new GridLayout(7, 7));
        labs = new JButton[6][7]; // first row is days

        bp.add(b0 = new JButton("S"));
        bp.add(new JButton("M"));
        bp.add(new JButton("T"));
        bp.add(new JButton("W"));
        bp.add(new JButton("R"));
        bp.add(new JButton("F"));
        bp.add(new JButton("S"));

        ActionListener dateSetter = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String num = e.getActionCommand();
                if (!num.equals("")) {
                    // set the current day highlighted
                    setDayActive(Integer.parseInt(num));
                    set_current_date();
                    // When this becomes a Bean, you can
                    // fire some kind of DateChanged event here.
                    // Also, build a similar daySetter for day-of-week btns.
                }
            }
        };

        // Construct all the buttons, and add them.
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                bp.add(labs[i][j] = new JButton(""));
                labs[i][j].addActionListener(dateSetter);
            }
        }

        add(BorderLayout.SOUTH, bp);
    }
    public final static int dom[] = {31, 28, 31, 30, /* jan feb mar apr */
        31, 30, 31, 31, /* may jun jul aug */
        30, 31, 30, 31 /* sep oct nov dec */};

    /** Compute which days to put where, in the Kalendar panel */
    protected void recompute() {
        // System.out.println("Kalendar::recompute: " + yy + ":" + mm + ":" + dd);
        if (mm < 0 || mm > 11) {
            throw new IllegalArgumentException("Month " + mm
                    + " bad, must be 0-11");
        }
        clearDayActive();
        calendar = new GregorianCalendar(yy, mm, dd);

        // Compute how much to leave before the first.
        // getDay() returns 0 for Sunday, which is just right.
        leadGap = new GregorianCalendar(yy, mm, 1).get(Calendar.DAY_OF_WEEK) - 1;
        // System.out.println("leadGap = " + leadGap);

        int daysInMonth = dom[mm];
        if (isLeap(calendar.get(Calendar.YEAR)) && mm == 1) //    if (isLeap(calendar.get(Calendar.YEAR)) && mm > 1)
        {
            ++daysInMonth;
        }

        // Blank out the labels before 1st day of month
        for (int i = 0; i < leadGap; i++) {
            labs[0][i].setText("");
        }

        // Fill in numbers for the day of month.
        for (int i = 1; i <= daysInMonth; i++) {
            JButton b = labs[(leadGap + i - 1) / 7][(leadGap + i - 1) % 7];
            b.setText(Integer.toString(i));
        }

        // 7 days/week * up to 6 rows
        for (int i = leadGap + 1 + daysInMonth; i < 6 * 7; i++) {
            labs[(i) / 7][(i) % 7].setText("");
        }

        // Shade current day, only if current month
        if (thisYear == yy && mm == thisMonth) {
            setDayActive(dd); // shade the box for today
        }
        // Say we need to be drawn on the screen
        repaint();
    }

    /**
     * Leap = VESOKOSNIJ GOD!
     * isLeap() returns true if the given year is a Leap Year.
     * 
     * "a year is a leap year if it is divisible by 4 but not by 100, except
     * that years divisible by 400 *are* leap years." -- Kernighan & Ritchie,
     * _The C Programming Language_, p 37.
     */
    public boolean isLeap(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        }
        return false;
    }

    /** Set the year, month, and day */
    public void setDate(int yy, int mm, int dd) {
        // System.out.println("Kalendar::setDate");
        this.yy = yy;
        this.mm = mm; // starts at 0, like Date
        this.dd = dd;
        recompute();
    }

    /** Unset any previously highlighted day */
    private void clearDayActive() {
        JButton b;

        // First un-shade the previously-selected square, if any
        if (activeDay > 0) {
            b = labs[(leadGap + activeDay - 1) / 7][(leadGap + activeDay - 1) % 7];
            b.setBackground(b0.getBackground());
            b.repaint();
            activeDay = -1;
        }
    }
    private int activeDay = -1;

    /** Set just the day, on the current month */
    private void setDayActive(int newDay) {

        clearDayActive();

        // Set the new one
        if (newDay <= 0) {
            dd = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
        } else {
            dd = newDay;
        }
        // Now shade the correct square
        Component square = labs[(leadGap + newDay - 1) / 7][(leadGap + newDay - 1) % 7];
        square.setBackground(Color.red);
        square.repaint();
        activeDay = newDay;
    }
    //============================================================================

    private void set_current_date() {
        String month = "" + (mm + 1);
        if (month.length() == 1) {
            String temp = "0";
            temp += month;
            month = temp;
        }

        String day = "" + dd;
        if (day.length() == 1) {
            String temp = "0";
            temp += day;
            day = temp;
        }

        CURRENT_DATE_YYYY_MM_DD = yy + "-" + month + "-" + day;
    }

    public String get_current_date_YYYY_MM_DD() {
        return CURRENT_DATE_YYYY_MM_DD;
    }

    /**
     * This one is IMPORTANT
     * @param time_hh_mm
     * @return 
     */
    public long get_selected_date_and_time_as_millis(String time_hh_mm) {
        String date_str = get_current_date_YYYY_MM_DD() + " " + time_hh_mm;
        return dateToMillisConverter(date_str);
    }

    //============================================================================
    private static long dateToMillisConverter(String date_yyyy_MM_dd) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // this works to!
        try {
            return formatter.parse(date_yyyy_MM_dd).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(Kalendar.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    //============================================================================
    /** For testing, a main program */
    public static void main(String[] av) {
        JFrame f = new JFrame("Cal");
        Container c = f.getContentPane();
        c.setLayout(new FlowLayout());

        // for this test driver, hardcode 1995/02/10.
        c.add(new Kalendar(1995, 2 - 1, 10));

        // and beside it, the current month.
        c.add(new Kalendar());

        f.pack();
        f.setVisible(true);
    }
   
}
