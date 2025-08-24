package com.eventofacil.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private int id;
    private String nome;
    private String endereco;
    private CategoriaEvento categoria;
    private LocalDateTime horario;
    private String descricao;
    private List<Usuario> participantes;

    public Evento(int id, String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantes = new ArrayList<>();
    }
    
    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; } // <-- MÉTODO QUE FALTAVA
    public CategoriaEvento getCategoria() { return categoria; }
    public LocalDateTime getHorario() { return horario; }
    public String getDescricao() { return descricao; } // <-- MÉTODO QUE FALTAVA
    public List<Usuario> getParticipantes() { return participantes; }

    public void adicionarParticipante(Usuario usuario) {
        if (!this.participantes.contains(usuario)) {
            this.participantes.add(usuario);
        }
    }

    public void removerParticipante(Usuario usuario) {
        this.participantes.remove(usuario);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format(
            "ID: %d | %s (%s) | %s",
            id, nome, categoria, horario.format(formatter)
        );
    }
}