package com.eventofacil.service;

import com.eventofacil.model.CategoriaEvento;
import com.eventofacil.model.Evento;
import com.eventofacil.model.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersistenciaService {

    private static final String USERS_FILE = "users.data";
    private static final String EVENTS_FILE = "events.data";
    private static final String DELIMITER = ";";

    public static List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Path path = Paths.get(USERS_FILE);
        if (!Files.exists(path)) return usuarios;

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(DELIMITER);
                
                if (parts.length >= 3) { // <-- Correção de robustez
                    usuarios.add(new Usuario(Integer.parseInt(parts[0]), parts[1], parts[2]));
                } else {
                    System.err.println("AVISO: Linha mal formatada no arquivo " + USERS_FILE + " ignorada: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERRO: Falha ao carregar usuários do arquivo " + USERS_FILE + ". " + e.getMessage());
        }
        return usuarios;
    }

    public static void salvarDados(List<Usuario> usuarios, List<Evento> eventos) {
        List<String> userLines = usuarios.stream()
                .map(u -> u.getId() + DELIMITER + u.getNome() + DELIMITER + u.getEmail())
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(USERS_FILE), userLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao salvar usuários. " + e.getMessage());
        }

        List<String> eventLines = eventos.stream().map(e -> {
            String idsParticipantes = e.getParticipantes().stream()
                    .map(u -> String.valueOf(u.getId()))
                    .collect(Collectors.joining(","));
            return String.join(DELIMITER,
                String.valueOf(e.getId()), e.getNome(), e.getEndereco(), e.getCategoria().name(),
                e.getHorario().toString(), e.getDescricao(), idsParticipantes
            );
        }).collect(Collectors.toList());
        try {
            Files.write(Paths.get(EVENTS_FILE), eventLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao salvar eventos. " + e.getMessage());
        }
    }

    public static List<Evento> carregarEventos(List<Usuario> todosUsuarios) {
        List<Evento> eventos = new ArrayList<>();
        Path path = Paths.get(EVENTS_FILE);
        if (!Files.exists(path)) return eventos;

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(DELIMITER);
                if (parts.length < 6) continue;

                Evento evento = new Evento(
                    Integer.parseInt(parts[0]), parts[1], parts[2], CategoriaEvento.valueOf(parts[3]),
                    LocalDateTime.parse(parts[4]), parts[5]
                );
                
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    String[] idsParticipantes = parts[6].split(",");
                    for (String idStr : idsParticipantes) {
                        int id = Integer.parseInt(idStr);
                        todosUsuarios.stream().filter(u -> u.getId() == id).findFirst()
                            .ifPresent(evento::adicionarParticipante);
                    }
                }
                eventos.add(evento);
            }
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao carregar eventos do arquivo " + EVENTS_FILE + ". " + e.getMessage());
        }
        return eventos;
    }
}