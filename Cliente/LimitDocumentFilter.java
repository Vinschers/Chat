import java.awt.EventQueue;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LimitDocumentFilter extends DocumentFilter {

    private int limite;

    public LimitDocumentFilter(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("Limit can not be <= 0");
        }
        this.limite = limite;
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int currentLength = fb.getDocument().getLength();
        int overLimit = (currentLength + text.length()) - limite - length;
        if (overLimit > 0) {
            text = text.substring(0, text.length() - overLimit);
        }
        if (text.length() > 0) {
            super.replace(fb, offset, length, text, attrs); 
        }
    }
    public String toString() {
        return super.toString();
    }
    public int hashCode() {
        int ret = super.hashCode();

        ret = 2 * Integer.valueOf(limite).hashCode();

        return ret;
    }
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        if (!obj.getClass().equals(this.getClass()))
            return false;
        if (this.limite != ((LimitDocumentFilter) obj).limite)
            return false;

        return true;
    }
}