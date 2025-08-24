package com.eventofacil.service;

import com.eventofacil.model.CategoriaEvento;
import com.eventofacil.model.Evento;
import com.eventofacil.model.Usuario;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventoService {
    private List<Usuario> usuarios;
    private List<Evento> eventos;
    private int proximoIdUsuario = 1;
    private int proximoIdEvento = 1;

    public EventoService() {
        this.usuarios = PersistenciaService.carregarUsuarios();
        this.eventos = PersistenciaService.carregarEventos(this.usuarios);
        
        if (!usuarios.isEmpty()) {
            proximoIdUsuario = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        }
        if (!eventos.isEmpty()) {
            proximoIdEvento = eventos.stream().mapToInt(Evento::getId).max().orElse(0) + 1;
        }
    }

    private void salvarTodosOsDados() {
        PersistenciaService.salvarDados(this.usuarios, this.eventos);
    }
    
    public Usuario cadastrarUsuario(String nome, String email) {
        Usuario novoUsuario = new Usuario(proximoIdUsuario++, nome, email);
        this.usuarios.add(novoUsuario);
        salvarTodosOsDados();
        return novoUsuario;
    }
    
    // ***** ESTE É O MÉTODO QUE ESTAVA FALTANDO *****
    public Evento cadastrarEvento(String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        Evento novoEvento = new Evento(proximoIdEvento++, nome, endereco, categoria, horario, descricao);
        this.eventos.add(novoEvento);
        salvarTodosOsDados();
        return novoEvento;
    }

    public Optional<Usuario> encontrarUsuarioPorId(int id) {
        return this.usuarios.stream().filter(u -> u.getId() == id).findFirst();
    }
    
    public boolean inscreverUsuarioEmEvento(Usuario usuario, Evento evento) {
        if (evento.getHorario().isBefore(LocalDateTime.now()) || evento.getParticipantes().contains(usuario)) {
            return false;
        }
        evento.adicionarParticipante(usuario);
        salvarTodosOsDados();
        return true;
    }

    public void cancelarInscricao(Usuario usuario, Evento evento) {
        evento.removerParticipante(usuario);
        salvarTodosOsDados();
    }
    
    public List<Evento> listarEventosFuturos() {
        return this.eventos.stream()
            .filter(e -> e.getHorario().isAfter(LocalDateTime.now()))
            .sorted(Comparator.comparing(Evento::getHorario))
            .collect(Collectors.toList());
    }
    
    public List<Evento> listarEventosDoUsuario(Usuario usuario) {
        return this.eventos.stream()
            .filter(e -> e.getParticipantes().contains(usuario))
            .sorted(Comparator.comparing(Evento::getHorario))
            .collect(Collectors.toList());
    }
}