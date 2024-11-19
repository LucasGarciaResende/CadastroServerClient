package cadastroclientev2;

import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class CadastroClienteV2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter email: ");
            String email = keyboard.readLine();
            System.out.print("Enter password: ");
            String senha = keyboard.readLine();
            output.writeObject(email);
            output.writeObject(senha);
            String response = (String) input.readObject();
            if (!"Invalid login or password.".equals(response)) {
                System.out.println("Login successful!");
                JFrame frame = new JFrame("Message Window");
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                frame.add(new JScrollPane(textArea));
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                Thread asyncThread = new Thread(() -> {
                    try {
                        while (true) {
                            Object serverMessage = input.readObject();
                            if (serverMessage instanceof String) {
                                textArea.append(serverMessage + "\n");
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Connection closed.");
                    }
                });
                asyncThread.start();
                while (true) {
                    System.out.println("\nMenu:");
                    System.out.println("L - Listar");
                    System.out.println("E - Entrada");
                    System.out.println("S - Saida");
                    System.out.println("X - Finalizar");
                    System.out.print("Choose an option: ");
                    String command = keyboard.readLine().toUpperCase();
                    output.writeObject(command);
                    if ("X".equals(command)) {
                        System.out.println("Exiting...");
                        break;
                    } else if ("L".equals(command)) {
                        System.out.println("Listing products...");
                    } else if ("E".equals(command) || "S".equals(command)) {
                        System.out.print("Enter Pessoa ID: ");
                        Long pessoaId = Long.parseLong(keyboard.readLine());
                        output.writeObject(pessoaId);
                        System.out.print("Enter Produto ID: ");
                        Long produtoId = Long.parseLong(keyboard.readLine());
                        output.writeObject(produtoId);
                        System.out.print("Enter Quantidade: ");
                        int quantidade = Integer.parseInt(keyboard.readLine());
                        output.writeObject(quantidade);
                        System.out.print("Enter Valor Unitario: ");
                        double valorUnitario = Double.parseDouble(keyboard.readLine());
                        output.writeObject(valorUnitario);
                        System.out.println("Operation sent to server.");
                    } else {
                        System.out.println("Invalid command.");
                    }
                }
            } else {
                System.out.println("Invalid login or password.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
