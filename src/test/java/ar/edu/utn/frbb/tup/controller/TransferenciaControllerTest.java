package ar.edu.utn.frbb.tup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;
import ar.edu.utn.frbb.tup.service.exception.TransferenciaServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TransferenciaControllerTest {

    @Mock
    private TransferenciaService transferenciaService;

    @Mock
    private TransferenciaValidator transferenciaValidator;

    @InjectMocks
    private TransferenciaController transferenciaController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransferenciaExitosa() throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException {
        // Arrange
        TransferenciaDto transferenciaDto = getTransferenciaDto();

        // Act
        ResponseEntity<ApiResponse> response = transferenciaController.transferencia(transferenciaDto);

        // Assert
        verify(transferenciaValidator, times(1)).validarTransferencia(transferenciaDto);
        verify(transferenciaService, times(1)).validacionTransferencia(transferenciaDto);

        assertEquals("EXITOSA", response.getBody().getEstado());
        assertEquals("Transferencia realizada exitosamente", response.getBody().getMensaje());
    }

    @Test
    public void testTransferenciaErrorValidacion() throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException {
        // Arrange
        TransferenciaDto transferenciaDto = getTransferenciaDto();
        doThrow(new TransferenciaException("Error en validación"))
                .when(transferenciaValidator).validarTransferencia(transferenciaDto);

        // Act & Assert
        TransferenciaException exception = assertThrows(TransferenciaException.class, () -> 
            transferenciaController.transferencia(transferenciaDto)
        );

        verify(transferenciaValidator, times(1)).validarTransferencia(transferenciaDto);
        verify(transferenciaService, times(0)).validacionTransferencia(transferenciaDto);

        assertEquals("Error en validación", exception.getMessage());
    }

    @Test
    public void testTransferenciaErrorServicio() throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException {
        // Arrange
        TransferenciaDto transferenciaDto = getTransferenciaDto();
        doThrow(new TransferenciaServiceException("Error en la transferencia"))
                .when(transferenciaService).validacionTransferencia(transferenciaDto);

        // Act & Assert
        TransferenciaServiceException exception = assertThrows(TransferenciaServiceException.class, () -> 
            transferenciaController.transferencia(transferenciaDto)
        );

        verify(transferenciaValidator, times(1)).validarTransferencia(transferenciaDto);
        verify(transferenciaService, times(1)).validacionTransferencia(transferenciaDto);

        assertEquals("Error en la transferencia", exception.getMessage());
    }

    // Método auxiliar para obtener TransferenciaDto
    private TransferenciaDto getTransferenciaDto() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("1001");
        transferenciaDto.setIdOrigen("2002");
        transferenciaDto.setImporte("300");
        return transferenciaDto;
    }
}
