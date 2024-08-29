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
                .csrf().disable()
                .exceptionHandling() //Permitimos el manejo de excepciones
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //Nos establece un punto de entrada personalizado de autenticación para el manejo de autenticaciones no autorizadas
                .and()
                .sessionManagement() //Permite la gestión de sessiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests() //Toda petición http debe ser autorizada

                // *** DATOS DE PRUEBA ** //

                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/registerAdmin").hasAuthority("ADMIN")
                .requestMatchers("/api/auth/registerProf").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/consulta/crear").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/consulta/{idConsulta}/respuestas/crear").hasAnyAuthority("ADMIN" , "PROFESIONAL")
                .requestMatchers(HttpMethod.POST, "/api/turno/crear").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.GET,"/api/turno/listar").hasAnyAuthority("ADMIN" , "PROFESIONAL")
                .requestMatchers(HttpMethod.GET,"/api/turno/listarId/**").hasAnyAuthority("ADMIN" , "PROFESIONAL")
                .requestMatchers(HttpMethod.DELETE,"/api/turno/eliminar/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/turno/actualizar").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
