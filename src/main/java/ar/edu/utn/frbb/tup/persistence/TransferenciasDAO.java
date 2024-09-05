package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Transferencia;

public interface TransferenciasDAO {
        public void agregarTransferencias(Transferencia transferencia) throws Exception;
        public List<Transferencia> obtenerListadoTransferencias() throws Exception;
}
