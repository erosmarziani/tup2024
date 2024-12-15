  package ar.edu.utn.frbb.tup.controller;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;

  import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
  import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;
  import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
  import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
  import ar.edu.utn.frbb.tup.service.TransferenciaService;
  import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;
  import ar.edu.utn.frbb.tup.service.exception.TransferenciaServiceException;


  @RestController
  @RequestMapping("/transferencia")
  public class TransferenciaController {
      
      @Autowired
      private TransferenciaService transferenciaService;

      @Autowired
      private TransferenciaValidator transferenciaValidator;

      @PostMapping()
      public ResponseEntity<ApiResponse> transferencia(@RequestBody TransferenciaDto transferenciaDto) throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException{
          
          //Validar transferencia
          transferenciaValidator.validarTransferencia(transferenciaDto);
          //realizar la transferencia.
          transferenciaService.validacionTransferencia(transferenciaDto);

          return ResponseEntity.ok(new ApiResponse("EXITOSA", "Transferencia realizada exitosamente"));
        }

  }
