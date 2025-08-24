package com.eventofacil;

import com.eventofacil.model.CategoriaEvento;
import com.eventofacil.model.Evento;
import com.eventofacil.model.Usuario;
import com.eventofacil.service.EventoService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaPrincipal extends JFrame {
    private Usuario usuarioLogado;
    private EventoService eventoService;
    private JList<Evento> listaEventos;
    private DefaultListModel<Evento> listModel;

    public TelaPrincipal(Usuario usuarioLogado, EventoService eventoService) {
        this.usuarioLogado = usuarioLogado;
        this.eventoService = eventoService;

        setTitle("Sistema de Eventos - Bem-vindo(a), " + usuarioLogado.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel labelBoasVindas = new JLabel("Logado como: " + usuarioLogado.getNome() + " (ID: " + usuarioLogado.getId() + ")");
        JButton botaoLogout = new JButton("Logout");
        painelSuperior.add(labelBoasVindas, BorderLayout.CENTER);
        painelSuperior.add(botaoLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaEventos = new JList<>(listModel);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEventos.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(listaEventos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Eventos"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnProximosEventos = new JButton("Ver Próximos Eventos");
        JButton btnMeusEventos = new JButton("Ver Meus Eventos");
        JButton btnCadastrarEvento = new JButton("Cadastrar Novo Evento");
        JButton btnInscrever = new JButton("Inscrever-se no Evento");
        JButton btnCancelarInscricao = new JButton("Cancelar Inscrição");
        painelBotoes.add(btnProximosEventos);
        painelBotoes.add(btnMeusEventos);
        painelBotoes.add(btnCadastrarEvento);
        painelBotoes.add(btnInscrever);
        painelBotoes.add(btnCancelarInscricao);
        add(painelBotoes, BorderLayout.SOUTH);

        btnProximosEventos.addActionListener(e -> carregarProximosEventos());
        btnMeusEventos.addActionListener(e -> carregarMeusEventos());
        btnCadastrarEvento.addActionListener(e -> cadastrarNovoEvento());
        btnInscrever.addActionListener(e -> inscreverEmEvento());
        btnCancelarInscricao.addActionListener(e -> cancelarInscricao());
        botaoLogout.addActionListener(e -> fazerLogout());

        carregarProximosEventos();
    }

    private void carregarProximosEventos() {
        atualizarLista(eventoService.listarEventosFuturos(), "Próximos Eventos");
    }
    
    private void carregarMeusEventos() {
        atualizarLista(eventoService.listarEventosDoUsuario(usuarioLogado), "Meus Eventos Inscritos");
    }

    private void atualizarLista(List<Evento> eventos, String titulo) {
        listModel.clear();
        for (Evento evento : eventos) {
            listModel.addElement(evento);
        }
        ((JScrollPane) listaEventos.getParent().getParent()).setBorder(BorderFactory.createTitledBorder(titulo));
    }
    
    private void cadastrarNovoEvento() {
        try {
            // MUDANÇA 1: Usar JFormattedTextField para data e hora com máscaras
            JTextField nomeField = new JTextField();
            JTextField enderecoField = new JTextField();
            JTextField descricaoField = new JTextField();
            JComboBox<CategoriaEvento> categoriaComboBox = new JComboBox<>(CategoriaEvento.values());
            JFormattedTextField dataField = new JFormattedTextField(new MaskFormatter("##/##/####"));
            JFormattedTextField horaField = new JFormattedTextField(new MaskFormatter("##:##"));
            
            // Dica: preencher com a data atual para facilitar
            dataField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
            horaField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));

            // MUDANÇA 2: Adicionar os novos campos ao formulário
            JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
            painelFormulario.add(new JLabel("Nome do Evento:"));
            painelFormulario.add(nomeField);
            painelFormulario.add(new JLabel("Endereço:"));
            painelFormulario.add(enderecoField);
            painelFormulario.add(new JLabel("Descrição:"));
            painelFormulario.add(descricaoField);
            painelFormulario.add(new JLabel("Categoria:"));
            painelFormulario.add(categoriaComboBox);
            painelFormulario.add(new JLabel("Data (dd/MM/yyyy):"));
            painelFormulario.add(dataField);
            painelFormulario.add(new JLabel("Hora (HH:mm):"));
            painelFormulario.add(horaField);

            int result = JOptionPane.showConfirmDialog(this, painelFormulario, "Cadastrar Novo Evento", 
                                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // MUDANÇA 3: Combinar data e hora antes de salvar
                String nome = nomeField.getText();
                String endereco = enderecoField.getText();
                String descricao = descricaoField.getText();
                CategoriaEvento categoria = (CategoriaEvento) categoriaComboBox.getSelectedItem();
                String dataHoraTexto = dataField.getText() + " " + horaField.getText();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime horario = LocalDateTime.parse(dataHoraTexto, formatter);

                if (nome.trim().isEmpty() || endereco.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e Endereço são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                eventoService.cadastrarEvento(nome, endereco, categoria, horario, descricao);
                
                JOptionPane.showMessageDialog(this, "Evento cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarProximosEventos();

            }
        } catch (ParseException e) {
            // Este erro é da criação da MaskFormatter, improvável de acontecer com máscara fixa
            JOptionPane.showMessageDialog(this, "Ocorreu um erro interno ao criar o formulário.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data ou hora inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao cadastrar o evento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inscreverEmEvento() {
        Evento eventoSelecionado = listaEventos.getSelectedValue();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um evento da lista primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja se inscrever no evento:\n" + eventoSelecionado.getNome() + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            if (eventoService.inscreverUsuarioEmEvento(usuarioLogado, eventoSelecionado)) {
                JOptionPane.showMessageDialog(this, "Inscrição realizada com sucesso!");
                carregarProximosEventos();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível se inscrever.\nVocê já pode estar inscrito ou o evento já ocorreu.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarInscricao() {
        Evento eventoSelecionado = listaEventos.getSelectedValue();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para cancelar a inscrição.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!eventoSelecionado.getParticipantes().contains(usuarioLogado)) {
             JOptionPane.showMessageDialog(this, "Você não está inscrito neste evento.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
             return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Cancelar inscrição em:\n" + eventoSelecionado.getNome() + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            eventoService.cancelarInscricao(usuarioLogado, eventoSelecionado);
            JOptionPane.showMessageDialog(this, "Inscrição cancelada!");
            carregarMeusEventos();
        }
    }

    private void fazerLogout() {
        dispose();
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}