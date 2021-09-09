/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author KOCMOC
 */
public class MyOutputStream extends OutputStream {

    private PipedOutputStream out = new PipedOutputStream();
    private Reader reader;
    private JTextPane errOutput;

    public MyOutputStream(JTextPane jTextPane) throws IOException {
        PipedInputStream in = new PipedInputStream(out);
        reader = new InputStreamReader(in, "UTF-8");
        this.errOutput = jTextPane;
    }

    @Override
    public void write(int i) throws IOException {
        out.write(i);
    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {
        out.write(bytes, i, i1);
        flush();
    }

    @Override
    public void flush() throws IOException {
        if (reader.ready()) {
            char[] chars = new char[1024];
            int n = reader.read(chars);

            // this is your text
            String txt = new String(chars, 0, n);

            // write to System.err in this example
//            System.err.print(txt);
            //
//            System.out.println(txt);
            //
            StyledDocument sd = errOutput.getStyledDocument();
            try {
                sd.insertString(sd.getLength(), txt, null);
            } catch (BadLocationException ex) {
                Logger.getLogger(MyOutputStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
