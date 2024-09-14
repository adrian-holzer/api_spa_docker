package com.adri.api_spa;

import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.*;
import com.adri.api_spa.services.ProfesionalService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {

    private final IRolesRepository roleRepository;
    private final IUsuariosRepository usuariosRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    IHorarioLaboralRepository horarioLaboralRepository;
    @Autowired
    ICategoriaServicioRepository categoriaServicioRepository;
    @Autowired
    IServicioRepository servicioRepository;

    @Autowired
    ProfesionalService profesionalService;


    public DataInitializer(IRolesRepository roleRepository,IUsuariosRepository usuariosRepository,PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuariosRepository= usuariosRepository;
        this.passwordEncoder= passwordEncoder;
    }


    //crear roles ADMIN, CLIENTE, PROFESIONAL

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("CLIENTE").isEmpty()) {
            Roles userRole = new Roles();
            userRole.setName("CLIENTE");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Roles adminRole = new Roles();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("PROFESIONAL").isEmpty()) {
            Roles adminRole = new Roles();
            adminRole.setName("PROFESIONAL");
            roleRepository.save(adminRole);
        }




// Crear usuario admin

        Usuarios usuarios = new Usuarios();
       ;
        usuarios.setUsername("user_admin");
        usuarios.setPassword(passwordEncoder.encode("adminpass"));
        usuarios.setNombre("jorge");
        usuarios.setApellido("perez");
        usuarios.setDni("15555555");
        usuarios.setEmail("admin@email.com");


        // Obtener los roles del repositorio
        Roles adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
       // Roles profesionalRole = roleRepository.findByName("PROFESIONAL").orElseThrow(() -> new RuntimeException("Rol PROFESIONAL no encontrado"));

        // Asignar ambos roles al usuario
        usuarios.setRoles(Arrays.asList(adminRole));


        usuariosRepository.save(usuarios);







