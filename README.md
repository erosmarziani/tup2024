Proyecto Final Laboratorio de Computacion III.
Marziani Eros

ENDPOINTS CLIENTE
POST crearCliente: Crea un cliente y lo almacena en la base de datos.
URL:"/cliente"

GET obtenerCliente: Busca y devuelve un cliente especifico.
URL:"/cliente/{dni}"

GET getClientes: Devuelve la totalidad de los clientes almacenados en la base de datos.
URL:"/clientes"

DELETE deleteCliente: Elimina un cliente especifico.
URL:"/cliente/{dni}"

ENDPOINTS CUENTA
POST agregarCuenta: Agrega una cuenta vinculada con un cliente ya registrado.
URL:"/cuentas"

GET getCuentas: Devuelve la totalidad de las cuentas almacenadas en la base de datos
URL: "/cuentas"

GET getCuentaPorIdCliente: Devuelve las cuentas vinculadas con un cliente en particular.
URL: "/cuentas/{idCliente}

GET obtenerTransacciones: Devuelve los movimientos por cuenta
URL: "/cuentas/movimientos/{idCuenta}

ENDPOINT TRANSFERENCIAS
POST transferencia: Realiza una transferencia entre dos cuentas
URL: "/transferencia/"
