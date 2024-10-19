

- REGISTRO Y LOGIN DE CLIENTES Y PROFESIONALES
- VALIDACIONES DE DATOS DE ENTRADA
- ENVIO DE CONSULTAS POR PARTE DEL PUBLICO EN GENERAL Y RESPONDER AL EMAIL SIENDO UN USUARIO PROFESIONAL
           
- Usar postman para pruebas de la Api
- Agregar credenciales a la application.properties 
- Comprobar que este configurado el correo de prueba
- Al iniciar la app se crean los roles ADMIN, PROFESIONAL, CLIENTE y un usuario con rol ADMIN Y PROFESIONAL para probar
la respuesta de consultas y el registro de nuevos profesionales.

Datos del usuario que se crea para hacer el login (ADMIN) :

POST- {{host}}/api/auth/login

{
"username" : "user_admin",
"password" : "adminpass"
}

Al loguearse se genera un token
Configurar en Authorization -> Bearer Token y agregar el token -> Esto se utiliza para cada autenticacion en las
rutas que lo requieran por ejemplo crear un nuevo profesional o responder a consultas. 


///////////////////////////////////////////////////////////




# REGISTROS


{{host}} -> Es la ruta de la api (en local http://localhost:8080)
## REGISTRO DE CLIENTE (lo puede hacer cualquier persona)

POST - {{host}}/api/auth/registerCliente

en Body mando los datos formato json
{
"username" : "user2",
"password" : "pass" ,
"dni" : "30000036",
"email": "prueba3385@email.com",
"nombre" :  "juan",
"telefono": "1442232323",
"domicilio" : "Av falsa 123",
"apellido": "perez"

}


## REGISTRO DE PROFESIONAL (lo puede hacer solo el ADMIN hay uno creado  por defecto
 username : user_admin
 password : adminpass
)

POST - {{host}}/api/auth/registerProf

paso en body 

{
"username" : "prof2",
"password" : "pass" ,
"dni" : "40031137",
"email": "p1@email.com",
"nombre" :  "juan",
"apellido": "perez"
}


## LOGIN (lo puede hacer cualquier persona cuando accede )


POST - {{host}}/api/auth/login
 
{
    "username" : "user2",
    "password" : "pass"
}

## OBTENER EL USUARIO LOGUEADO (tenes que estar autenticado)

GET - {{host}}/api/auth/userLogueado


## COMENTARIOS

Listar comentarios (lo puede hacer cualquiera)

GET - {{host}}/api/comentario/listar

Crear un comentario (lo puede hacer cualquiera)

 POST - {{host}}/api/comentario/crear



## SERVICIOS

Listar todos los servicios (puede acceder cualquiera)

GET - {{host}}/api/servicio/listar


Buscar servicio por id (lo puede hacer cualquiera) 

GET - {{host}}/api/servicio/10


Listar las caTgorias de servicios (puede acceder cualquiera)


GET - {{host}}/api/servicio/categorias

Filtrar servicios por categoria (puede acceder cualquiera )

GET - {{host}}/api/servicio/listar?categoria=masajes


## CONSULTAS - RESPUESTAS

 Crear una consulta (lo puede hacer cualquiera)

POST - {{host}}/api/consulta/crear 

EN EL BODY
{

    "nombrePersona" : "juan gomez",
    "temaConsulta" : "servicio de masaje" ,
    "textoConsulta" : "quisiera consultar sobre el servicio...",
    "email": "prueba.metod.2@gmail.com"

}

Responder una consulta (tenes que tener el rol PROFESIONAL)

POST- {{host}}/api/consulta/1/respuestas/crear


{

    "textoRespuesta" : "estoy respondiendo a la consulta 1"
}


listar todas las consultas realizadas (tenes que tener el rol PROFESIONAL)

GET- {{host}}/api/consulta/listar 

Listar consulta por id  (tenes que tener el rol PROFESIONAL)

GET-  {{host}}/api/consulta/1



Consultas contestadas  (tenes que tener el rol PROFESIONAL)


GET- {{host}}/api/consulta/listar?contestado=true



Consultas Sin contestar (tenes que tener el rol PROFESIONAL)


GET- {{host}}/api/consulta/listar?contestado=false





## GESTION DE TURNOS


### (Alta de turno , listado y cancelar estan en el front de prueba) 

Alta de turnos (cliente logueado > pasar token de cliente)


- POST {{host}}/api/turno/crear

En el body 

{
"id_cliente": "1",
"id_servicio": "9",
"fecha": "2024-09-17",
"horaInicio": "08:00",
"horaFin": "09:00"
} 
Datos de prueba (verificar que existan)



Cancelar un turno  (cliente logueado > pasar token de cliente)

DELETE - {{host}}/api/turno/cancelar/1 (CAMBIA EL ESTADO A CANCELADO)



Listado de MIS TURNOS (cliente logueado > pasar token de cliente)

GET - {{host}}/api/turno/misTurnos



Mostrar todos los turnos (SOLO PARA EL PROFESIONAL)

GET -  {{host}}/api/turno/listar


Mostrar turnos de un cliente en particular  (SOLO PARA EL PROFESIONAL)

GET -  {{host}}/api/turno/cliente/1


Mostrar turnos por fecha  (SOLO PARA EL PROFESIONAL)


GET -  {{host}}/api/turno/por-fecha?fecha=2024-09-26



