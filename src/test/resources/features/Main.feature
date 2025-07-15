@Full
Feature: Main Feature

    Scenario: Flujo de compra con registro de usuario
        Given entro a la url de la tienda
        When busco el producto "iPod Classic" y lo agrego al carrito
        When busco el producto "iMac" y lo agrego al carrito
        And verifico que el carrito tenga "ipod classic" y "iMac"
        Then procedo al checkout
        Then selecciono la opcion de registarme como usuario
        And me registro como usuario con los siguientes datos:
            | Nombre  | Apellido | Email                | Telefono | Direccion | Ciudad | CodigoPostal | Pais | Region |
            | Juan    | Perez    | previredPrueba157@gmail.com     | 123456789 | Calle Falsa 123 | Santiago | 12345 | Chile | Region Metropolitana |
        And ingreso mi contrasena y la confirmo "contrasena123"
        Then acepto los terminos y condiciones
        Then confirmo el registro
        Then continuo con los datos de despacho ingresados en cuenta
        And valido que el despacho y costo sea Flat Shipping Rate - $5.00 y continuo
        Then acepto los terminos y condiciones de compra y continuo a confirmar orden
        And confirmo la orden de compra
        Then valido que el mensaje de confirmacion sea Your order has been placed!
        And voy al apartado de My Account y entro en Order History
        Then valido que el pedido este en estado Pending
        And valido los datos de pago vs los ingresados en mi cuenta
        Then cierro la sesion
    
    Scenario: Flujo de compra con usuario registrado
        Given entro a la url de la tienda
        When busco el producto "iPod Classic" y lo agrego al carrito
        When busco el producto "iMac" y lo agrego al carrito
        And verifico que el carrito tenga "ipod classic" y "iMac"
        Then procedo al checkout
        And ingreso mis credenciales de usuario desde el archivo properties
        And continuo con los datos de pago ingresados en cuenta
        Then continuo con los datos de despacho ingresados en cuenta
        And valido que el despacho y costo sea Flat Shipping Rate - $5.00 y continuo
        Then acepto los terminos y condiciones de compra y continuo a confirmar orden
        And confirmo la orden de compra
        Then valido que el mensaje de confirmacion sea Your order has been placed!
        And voy al apartado de My Account y entro en Order History
        Then valido que el pedido este en estado Pending
        And valido los datos de pago vs los ingresados en mi cuenta
        Then cierro la sesion

    # # Esta seccion corresponde a los puntos extras
    Scenario: Comparacion de productos
         Given entro a la url de la tienda 
         When busco el producto 'Apple Cinema 30"' y lo agrego a comparacion
         When busco el producto 'Samsung SyncMaster 941BW' y lo agrego a comparacion
         Then entro al apartado de Product Comparison
         Then valido que los productos comparados sean Apple Cinema y Samsung SyncMaster 941BW

    Scenario: Agregando 2 productos y validaciones 
        Given entro a la url de la tienda
        When busco el producto "HP LP3065" y lo agrego al carrito
        Then selecciono el delivery date para manana
        And coloco la cantidad 2 y lo agrego al carrito
        Then valido que la memoria del equipo sea 16GB
        Then ingreso a la seccion reviews e ingreso un mensaje "incorrecto"
        Then ingreso a la seccion reviews e ingreso un mensaje "correcto"
