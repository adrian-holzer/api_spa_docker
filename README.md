

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


- Registro de Cliente

POST {{host}}/api/auth/register

Datos de prueba Ejemplo

En Body -> Raw :

{

"username" : "user1",
"password" : "pass" ,
"dni" : "40150100",
"email": "prueba@email.com",
"nombre" :  "juan",
"apellido": "perez"

}

///////////////////////////////////////////////////////////


- Login de Cliente 

POST- {{host}}/api/auth/login


Datos de prueba Ejemplo

En Body -> Raw -> JSON :

{

"username" : "user1",
"password" : "pass" ,

}

resultado -> { token , Baerer } -> con estos datos se prueba la autorizacion en el front



///////////////////////////////////////////////////////////
Registro de profesional (TIENE QUE ESTAR AUTENTICADO ALGUN USUARIO ADMIN )


{
"username" : "prof1",
"password" : "pass" ,
"dni" : "50059090",
"email": "prof1@email.com",
"nombre" :  "juan",
"apellido": "perez"

   }


POST - {{host}}/api/auth/registerProf




Login de Profesional(IGUAL QUE CON CLIENTE)


///////////////////////////////////////////////////////////

- Enviar una consulta -> puede realizarla cualquiera (todos pueden acceder a esa url)

POST - {{host}}/api/consulta/crear

Datos de prueba 

{

    "nombrePersona" : "juan gomez",
    "temaConsulta" : "servicio de masaje" ,
    "textoConsulta" : "quisiera consultar sobre el servicio...",
    "email": "email@gmail.com"

}

- Responder una Consulta -> solo la puede responder un profesional (tiene que estar autenticado)

-POST {{host}}/api/consulta/1/respuestas/crear (el numero es la consulta que estas respondiendo, verificar que este creada en la bd)


{

    "textoRespuesta" : "estoy respondiendo a la consulta"
}




/////////////////////////////////////////////////////////////////////

Operaciones con Consultas > (solo lo puede hacer un admin o un profesional)

-- Listar Todas las consultas 

GET / {{host}}/api/consulta/listar


-- Listar consulta por ID 


GET / {{host}}/api/consulta/1

-- Listar consultas contestadas

GET / {{host}}/api/consulta/listar?contestado=true



-- Listar consultas no contestadas

GET / {{host}}/api/consulta/listar?contestado=false


/////////////////////////////////////////////

Operaciones con Comentarios > (Lo puede hacer cualquiera)

-- Listado de comentarios

GET / {{host}}/api/comentario/listar

-- Crear un comentario 


POST / {{host}}/api/comentario/crear



 **** Completar los datos de application.properties ***** 