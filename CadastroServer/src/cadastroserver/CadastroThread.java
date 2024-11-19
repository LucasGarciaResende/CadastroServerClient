package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.controller.MovimentoJpaController;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;
import cadastroserver.model.Movimento;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class CadastroThread extends Thread {
    private ProdutoJpaController ctrlProd;
    private UsuarioJpaController ctrlUsu;
    private MovimentoJpaController ctrlMov;
    private Socket s1;

    public CadastroThread(ProdutoJpaController ctrlProd, UsuarioJpaController ctrlUsu, MovimentoJpaController ctrlMov, Socket s1) {
        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
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
                output.writeObject("Login inválido!");
                return;
            }

            while (true) {
                String command = (String) input.readObject();

                if ("L".equalsIgnoreCase(command)) {
                    List<Produto> produtos = ctrlProd.findProdutoEntities();
                    output.writeObject(produtos);
                } else if ("E".equalsIgnoreCase(command) || "S".equalsIgnoreCase(command)) {
                    try {
                        int pessoaId = (int) input.readObject();
                        int produtoId = (int) input.readObject();
                        int quantidade = (int) input.readObject();
                        float valorUnitario = (float) input.readObject();

                        Produto produto = ctrlProd.findProduto(produtoId);
                        if (produto == null) {
                            output.writeObject("ID Inválido");
                            continue;
                        }

                        Movimento movimento = new Movimento();
                        movimento.setIdusuario(usuario);
                        movimento.setTipo(command.toUpperCase().charAt(0));
                        movimento.setIdpessoa(pessoaId);
                        movimento.setIdproduto(produto);
                        movimento.setQuantidade(quantidade);
                        movimento.setValorUnitario(valorUnitario);

                        ctrlMov.create(movimento);

                        if ("E".equalsIgnoreCase(command)) {
                            produto.setQuantidade(produto.getQuantidade() + quantidade);
                        } else if ("S".equalsIgnoreCase(command)) {
                            if (produto.getQuantidade() < quantidade) {
                                output.writeObject("Erro!");
                                continue;
                            }
                            produto.setQuantidade(produto.getQuantidade() - quantidade);
                        }
                        ctrlProd.edit(produto);

                        output.writeObject("Movimento salvo com sucesso!.");
                    } catch (Exception e) {
                        output.writeObject("Erro: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else if ("X".equalsIgnoreCase(command)) {
                    output.writeObject("Conexão fechada");
                    break;
                } else {
                    output.writeObject("Commando errado.");
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
