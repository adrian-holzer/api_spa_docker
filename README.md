


- Agregar credenciales a la application.properties 


Request 

Registrar un Cliente

POST http://localhost:8080/api/auth/register

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


Login de Cliente 

POST- http://localhost:8080/api/auth/login


Datos de prueba Ejemplo

En Body -> Raw -> JSON :

{
"username" : "user1",
"password" : "pass" ,

}

resultado -> { token , Baerer } -> con estos datos se prueba la autorizacion en el front