package ar.edu.utn.frbb.tup.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class BanelcoService {
    private final Random random = new Random();

    public boolean transferir() {
        // Simula la transferencia a otro banco con una respuesta aleatoria
        // Retorna true o false aleatoriamente
        return random.nextBoolean(); // Retorna true o false con igual probabilidad
}
}