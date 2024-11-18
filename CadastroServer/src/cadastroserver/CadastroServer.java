package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.ServerSocket;
import java.net.Socket;

public class CadastroServer {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");     
        ProdutoJpaController ctrl = new ProdutoJpaController(emf);
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Server started on port 4321.");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    CadastroThread cadastroThread = new CadastroThread(ctrl, ctrlUsu, clientSocket);
                    cadastroThread.start();
                } catch (Exception e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error starting the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}