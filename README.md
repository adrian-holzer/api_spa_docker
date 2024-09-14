

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