package mySwing;
 
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
 
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
public class JFilePicker extends JPanel {
    //private final String textFieldLabel;
    //private final String buttonLabel;
     
    private final JLabel label;
    private final JTextField textField;
    private final JButton button;
     
    private final JFileChooser fileChooser;
     
    private int mode;
    public static final int MODE_OPEN = 1;
    public static final int MODE_SAVE = 2;
     
    public JFilePicker(String textFieldLabel, String buttonLabel) {
        //this.textFieldLabel = textFieldLabel;
        //this.buttonLabel = buttonLabel;
         
        fileChooser = new JFileChooser();
         
        //setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setLayout(new BorderLayout(5, 0));
 
        // creates the GUI
        label = new JLabel(textFieldLabel);
         
        textField = new JTextField(30);
        button = new JButton(buttonLabel);
         
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonActionPerformed(evt);            
            }
        });
         
        add(label,BorderLayout.WEST);
        add(textField,BorderLayout.CENTER);
        add(button,BorderLayout.EAST);
         
    }
     
    private void buttonActionPerformed(ActionEvent evt) {
        if (mode == MODE_OPEN) {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        } else if (mode == MODE_SAVE) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }
 
    public void addFileTypeFilter(String extension, String description) {
        FileTypeFilter filter = new FileTypeFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
    }
     
    public void setMode(int mode) {
        this.mode = mode;
    }
     
    public String getSelectedFilePath() {
        return textField.getText();
    }
     
    public JFileChooser getFileChooser() {
        return this.fileChooser;
    }
    
    public void setPath(String path) {
        textField.setText(path);
        fileChooser.setCurrentDirectory(new File(path));
    }
    
}