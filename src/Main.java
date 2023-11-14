import javax.swing.*; // Fornece classes para a criação de interfaces gráficas.
import java.awt.*; // Fornece classes para a criação de interfaces gráficas.
import java.awt.event.ActionEvent; // Trata eventos de ação.
import java.awt.event.ActionListener; // Define interfaces para tratamento de eventos.
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        inserir();
        consulta();
    }

    public static void inserir() {
        JFrame frame_inserir = new JFrame("Cadastro de Despesas ");
        frame_inserir.setSize(500, 300); // Aumentei a altura para acomodar o campo de despesas

        frame_inserir.setLocationRelativeTo(null);
        frame_inserir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel_inserir = new JPanel(new GridLayout(6, 2)); // Aumentei para 5 linhas

        JTextField contaField = new JTextField(25);
        JTextField datavcField = new JTextField(25);
        JTextField observField = new JTextField(25);
        JTextField valorField = new JTextField(25); // Novo campo para preço

        JLabel contaLabel = new JLabel("Conta: ");
        JLabel datavcLabel = new JLabel("Data vencimento: ");
        JLabel observLabel = new JLabel("Observação: ");
        JLabel valorLabel = new JLabel("Valor da despesas: "); // Rótulo para valor da conta


        panel_inserir.add(contaLabel);
        panel_inserir.add(contaField);
        panel_inserir.add(datavcLabel);
        panel_inserir.add(datavcField);
        panel_inserir.add(observLabel);
        panel_inserir.add(observField);
        panel_inserir.add(valorLabel);
        panel_inserir.add(valorField); // Adicionando o campo de valor

        JButton cadastrarButton = new JButton("Cadastrar");

        panel_inserir.add(cadastrarButton);

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String conta = contaField.getText();
                String data = datavcField.getText();
                String observacao = observField.getText();
                String valorStr = valorField.getText(); // Obtendo a idade como uma string

                try {
                    double valor = Double.parseDouble(valorStr); // Convertendo a idade para um número inteiro

                    String url = "jdbc:mysql://localhost:3306/basecadastro";
                    String usuario = "root";
                    String senhaBanco = "";

                    try {
                        Connection connection = DriverManager.getConnection(url, usuario, senhaBanco);

                        String sql = "INSERT INTO cadastro_despesas (conta, data_vencimento, observacao, valor) VALUES (?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);

                        preparedStatement.setString(1, conta);
                        preparedStatement.setString(2, data);
                        preparedStatement.setString(3, observacao);
                        preparedStatement.setDouble(4, valor); // Definindo o preço em DOUBLE no PreparedStatement

                        int linhasAfetadas = preparedStatement.executeUpdate();

                        if (linhasAfetadas > 0) {
                            JOptionPane.showMessageDialog(null, "Despesa cadastrada com sucesso !!!");

                            contaField.setText("");
                            datavcField.setText("");
                            observField.setText("");
                            valorField.setText(""); // Limpando o campo de dados

                        } else {
                            JOptionPane.showMessageDialog(null, "Falha ao cadastrar a despesa.");
                        }

                        preparedStatement.close();
                        connection.close();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erro: Não foi possível conectar ao banco de dados.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Erro: O valor deve ser um número inteiro.");
                }
            }
        });

        frame_inserir.add(panel_inserir);
        frame_inserir.setVisible(true);
    }

    public static void consulta() {

        // Crie uma janela de diálogo Swing para a consulta por nome
        JFrame frame = new JFrame("Exibir Despesa");
        frame.setSize(800, 250); // Define o tamanho da janela (largura x altura)

        // Centraliza a janela na tela
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use um painel com layout de grade para organizar os rótulos e campos
        JPanel panel = new JPanel(new GridLayout(2, 2));

        // Campos de entrada
        JTextField nomeField = new JTextField(20);

        // Rótulo
        JLabel nomeLabel = new JLabel("Nome da Despesa: ");

        // Adicione rótulo e campo de entrada ao painel
        panel.add(nomeLabel);
        panel.add(nomeField);

        // Botão de consulta por nome
        JButton consultarButton = new JButton("Consultar");

        // Adicione o botão de consulta ao painel
        panel.add(consultarButton);

        // Área de exibição para mostrar os resultados da consulta
        JTextArea resultadoArea = new JTextArea(5, 20);
        resultadoArea.setEditable(false);

        // Adicione a área de exibição ao painel
        panel.add(resultadoArea);

        // Evento de clique do botão de consulta por nome
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();

                // Informações de conexão com o banco de dados MySQL
                String url = "jdbc:mysql://localhost:3306/basecadastro";
                String usuario = "root";
                String senhaBanco = "";

                try {
                    // Estabelecer uma conexão com o banco de dados
                    Connection connection = DriverManager.getConnection(url, usuario, senhaBanco);

                    // Criar uma declaração SQL para buscar por nome
                    String sql = "SELECT conta, data_vencimento, observacao, valor FROM cadastro_despesas WHERE conta=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, nome);

                    // Executar a consulta
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Limpar a área de exibição
                    resultadoArea.setText("");

                    // Exibir os resultados na área de exibição
                    while (resultSet.next()) {
                        String resultado = "Conta: " + ((ResultSet) resultSet).getString("conta") + ", Valor: " + resultSet.getString("valor") + "\n";
                        resultadoArea.append(resultado);
                    }

                    // Fechar a conexão
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro: Não foi possível realizar a consulta.");
                }
            }
        });

        // Adicione o painel à janela
        frame.add(panel);

        // Tornar a janela visível
        frame.setVisible(true);
    }

}