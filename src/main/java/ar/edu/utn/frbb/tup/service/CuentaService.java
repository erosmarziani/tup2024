package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.Dto.MovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.Dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.*;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    @Autowired
    private CuentaDaoImpl cuentaDao;

    @Autowired
    private ClienteDaoImpl clienteDao;

    @Autowired
    private MovimientosDaoImpl movimientoDao;

    
    public Cuenta altaCuenta(CuentaDto cuentaDto) throws ErrorArchivoException, ClienteServiceException, CuentaServiceException {

        Cuenta cuentaExistente = cuentaDao.obtenerCuentaPorId(Long.parseLong(cuentaDto.getIdCuenta()));
        if(cuentaExistente != null) {
            throw new CuentaServiceException("Ya existe una cuenta con el ID proporcionado");
        }
        Cuenta cuenta = new Cuenta(
            Long.parseLong(cuentaDto.getIdCuenta()),
             LocalDate.now(),
              Double.parseDouble(cuentaDto.getBalance()),
                TipoCuenta.valueOf(cuentaDto.getTipoCuenta()),
                Long.parseLong(cuentaDto.getDniTitular()),
                TipoMoneda.valueOf(cuentaDto.getMoneda()));
        
        Cliente clienteAsociado = clienteDao.obtenerClientePorDNI(cuenta.getTitular());
        if (clienteAsociado == null) {
            throw new ClienteServiceException("El cliente no ha sido encontrado en la base de datos");
        }
        
        cuentaDao.guardarCuenta(cuenta);
        return cuenta;

    }

    public Cuenta eliminarCuentasCliente(long idCliente) throws CuentaServiceException, ErrorArchivoException{
        Cuenta cuenta = cuentaDao.eliminarCuenta(idCliente);

        if (cuenta == null) {
            throw new CuentaServiceException("El cliente no tiene cuentas asociadas en la base de datos.");

        }        
        return cuenta;
    }

    public List<Cuenta> obtenerCuentasPorIdCliente(Long idCliente)
            throws ErrorArchivoException, ClienteServiceException {
        Cliente cliente = clienteDao.obtenerClientePorDNI(idCliente);
        if (cliente == null) {
            throw new ClienteServiceException("El cliente no ha sido encontrado en la base de datos");
        }

        List<Cuenta> cuentas = cuentaDao.obtenerCuentasDelCliente(idCliente);
        return cuentas;
    }

    public List<Cuenta> obtenerCuentas() throws ErrorArchivoException, CuentaServiceException {
        List<Cuenta> cuentas = cuentaDao.obtenerCuentas();
        if (cuentas.isEmpty()) {
            throw new CuentaServiceException("No se han encontrado cuentas en la base de datos");
        }
        return cuentas;
    }

    public Cuenta actualizarCuenta(CuentaDto cuentaDto) throws ErrorArchivoException, CuentaServiceException{
        Cuenta cuentaModificada = new Cuenta(cuentaDto);
        Cuenta cuentaExistente = cuentaDao.actualizarCuenta(cuentaModificada);
        if (cuentaExistente == null)  {
            throw new CuentaServiceException("La cuenta no ha sido encontrada en la base de datos");
        }
        return cuentaModificada;
    }

    public MovimientosResponseDto obtenerMovimientos(long idCuenta) throws CuentaServiceException, ErrorArchivoException{
        Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);

        if (cuenta == null) {
            throw new CuentaServiceException("Cuenta no encontrada");   
        }

        List<Movimiento> movimientos = movimientoDao.obtenerMovimientoPorCuenta(idCuenta);

        List<MovimientoDto> transacciones = movimientos.stream().map(movimiento -> convertirMovimientoAMovimientoDto(movimiento)).collect(Collectors.toList());

        MovimientosResponseDto response = new MovimientosResponseDto();
        response.setNumeroCuenta(String.valueOf(cuenta.getNumeroCuenta()));
        response.setTransacciones(transacciones);

        return response;
    }

    private MovimientoDto convertirMovimientoAMovimientoDto(Movimiento movimiento){
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setFechaOperacion(movimiento.getFechaOperacion().toString());
        movimientoDto.setTipo(movimiento.getTipo().toString());
        movimientoDto.setDescripcion(movimiento.getDescripcion());
        movimientoDto.setMonto(String.valueOf(movimiento.getMonto()));
        return movimientoDto;
    }
}