// Crear horarios Laborales

        // Insertar horarios de lunes a sábado (mañana y tarde)
        List<HorarioLaboral> horariosLaborales = new ArrayList<>();
        horariosLaborales.add(crearHorario(DiaSemana.LUNES, TurnoLaboral.MAÑANA, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.LUNES, TurnoLaboral.TARDE, LocalTime.of(17, 0), LocalTime.of(21, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.MARTES, TurnoLaboral.MAÑANA, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.MARTES, TurnoLaboral.TARDE, LocalTime.of(17, 0), LocalTime.of(21, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.MIÉRCOLES, TurnoLaboral.MAÑANA, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.MIÉRCOLES, TurnoLaboral.TARDE, LocalTime.of(17, 0), LocalTime.of(21, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.JUEVES, TurnoLaboral.MAÑANA, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.JUEVES, TurnoLaboral.TARDE, LocalTime.of(17, 0), LocalTime.of(21, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.VIERNES, TurnoLaboral.MAÑANA, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.VIERNES, TurnoLaboral.TARDE, LocalTime.of(17, 0), LocalTime.of(21, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.SÁBADO, TurnoLaboral.MAÑANA, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        horariosLaborales.add(crearHorario(DiaSemana.SÁBADO, TurnoLaboral.TARDE, LocalTime.of(17, 0), LocalTime.of(21, 0)));

        horarioLaboralRepository.saveAll(horariosLaborales);


        // Crear categorias de Servicios y sus servicios asociados






// Crear profesional


        Usuarios usuariosProf = new Usuarios();
        Profesional nuevoProf = new Profesional();
        usuariosProf.setUsername("prof1");
        usuariosProf.setPassword(passwordEncoder.encode("pass"));
        usuariosProf.setNombre("carla");
        usuariosProf.setApellido("gomez");
        usuariosProf.setDni("30000111");
        usuariosProf.setEmail("prof1@email.com");

        usuariosProf.setProfesional(nuevoProf);
        nuevoProf.setUsuario(usuariosProf);


        // Obtener los roles del repositorio
        Roles profRole = roleRepository.findByName("PROFESIONAL").orElseThrow(() -> new RuntimeException("Rol PROFESIONAL no encontrado"));
        // Roles profesionalRole = roleRepository.findByName("PROFESIONAL").orElseThrow(() -> new RuntimeException("Rol PROFESIONAL no encontrado"));

        // Asignar ambos roles al usuario
        usuariosProf.setRoles(Arrays.asList(profRole));


        profesionalService.asignarHorarios(nuevoProf);

        usuariosRepository.save(usuariosProf);





// Crear categorías

        CategoriaServicio categoriaMasajes = new CategoriaServicio();
        categoriaMasajes.setNombre("Masajes");

        CategoriaServicio categoriaBelleza = new CategoriaServicio();
        categoriaBelleza.setNombre("Belleza");



        CategoriaServicio tratamientosFaciales = new CategoriaServicio();
        tratamientosFaciales.setNombre("Tratamientos Faciales");

        CategoriaServicio tratamientosCorporales = new CategoriaServicio();
        tratamientosCorporales.setNombre("Tratamientos Corporales");




        // Guardar las categorías
        categoriaServicioRepository.saveAll(Arrays.asList(categoriaMasajes, categoriaBelleza,tratamientosFaciales,tratamientosCorporales));

        // Crear servicios para la categoría Masajes

        Servicio servicio1 = new Servicio();
        servicio1.setDetallesServicio("Anti-stress");
        servicio1.setDuracionMinutos(60);
        servicio1.setPrecio(100.0);
        servicio1.setCategoria(categoriaMasajes); // Asignar categoría

        Servicio servicio2 = new Servicio();
        servicio2.setDetallesServicio("Descontracturantes");
        servicio2.setDuracionMinutos(60);
        servicio2.setPrecio(100.0);
        servicio2.setCategoria(categoriaMasajes); // Asignar categoría


        Servicio servicio3 = new Servicio();
        servicio3.setDetallesServicio("Masajes con piedras calientes");
        servicio3.setDuracionMinutos(60);
        servicio3.setPrecio(100.0);
        servicio3.setCategoria(categoriaMasajes); // Asignar categoría




        Servicio servicio4 = new Servicio();
        servicio4.setDetallesServicio("Circulatorios");
        servicio4.setDuracionMinutos(60);
        servicio4.setPrecio(100.0);
        servicio4.setCategoria(categoriaMasajes); // Asignar categoría


        // Crear servicios para la categoría Belleza


        Servicio servicio5 = new Servicio();
        servicio5.setDetallesServicio("Lifting de pestaña ");
        servicio5.setDuracionMinutos(60);
        servicio5.setPrecio(100.0);
        servicio5.setCategoria(categoriaBelleza); // Asignar categoría


        Servicio servicio6 = new Servicio();
        servicio6.setDetallesServicio("Depilación facial ");
        servicio6.setDuracionMinutos(60);
        servicio6.setPrecio(100.0);
        servicio6.setCategoria(categoriaBelleza); // Asignar categoría


        Servicio servicio7 = new Servicio();
        servicio7.setDetallesServicio("Belleza de manos y pies ");
        servicio7.setDuracionMinutos(60);
        servicio7.setPrecio(100.0);
        servicio7.setCategoria(categoriaBelleza); // Asignar categoría






        // Crear servicios para la categoría Tratamientos Faciales


        Servicio servicio8 = new Servicio();
        servicio8.setDetallesServicio("Punta de Diamante: Microexfoliación. ");
        servicio8.setDuracionMinutos(60);
        servicio8.setPrecio(100.0);
        servicio8.setCategoria(tratamientosFaciales); // Asignar categoría


        Servicio servicio9= new Servicio();
        servicio9.setDetallesServicio("Limpieza profunda + Hidratación ");
        servicio9.setDuracionMinutos(60);
        servicio9.setPrecio(100.0);
        servicio9.setCategoria(tratamientosFaciales); // Asignar categoría


        Servicio servicio10 = new Servicio();
        servicio10.setDetallesServicio("Crio frecuencia facial:  produce el “SHOCK TERMICO” \n" +
                "logrando resultados instantáneos de efecto lifting. ");
        servicio10.setDuracionMinutos(60);
        servicio10.setPrecio(100.0);
        servicio10.setCategoria(tratamientosFaciales); // Asignar categoría




        // Crear servicios para la categoría Tratamientos Corporales


        Servicio servicio11 = new Servicio();
        servicio11.setDetallesServicio("VelaSlim: Reducción de la circunferencia corporal y la \n" +
                "celulitis.  ");
        servicio11.setDuracionMinutos(60);
        servicio11.setPrecio(100.0);
        servicio11.setCategoria(tratamientosCorporales); // Asignar categoría


        Servicio servicio12= new Servicio();
        servicio12.setDetallesServicio("DermoHealth:  moviliza los distintos tejidos de la piel y \n" +
                "estimula la microcirculación, generando un drenaje \n" +
                "linfático.  ");
        servicio12.setDuracionMinutos(60);
        servicio12.setPrecio(100.0);
        servicio12.setCategoria(tratamientosCorporales); // Asignar categoría


        Servicio servicio13 = new Servicio();
        servicio13.setDetallesServicio("Criofrecuencia: produce un efecto de lifting instantáneo.  ");
        servicio13.setDuracionMinutos(60);
        servicio13.setPrecio(100.0);
        servicio13.setCategoria(tratamientosCorporales); // Asignar categoría



        Servicio servicio14 = new Servicio();
        servicio14.setDetallesServicio("Ultracavitación:Técnica reductora. ");
        servicio14.setDuracionMinutos(60);
        servicio14.setPrecio(100.0);
        servicio14.setCategoria(tratamientosCorporales); // Asignar categoría



        // Guardar los servicios
        servicioRepository.saveAll(Arrays.asList(servicio1, servicio2, servicio3, servicio4,servicio5,servicio6,servicio7,servicio8,servicio9,servicio10,
                servicio11,servicio12,servicio13,servicio14));

        System.out.println("Datos inicializados correctamente.");







    }















    private HorarioLaboral crearHorario(DiaSemana dia, TurnoLaboral turnoLaboral, LocalTime horaInicio, LocalTime horaFin) {
        HorarioLaboral horario = new HorarioLaboral();
        horario.setDiaSemana(dia);
        horario.setTurnoLaboral(turnoLaboral);
        horario.setHoraInicio(horaInicio);
        horario.setHoraFin(horaFin);
        return horario;
    }







}