Mostrar listado de turnos con ESTADO ASIGNADO  (SOLO PARA EL PROFESIONAL)


GET - {{host}}/api/turno/listar?estado=asignado




Mostrar listado de turnos con ESTADO CANCELADO  (SOLO PARA EL PROFESIONAL)


GET - {{host}}/api/turno/listar?estado=cancelado


Mostrar listado de turnos con ESTADO FINALIZADO  (SOLO PARA EL PROFESIONAL)


GET - {{host}}/api/turno/listar?estado=finalizado


 **** Completar los datos de application.properties ***** 
 

## Empleo y Postulaciones

Crear un EMPLEO

POST - {{host}}/api/empleo/crear (solo puede el PROFESIONAL)

{

    "titulo" : "Masajista",
    "descripcion": "Lorem ipsum dolor sit amet consectetur, adipiscing elit praesent fringilla. Vestibulum ante non per hac metus consequat justo conubia nisl id phasellus gravida, vel nec bibendum natoque integer sollicitudin dui erat nostra primis nam. Per nulla ve"

}


Postularse a un empleo (lo puede hacer cualquiera)

POST- {{host}}/api/postulacion/upload

Los datos se mandan en > body > seleccionar form-data > agregar un atributo cv que es de tipo archivo (cambiar de text a file)
Otro atributo id_empleo = 1 (el primer empleo creado)


Listado de empleos (puede acceder cualquiera es lo que se ve en la pagina)

GET - {{host}}/api/empleo/listar


Listado de empleos activos

GET- {{host}}/api/empleo/listar?estado=activo

Listar Postulaciones (solo PROFESIONALES)

GET - {{host}}/api/empleo/1/postulaciones >El 1 es el id del empleo



Descargar el cv de la postulacion  (solo PROFESIONALES)

GET - {{host}}/api/postulacion/download/1  > el 1 es de la postulacion






# ******** MODIFICACIONES *********
`

Alta de Turno (Horarios Disponibles) >>> Solo para Admin , Profesional, Secretario

POST {{host}}/api/turno/crear

*** Utilizo la ruta de listar profesionales para cargar los prof y seleccionar uno

*** Verificar que no se pueda seleccionar fechas hacia atras en el tiempo

{
"idProfesional": 3,
"fecha": "2024-10-26",
"horaInicio": "08:00"
}



Asignar un Turno (El cliente selecciona el dia y el horario del turno que desea) >> Solo accede el Cliente

*** Selecciona fecha 


*** Utilizar la ruta de turnos disponibles segun la fecha seleccionada
para cargar los horarios (solo hora de inicio)>> la hora final se calcula segun los servicios cargados


GET {{host}}/api/turno/disponibles?fecha=2024-10-26   >> Para CLIENTES , PROFESIONAL, ADMIN, SECRETARIO



*** Utilizar la ruta para listar servicios para cargar y poder seleccionar


POST {{host}}/api/turno/asignar


{
"idTurno": 1,
"servicioIds": [1, 2]
}
(Desde el front se controla el poder eliminar un servicio seleccionado para agregar otro etc)



Procesar Pago >>> Solo accede el Cliente (Un boton que permita ver el form de pago, se completa el mismo
y con esta ruta se realiza el pago enviando los datos)


*** Utilizar la ruta MIS TURNOS >> para seleccionar el turno a pagar

POST {{host}}/api/pago/procesar


{
"turnoId": 1,
"numTarjeta": "12345678901234567890",
"nombreTitular": "John Doe",
"vencimiento": "12/25",
"codSeguridad": "123",
"metodoPago": "CREDITO"
}


(Validar desde el front cada dato que se ingresa en los input ) >> Metodo de pago puede ser CREDITO O DEBITO


Enviar Factura por email >>> Accede el cliente >> Esta ruta se utiliza despues de realizar el pago , si  es exitoso , el front genera la factura o comprobante
en formato pdf y se envia por esta ruta que se encarga de enviar al email del cliente logueado (Que esta realizando el proceso)


POST {{host}}/api/pago/enviar-factura

*** Envio en el formulario el atributo "factura" con el pdf adjunto


### ************ Otras rutas Rutas necesarias para realizar todo el proceso de pago ***************



Listado de Profesionales (Para que el admin , profesional o secretario realice el alta del horario se necesita el listado de
profesionales )


GET {{host}}/api/profesional/listar  >> Para admin, profesional y secretario



Listado de todos los turnos disponibles (Accede el Cliente) >> Se utiliza para poder seleccionar el turno que desea


GET {{host}}/api/turno/disponibles



Buscar turno por id dentro de MIS TURNOS (CLIENTE) (esto srive para el momento de realizar un pago y buscar un turno determinado asignado al cliente logueado)


GET {{host}}/api/turno/misTurnos?idTurno=1


Buscar todos MIS TURNOS (CLIENTE) (obtengo todos los turnos del Cliente logueado >> por ej para saber que turnos asignados no estan pagados)



GET {{host}}/api/turno/misTurnos`


Registrar Secretario (lO PUEDE HACER EL ADMIN) 

***** Admin por defecto ********

{

"username" : "user_admin",
"password" : "adminpass"
}

****************************


POST {{host}}/api/auth/registerSecretario


{
"username" : "sec1",
"password" : "pass" ,
"dni" : "35525569",
"email": "sec1@email.com",
"nombre" :  "Maria",
"apellido": "Martinez"
}