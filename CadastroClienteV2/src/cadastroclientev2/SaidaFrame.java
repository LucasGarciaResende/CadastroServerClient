package cadastroclientev2;

import javax.swing.*;
import java.awt.*;

public class SaidaFrame extends JDialog {
    public JTextArea texto;

    public SaidaFrame() {
        setBounds(100, 100, 400, 300);
        setModal(false);
        texto = new JTextArea();
        texto.setEditable(false);
        texto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(texto), BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Janela");
    }
}
