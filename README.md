# Ejercicio Spring Batch

Realizar un proceso batch que realice las siguientes operaciones:
- Obtener todos los ficheros CSV que se encuentren en una carpeta
- A partir de cada uno de los ficheros, leer su contenido y ejecutar las siguientes acciones:
	- En caso de que la palabra clave sea INSERT, que inserte el registro en base de datos si no existe el nombre de usuario
	- En caso de que la palabra clave sea UPDATE, que actualice el registro en base de datos si existe el nombre de usuario
	- En caso de que la palabra clave sea DELETE, que elimine el registro de base de datos si existe el nombre de usuario
- Una vez procesado el fichero, que lo elimine
 
El fichero CSV estará compuesto de la siguiente manera:
operacion,username,password,email
INSERT,admin,admin,admin@group.com
