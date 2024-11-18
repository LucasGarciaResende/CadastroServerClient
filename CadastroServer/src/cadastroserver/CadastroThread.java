package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class CadastroThread extends Thread {
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;
    private Socket s1;

    public CadastroThread(ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream input = new ObjectInputStream(s1.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(s1.getOutputStream());
        ) {
            String email = (String) input.readObject();
            String senha = (String) input.readObject();
            Usuario usuario = ctrlUsu.findUsuario(email, senha);
            if (usuario == null) {
                output.writeObject("Invalid login or password.");
                return;
            }
            while (true) {
                String command = (String) input.readObject();

                if ("L".equalsIgnoreCase(command)) {
                    List<Produto> produtos = ctrl.findProdutoEntities();
                    output.writeObject(produtos);
                } else if ("EXIT".equalsIgnoreCase(command)) {
                    output.writeObject("Connection closed.");
                    break;
                } else {
                    output.writeObject("Unknown command.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (s1 != null && !s1.isClosed()) {
                    s1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
