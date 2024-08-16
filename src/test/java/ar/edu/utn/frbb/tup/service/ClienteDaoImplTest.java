public package ar.edu.utn.frbb.tup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;

public class ClienteDaoImplTest {


    @InjectMocks
    private ClienteDaoImpl clienteDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); //Inicializar los mocks
    }

    @Test
    public void testCrearCliente() {
}