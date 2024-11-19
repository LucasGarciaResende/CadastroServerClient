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
            System.out.print("Entre email: ");
            String email = keyboard.readLine();
            System.out.print("Enter senha: ");
            String senha = keyboard.readLine();
            output.writeObject(email);
            output.writeObject(senha);
            String response = (String) input.readObject();
            if (!"Login inválido!.".equals(response)) {
                System.out.println("Login efeituado!");
                JFrame frame = new JFrame("Janela");
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
                        System.out.println("Conexão fechada.");
                    }
                });
                asyncThread.start();
                while (true) {
                    System.out.println("\nMenu:");
                    System.out.println("L - Listar");
                    System.out.println("E - Entrada");
                    System.out.println("S - Saida");
                    System.out.println("X - Finalizar");
                    System.out.print("Escolha uma opção: ");
                    String command = keyboard.readLine().toUpperCase();
                    output.writeObject(command);
                    if ("X".equals(command)) {
                        System.out.println("Fechando...");
                        break;
                    } else if ("L".equals(command)) {
                        System.out.println("Listing products...");
                    } else if ("E".equals(command) || "S".equals(command)) {
                        System.out.print("Entre ID Pessoa: ");
                        Long pessoaId = Long.parseLong(keyboard.readLine());
                        output.writeObject(pessoaId);
                        System.out.print("Entre ID Produto: ");
                        Long produtoId = Long.parseLong(keyboard.readLine());
                        output.writeObject(produtoId);
                        System.out.print("Entre a Quantidade: ");
                        int quantidade = Integer.parseInt(keyboard.readLine());
                        output.writeObject(quantidade);
                        System.out.print("Entre o Valor Unitario: ");
                        double valorUnitario = Double.parseDouble(keyboard.readLine());
                        output.writeObject(valorUnitario);
                        System.out.println("Operação foi um sucesso");
                    } else {
                        System.out.println("Comando Inválido.");
                    }
                }
            } else {
                System.out.println("Login inválido!");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
