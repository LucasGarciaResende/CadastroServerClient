package cadastroclientev2;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.util.List;

public class ThreadClient extends Thread {
    private ObjectInputStream entrada;
    private JTextArea textArea;
    public ThreadClient(ObjectInputStream entrada, JTextArea textArea) {
        this.entrada = entrada;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object data = entrada.readObject();
                if (data instanceof String) {
                    textArea.append((String) data + "\n");
                }
                else if (data instanceof List<?>) {
                    List<?> list = (List<?>) data;
                    for (Object obj : list) {
                        if (obj instanceof cadastroserver.model.Produto) {
                            cadastroserver.model.Produto produto = (cadastroserver.model.Produto) obj;
                            String productInfo = "Nome: " + produto.getNome() +
                                                 ", Quantidade: " + produto.getQuantidade();
                            textArea.append(productInfo + "\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            textArea.append("Connection lost or error occurred: " + e.getMessage() + "\n");
        }
    }
}
