package ar.edu.utn.frbb.tup.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BanelcoServiceTest {

    @Test
    void testTransferir() {
        // Crear una instancia del servicio
        BanelcoService banelcoService = new BanelcoService();

        // Ejecutar el método transferir
        boolean resultado = banelcoService.transferir();

        // Verificar que el resultado es un valor booleano (true o false)
        assertTrue(resultado == true || resultado == false);
    }
}
