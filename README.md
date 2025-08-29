# 🚀 Prueba Técnica BTG Pactual

Este proyecto contiene la solución a la prueba técnica para el rol de **Ingeniero de Desarrollo Back End**, propuesta por **SETI**. El proyecto se divide en dos partes principales: la implementación de una **API REST** para la gestión de fondos y la resolución de consultas **SQL**.

## 🛠️ Tecnologías Utilizadas
- Java
- Maven
- Spring Boot
- MongoDB
- PostgreSQL
- Docker

## 📋 Requirimientos

- Java 17
- maven
- PostgreSQL
- MongoDB
- Docker

## 🐳 Levantar Contenedores necesarios

### ⚙️ Configuración y Ejecución

#### ⬇️ Clonar el repositorio:

>```shell
> git clone <URL_DEL_REPOSITORIO>
> cd <NOMBRE_DEL_REPOSITORIO>
>```

#### 🔧 Iniciar los servicios:

Ejecuta el siguiente comando en la raíz del proyecto para crear y arrancar los contenedores en segundo plano:

>```shell
> docker-compose up -d
>```

Este comando leerá el archivo **docker-compose.yml** y creará dos contenedores: uno para **PostgreSQL** y otro para **MongoDB**.

### 🧹 Eliminar contenedores y datos

En caso de que quieras detener y eliminar completamente el contenedor, junto con los volúmenes asociados (datos creados durante las pruebas), puedes usar:

>```shell
> docker-compose down --volumes --rmi all
>```

## 🧩 Parte 1: Fondos


## 🧩 Parte 2: Consultas SQL

Esta sección contiene la consulta **SQL** para la segunda parte del desafío, basándose en el esquema de la base de datos. A continuación, se muestra el diagrama diseñado para representar dicha estructura.

>```mermaid
>erDiagram
>   cliente {
>       int id PK
>       varchar nombre
>       varchar apellidos
>       varchar ciudad
>   }
>
>   producto {
>       int id PK
>       varchar nombre
>       varchar tipoProducto
>   }
>
>   sucursal {
>       int id PK
>       varchar nombre
>       varchar ciudad
>   }
>
>   inscripcion {
>       int idProducto PK, FK
>       int idCliente PK, FK
>   }
>
>   disponibilidad {
>       int idSucursal PK, FK
>       int idProducto PK, FK
>   }
>
>   visitan {
>       int idSucursal PK, FK
>       int idCliente PK, FK
>       date fechaVisita
>   }
>
>   cliente ||--o{ inscripcion : "tiene"
>   cliente ||--o{ visitan : "visita"
>   producto ||--o{ inscripcion : "es inscrito en"
>   producto ||--o{ disponibilidad : "esta disponible en"
>   sucursal ||--o{ disponibilidad : "ofrece"
>   sucursal ||--o{ visitan : "es visitada por"
>```

### 🐘️Solución SQL

> [!IMPORTANT]
> Es necesario ejecutar el script **parte-2-schema-data.sql** para realizar la creación y población de las tablas requeridas para esta sección.

A continuación, se presentan los comandos necesarios para ejecutar el script mencionado anteriormente desde la terminal. También es posible ejecutarlo utilizando un cliente de base de datos.

#### 🚫🐳 Ejecutar comandos sin Docker
>```shell
> psql -U <nombre_usuario> -d <nombre_bd> < <ruta_archivo>
>```

#### 🐳 Ejecutar comandos con Docker
>```shell
> docker exec -i <nombre_contenedor> psql -U <nombre_usuario> -d <nombre_bd> < <ruta_archivo>
>```


La siguiente consulta resuelve el problema de **obtener los nombres de los clientes que tienen inscrito algún producto disponible solo en las sucursales que visitan**.

>```sql
> SELECT DISTINCT C.nombre
> FROM cliente AS C
> JOIN inscripcion AS i ON c.id = i.idCliente
> JOIN disponibilidad AS d ON i.idProducto = d.idProducto
> JOIN visitan AS v ON v.idSucursal = d.idSucursal AND v.idCliente = c.id
> WHERE i.idProducto IN (
>         SELECT idProducto
>         FROM disponibilidad
>         GROUP BY idProducto
>         HAVING COUNT(idSucursal) = 1
>);
>```
