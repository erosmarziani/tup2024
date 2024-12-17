/*import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CuentaDaoTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private CuentaDaoImpl cuentaDaoImpl;  // Asegúrate de que la implementación sea la correcta

    private static final String FILE_PATH = "ruta/a/tu/archivo";  // Establecer la ruta a tu archivo

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerCuentaPorId_EncontrarCuenta() throws Exception {
        // Mock de un cliente
        Cliente mockCliente = new Cliente();
        mockCliente.setDni(458597);
        when(clienteDao.obtenerClientePorDNI(458597)).thenReturn(mockCliente);

        // Simular el archivo con datos
        String data = "idCuenta;fechaAlta;balance;tipoCuenta;dniTitular;moneda\n"
                + "123456;2024-12-15;50000.0;CUENTA_CORRIENTE;458597;ARS\n"
                + "52525648;2024-12-15;1.25E7;CAJA_AHORRO;38583564;ARS\n"
                + "1;2024-12-16;1.25E7;CAJA_AHORRO;12526398;ARS";

        Path path = Paths.get(FILE_PATH);
        Files.write(path, data.getBytes());

        // Prueba el método obtenerCuentaPorId con el ID existente
        Cuenta cuenta = cuentaDaoImpl.obtenerCuentaPorId(123456);

        assertNotNull(cuenta);
        assertEquals(123456, cuenta.getNumeroCuenta());
        assertEquals(458597, cuenta.getTitular().getDni());

        // Limpiar después del test
        Files.deleteIfExists(path);
    }

    @Test
    public void testObtenerCuentaPorId_NoEncontrarCuenta() throws Exception {
        // Simular el archivo con datos
        String data = "idCuenta;fechaAlta;balance;tipoCuenta;dniTitular;moneda\n"
                + "123456;2024-12-15;50000.0;CUENTA_CORRIENTE;458597;ARS\n"
                + "52525648;2024-12-15;1.25E7;CAJA_AHORRO;38583564;ARS\n"
                + "1;2024-12-16;1.25E7;CAJA_AHORRO;12526398;ARS";

        Path path = Paths.get(FILE_PATH);
        Files.write(path, data.getBytes());

        // Prueba con un ID que no existe
        Cuenta cuenta = cuentaDaoImpl.obtenerCuentaPorId(999999);

        assertNull(cuenta);

        // Limpiar después del test
        Files.deleteIfExists(path);
    }

    @Test
    public void testObtenerCuentaPorId_ErrorLectura() throws Exception {
        // Simular un error de lectura
        Path path = Paths.get(FILE_PATH);
        Files.write(path, "incorrect data".getBytes());

        Exception exception = assertThrows(ErrorArchivoException.class, () -> {
            cuentaDaoImpl.obtenerCuentaPorId(123456);
        });

        assertTrue(exception.getMessage().contains("Error al leer el archivo"));

        // Limpiar después del test
        Files.deleteIfExists(path);
    }
}
*/