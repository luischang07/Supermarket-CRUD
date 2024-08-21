# CRUD Aplicación móvil 
El super mercado con razón social “Encuentras todo”, tiene una serie de clientes que pueden realizar en una misma compra producto(s) así como paquetes (conjunto de productos). Se desea que desarrolle una app para dispositivos móviles Android que permita:
1. Realizar el CRUD del catálogo de Productos
2.	Realizar el CRUD del catálogo de paquetes
3.	Procesar las ventas que se realicen.
4.	Emisión de reportes.

El gestor de base de datos que se utilizará es el SQLite. En las siguientes figuras se muestra la estructura de las tablas que debe crear en la base de datos Ventas. PRODUCTOS, PAQUETES, VENTAS.

![Productos](https://github.com/user-attachments/assets/9b9f2f5f-afff-4808-b747-30adb70f57b7)

![Paquetes](https://github.com/user-attachments/assets/330f755c-5f4d-4922-b3bb-d9c163347ac8)

![Folios](https://github.com/user-attachments/assets/0964c849-9c6a-4770-9bf1-ffd63c795292)

Desarrollar las opciones para realizar el CRUD del:
1.	catálogo de PRODUCTOS. el id puede ser auto incremental, el estatus cuando se crea es activo (‘A’), el borrado debe de ser lógico (estatus ponerle B).
2.	 catálogo de PAQUETES. Mientras no haya ventas registradas con id de un paquete se puede realizar cualquier cambio, el borrado es físico.
3.	Registro de Ventas. La venta está compuesta por uno o mas renglones de venta de producto o paquete, se debe poder surtir el total de lo pedido para que la venta se realice.

Desarrollar las siguientes consultas:
1. Los productos que no forman parte de paquetes y que no se han vendido. (IdProducto)
2. Los paquetes que requieren de mas de 3 productos y 10 unidades.
3. El importe de ventas por producto realizada de manera individual y en paquete.



