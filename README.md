# Automatizacion sitio de prueba 

## Descripción
Se genera automatizacion de sitio de prueba realizando multiples acciones dentro del sitio para validar su funcionamiento.

## Consideraciónes
Para que este proyecto se pueda ejecutar sin problemas necesitamos tener los siguientes puntos en consideraciones:
    - Tener instalado Java y Maven 
    - En el Scenario 1 que seria con usuario registrado si el correo no se cambia entonces el flujo no podra realizarse ya que por validacion de ambiente un correo solo puede estar asociado a una cuenta
        - Para relizar esto es necesario dirigirnos al archivo **`Main.feature`** que se encuentra en la ruta **`src/test/resources/features/Main.feature`** y modificar el correo que se encuentra en el primer escenario

## Construccion y pruebas
Para construir el proyecto y ejecutar las pruebas, habrá una terminal directamente en la carpeta del proyecto y luego ejecute este comando:

```bash
mvn clean install -DskipTests
```
Este comando limpiara el directorio temporal e instalara todas las dependencias necesarias

```bash
mvn test -Dcucumber.options="--tags @Full"
```
## Detalle de flujos realizados
En esta seccion se veran los detalles de los flujos realizados a groso modo

    - 1er flujo: Se realiza con una creacion de usuario

        - Añadir al carro de compras un Ipod Classic
        - Añadir al carro de compras un iMac
        - Proceder a realizar la compra
        - Crear una cuenta
        - Continuar con la compra y llegar a la orden completa
        - Visitar el historial de ordenes y validar resumen de orden
        - Cerrar sesión

    - 2do flujo: Se realiza el mismo flujo anterior pero con un usuario ya con credenciales que se usa el usuario que se creo anteriormente

        - Añadir al carro de compras un Ipod Classic
        - Añadir al carro de compras un iMac
        - Proceder a realizar la compra
        - Continuar con la compra y llegar a la orden completa
        - Visitar el historial de ordenes y validar resumen de orden
        - Cerrar sesión

    - 3er flujo: Comparacion de 2 productos

        - Se busca el producto Apple Cinema 30 y se agrega a la seccion de comparación 
        - Se busca el producto Samsung SyncMaster 941BW y se agrega a la seccion de comparación
        - Se evidencia que esten en la sección comparación los productos anteriormente mencionados

    - 4to flujo: Añadir dos PC HP LP3065

        - Se busca el producto HP LP3065
        - Se selecciona la fecha de entrega tomando la fecha de hoy y sumandole 1 dia
        - Se valida que la descripción corresponda a 16GB de memoria

