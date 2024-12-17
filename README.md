Proyecto Final Laboratorio de Computacion III.
Marziani Eros

Clientes:
    POST /cliente: Crear cliente.
    GET /cliente/{dni}: Obtener cliente por DNI.
    GET /clientes: Obtener todos los clientes.
    DELETE /cliente/{dni}: Eliminar cliente.

Cuentas:
    POST /cuentas: Crear cuenta.
    GET /cuentas: Obtener todas las cuentas.
    GET /cuentas/{idCliente}: Obtener cuentas de un cliente.
    GET /cuentas/movimientos/{idCuenta}: Obtener movimientos de una cuenta.

Transferencias:
    POST /transferencia: Realizar transferencia entre cuentas.