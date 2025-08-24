package com.eventofacil;

import javax.swing.*;
import java.awt.*;
import com.eventofacil.service.EventoService;
import com.eventofacil.model.Usuario;

public class TelaLogin extends JFrame {
    private EventoService eventoService = new EventoService();
    private JTextField campoIdUsuario;

    public TelaLogin() {
        setTitle("Login - Sistema de Eventos");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel labelTitulo = new JLabel("Bem-vindo! Faça seu login", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelPrincipal.add(labelTitulo, BorderLayout.NORTH);

        JPanel painelInput = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelId = new JLabel("ID do Usuário:");
        campoIdUsuario = new JTextField(10);
        painelInput.add(labelId);
        painelInput.add(campoIdUsuario);
        painelPrincipal.add(painelInput, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoLogin = new JButton("Entrar");
        JButton botaoCadastro = new JButton("Cadastrar Novo Usuário");
        painelBotoes.add(botaoLogin);
        painelBotoes.add(botaoCadastro);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelPrincipal);

        botaoLogin.addActionListener(e -> realizarLogin());
        botaoCadastro.addActionListener(e -> cadastrarNovoUsuario());
    }

    private void realizarLogin() {
        String idTexto = campoIdUsuario.getText().trim();
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite seu ID.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idTexto);
            eventoService.encontrarUsuarioPorId(id).ifPresentOrElse(
                usuario -> {
                    dispose(); 
                    SwingUtilities.invokeLater(() -> new TelaPrincipal(usuario, eventoService).setVisible(true));
                },
                () -> JOptionPane.showMessageDialog(this, "ERRO: Usuário com ID " + id + " não encontrado.", "Erro de Login", JOptionPane.ERROR_MESSAGE)
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um ID numérico válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarNovoUsuario() {
        String nome = JOptionPane.showInputDialog(this, "Digite seu nome completo:", "Cadastro de Usuário", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) {
            return; // Usuário cancelou ou não digitou nada
        }

        String email = JOptionPane.showInputDialog(this, "Digite seu e-mail:", "Cadastro de Usuário", JOptionPane.PLAIN_MESSAGE);
        if (email == null || email.trim().isEmpty()) {
            return; // Usuário cancelou ou não digitou nada
        }

        Usuario novoUsuario = eventoService.cadastrarUsuario(nome.trim(), email.trim());
        JOptionPane.showMessageDialog(this, 
            "Usuário cadastrado com sucesso!\nSeu ID para login é: " + novoUsuario.getId(),
            "Cadastro Concluído", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}