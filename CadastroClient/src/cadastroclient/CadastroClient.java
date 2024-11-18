package cadastroclient;

import cadastroserver.model.Produto;

import java.io.*;
import java.net.*;
import java.util.*;

public class CadastroClient {
    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        
        try {

            socket = new Socket("localhost", 4321);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            String login = "op1";
            String password = "op1";
            output.writeObject(login);
            output.writeObject(password);
            String command = "L";
            output.writeObject(command);
            List<?> responseList = (List<?>) input.readObject();         
            if (responseList != null && !responseList.isEmpty()) {
                for (Object entity : responseList) {
                    if (entity instanceof Produto) {
                        Produto produto = (Produto) entity;
                        System.out.println(produto.getNome()); // Printing the 'nome' field
                    }
                }
            } else {
                System.out.println("No data found.");
            }         
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}