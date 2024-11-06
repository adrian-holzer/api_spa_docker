package com.adri.api_spa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
//Le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicación
@EnableWebSecurity
//Indicamos que se activa la seguridad web en nuestra aplicación y además esta será una clase la cual contendrá toda la configuración referente a la seguridad
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;






    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //Este bean va a encargarse de verificar la información de los usuarios que se loguearán en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este bean incorporará el filtro de seguridad de json web token que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }




    //Vamos a crear un bean el cual va a establecer una cadena de filtros de seguridad en nuestra aplicación.
    // Y es aquí donde determinaremos los permisos segun los roles de usuarios para acceder a nuestra aplicación
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling() //Permitimos el manejo de excepciones
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //Nos establece un punto de entrada personalizado de autenticación para el manejo de autenticaciones no autorizadas
                .and()
                .sessionManagement() //Permite la gestión de sessiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests() //Toda petición http debe ser autorizada

                // *** DATOS DE PRUEBA ** //


                // AUTH

                .requestMatchers("/api/auth/registerCliente").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/userLogueado").hasAnyAuthority("ADMIN" , "PROFESIONAL","CLIENTE")
                .requestMatchers("/api/auth/registerAdmin").hasAuthority("ADMIN")
                .requestMatchers("/api/auth/registerProf").hasAuthority("ADMIN")
                .requestMatchers("/api/auth/registerSecretario").hasAuthority("ADMIN")



                // CONSULTAS
                .requestMatchers(HttpMethod.POST, "/api/consulta/crear").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/consulta/{idConsulta}/respuestas/crear").hasAnyAuthority("ADMIN" , "PROFESIONAL")
                .requestMatchers(HttpMethod.GET, "/api/consulta/listar").hasAnyAuthority("ADMIN" , "PROFESIONAL")
                .requestMatchers(HttpMethod.GET, "/api/consulta/").hasAnyAuthority("ADMIN" , "PROFESIONAL")
                .requestMatchers(HttpMethod.GET, "/api/consulta/{idConsulta}").hasAnyAuthority("ADMIN" , "PROFESIONAL")

                //COMENTARIOS
                .requestMatchers(HttpMethod.POST,"/api/comentario/crear").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/comentario/listar").permitAll()

                // SERVICIOS

                .requestMatchers(HttpMethod.GET,"/api/servicio/listar").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/servicio/{idServicio}").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/servicio/categorias").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/servicio/crear").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")

                // TURNOS
                .requestMatchers(HttpMethod.GET, "/api/turno/disponibles").hasAnyAuthority("ADMIN" , "PROFESIONAL","CLIENTE","SECRETARIO")

                .requestMatchers(HttpMethod.GET,"/api/turno/listar").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.POST, "/api/turno/crear").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.POST, "/api/turno/asignar").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/turno/por-fecha").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.GET,"/api/turno/misTurnos").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.DELETE,"/api/turno/cancelar/{idTurno}").hasAnyAuthority("ADMIN" , "PROFESIONAL","CLIENTE","SECRETARIO")
                .requestMatchers(HttpMethod.GET,"/api/turno/cliente/{idCliente}").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.POST,"/api/turno/finalizarTurno/{idTurno}").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO","CLIENTE")
                .requestMatchers(HttpMethod.GET,"api/turno/asignados/por-profesional").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.GET,"api/turno/profesional/servicios").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")




                // EMPLEOS

                .requestMatchers(HttpMethod.POST,"/api/empleo/crear").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.GET,"/api/empleo/listar").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/empleo/{id}/postulaciones").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.POST,"/api/postulacion/upload").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/postulacion/download/{id}").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")


                // PAGOS
                .requestMatchers(HttpMethod.POST,"/api/pago/procesar").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.POST,"/api/pago/enviar-factura").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.GET,"/api/pago/cliente/{idCliente}").hasAnyAuthority("SECRETARIO","ADMIN","PROFESIONAL")
                .requestMatchers(HttpMethod.GET,"/api/pago/ingresos").hasAnyAuthority("SECRETARIO","ADMIN","PROFESIONAL")


                // PROFESIONALES

                .requestMatchers(HttpMethod.GET,"/api/profesional/listar").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")


                // CLIENTES

                .requestMatchers(HttpMethod.GET,"/api/cliente/listar").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO")
                .requestMatchers(HttpMethod.GET,"/api/cliente/clienteLogueado").hasAnyAuthority("ADMIN" , "PROFESIONAL","SECRETARIO","CLIENTE")
                .requestMatchers(HttpMethod.PUT, "/api/cliente/modificar").hasAuthority("CLIENTE")


                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("capacitor://localhost", "http://localhost", "https://localhost", "http://localhost:5173","https://frontspa.netlify.app/","https://spasentirsebien.netlify.app/","https://spa-proyect.vercel.app/")); // Origen permitido List.of("http://localhost:5173","https://frontspa.netlify.app/")
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization"); // Permite exponer el header Authorization

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
