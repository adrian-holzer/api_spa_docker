package com.adri.api_spa.services;

import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TurnoService {

    @Autowired
    ITurnoRepository turnoRepository;

    @Autowired
    UsuarioService usuarioService;

    Usuarios usuarioLoggeado;


    @Autowired
    IClienteRepository clienteRepository;


    @Autowired
    IServicioRepository servicioRepository;


    @Autowired
    IHorarioLaboralRepository horarioLaboralRepository;

    @Autowired
    IProfesionalRepository profesionalRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");




    //Obtenemos toda una lista de turnos
    public List<Turno> readAll() {
        return turnoRepository.findAll();
    }

    //Obtenemos un turno por su id
    public Optional<Turno> readOne(Long id) {
        return turnoRepository.findById(id);
    }

    //Actualizamos un turno
    public void update(Turno turno) {
        turnoRepository.save(turno);
    }

    //Eliminamos un turno
    public void delete(Long id) {
        turnoRepository.deleteById(id);
    }


    public void crearTurno(Long clienteId, Long servicioId, LocalDate fecha, LocalTime horaInicio) {

    }


    public void    obtenerHorariosDisponibles(Long servicioId,LocalDate fecha) {




    }





    public Map<String, Object> obtenerHorariosDisponiblesAuto(Long servicioId,LocalDate fecha)  {
        // Crear el mapa de respuesta
        Map<String, Object> response = new HashMap<>();


         usuarioLoggeado = usuarioService.usuarioLogueado();


        if (usuarioLoggeado == null || usuarioLoggeado.getCliente()==null)
        {
            response.put("mensaje", "Usted debe estar logueado y  ser cliente registrado");
            return response;
           }



            // Verificar si la fecha solicitada es anterior a la fecha actual
        if (fecha.isBefore(LocalDate.now())) {
            response.put("mensaje", "La fecha solicitada ya ha pasado. Por favor seleccione una fecha futura.");
            return response;
        }

        // Validar que la fecha solicitada sea al menos un día en el futuro
        if (fecha.isBefore(LocalDate.now().plusDays(1))) {

            response.put("mensaje", "Las reservas deben realizarse con al menos un día de anticipación.");
            return response;
        }

        // Validar que la fecha solicitada esté dentro del rango permitido
        LocalDate maxReserva = LocalDate.now().plusDays(20);
        if (fecha.isAfter(maxReserva)) {

            response.put("mensaje", "No se pueden hacer reservas para más de 20 días en el futuro.");
            return response;

        }

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        int duracionServicio = servicio.getDuracionMinutos();

        // Convertir el día de la semana en inglés a DiaSemana en español
        DiaSemana diaSemana = convertirDayOfWeekADiaSemana(fecha.getDayOfWeek());

        // Obtener los horarios laborales para el día de la semana
        List<HorarioLaboral> horariosLaborales = horarioLaboralRepository.findByDiaSemana(diaSemana);

        if (horariosLaborales.isEmpty()) {
            response.put("mensaje", "No se atiende en " + diaSemana);
            return response;
        }

        // Obtener todos los profesionales
        List<Profesional> profesionales = profesionalRepository.findAll();

        // Obtener los turnos reservados y cancelados para la fecha
        List<Turno> turnosReservadosYCancelados = turnoRepository.findByFecha(fecha);

        // Usar un Set para almacenar intervalos únicos
        Set<Map<String, String>> intervalosDisponibles = new HashSet<>();

        // Para cada profesional, revisar sus horarios laborales y calcular intervalos disponibles
        for (Profesional profesional : profesionales) {
            List<HorarioLaboral> horariosDisponibles = profesional.getHorariosLaborales()
                    .stream()
                    .filter(horarioLaboral -> horariosLaborales.contains(horarioLaboral))
                    .collect(Collectors.toList());

            for (HorarioLaboral horario : horariosDisponibles) {
                LocalTime horaInicio = horario.getHoraInicio();
                LocalTime horaFin = horario.getHoraFin();

                // Ajustar la hora de inicio si es el día actual
                if (fecha.isEqual(LocalDate.now())) {
                    LocalTime ahora = LocalTime.now();
                    // Si el fin del horario es antes de la hora actual, saltar este horario
                    if (horaFin.isBefore(ahora)) {
                        continue;
                    }
                    // Si el inicio del horario es antes de la hora actual, ajustar la hora de inicio
                    if (horaInicio.isBefore(ahora)) {
                        horaInicio = ahora;
                    }
                }

                // Generar intervalos de tiempo disponible
                while (horaInicio.plusMinutes(duracionServicio).isBefore(horaFin) || horaInicio.plusMinutes(duracionServicio).equals(horaFin)) {
                    LocalTime proximaHoraInicio = horaInicio.plusMinutes(duracionServicio);

                    boolean solapado = false;

                    // Verificar solapamientos con turnos existentes
                    for (Turno turno : turnosReservadosYCancelados) {
                        if (turno.getProfesional().equals(profesional) &&
                                ((horaInicio.isBefore(turno.getHoraFin()) && proximaHoraInicio.isAfter(turno.getHoraInicio())) ||
                                        horaInicio.equals(turno.getHoraInicio()) || proximaHoraInicio.equals(turno.getHoraFin()))) {
                            // Si el turno no está cancelado, marca el intervalo como ocupado
                            if (turno.getEstado() != EstadoTurno.CANCELADO) {
                                solapado = true;
                                break;
                            }
                        }
                    }


                    // Comprobar que el cliente no tenga un turno en esa fecha en ese horario

                    List<Turno> t = turnoRepository.findByFechaAndClienteAndHoraInicioAndEstado(fecha,usuarioLoggeado.getCliente(),horaInicio,EstadoTurno.ASIGNADO);



                        if (!t.isEmpty()){
                            solapado = true;

                        }

                    if (!solapado) {



                        intervalosDisponibles.add(Map.of(



                                "hora_inicio", horaInicio.format(TIME_FORMATTER),
                                "hora_fin", proximaHoraInicio.format(TIME_FORMATTER)

                        ));
                    }

                    horaInicio = proximaHoraInicio;
                }
            }
        }

        // Verificar si la lista de intervalos disponibles está vacía
        if (intervalosDisponibles.isEmpty()) {
            response.put("mensaje", "No hay turnos disponibles para este día.");
        } else {
            response.put("horarios_disponibles", new ArrayList<>(intervalosDisponibles));
        }

        return response;
    }



    private DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return DiaSemana.LUNES;
            case TUESDAY:
                return DiaSemana.MARTES;
            case WEDNESDAY:
                return DiaSemana.MIÉRCOLES;
            case THURSDAY:
                return DiaSemana.JUEVES;
            case FRIDAY:
                return DiaSemana.VIERNES;
            case SATURDAY:
                return DiaSemana.SÁBADO;
            case SUNDAY:
                return DiaSemana.DOMINGO;

            default:
                throw new IllegalArgumentException("Día de la semana no soportado: " + dayOfWeek);
        }
    }



    public Profesional obtenerProfesionalDisponible(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        // Obtener todos los profesionales
        List<Profesional> todosLosProfesionales = profesionalRepository.findAll();

        // Filtrar los profesionales que NO tengan un turno asignado en la fecha y hora especificada
        List<Profesional> profesionalesDisponibles = todosLosProfesionales.stream()
                .filter(profesional -> profesional.getTurnos().stream()
                        .noneMatch(turno ->
                                turno.getFecha().equals(fecha) &&
                                        turno.getHoraInicio().equals(horaInicio) &&
                                        turno.getHoraFin().equals(horaFin) && !turno.getEstado().equals(EstadoTurno.CANCELADO)
                        )
                )
                .collect(Collectors.toList());


        // Imprimir la lista de profesionales disponibles
        System.out.println("Profesionales disponibles en la fecha " + fecha + " y horario " + horaInicio + " a " + horaFin + ":");
        profesionalesDisponibles.forEach(profesional ->
                System.out.println("ID: " + profesional.getIdProfesional() + ", Nombre: " + profesional.getUsuario().getUsername())
        );

        // Si la lista no está vacía, seleccionar un profesional aleatorio
        if (!profesionalesDisponibles.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(profesionalesDisponibles.size());
            Profesional profesionalAleatorio = profesionalesDisponibles.get(randomIndex);

            System.out.println("Profesional seleccionado aleatoriamente: ID: " + profesionalAleatorio.getIdProfesional() + ", Nombre: " + profesionalAleatorio.getUsuario().getNombre());
            return profesionalAleatorio;
        } else {
            System.out.println("No hay profesionales disponibles.");
            return null; // O lanzar una excepción si prefieres manejar el caso de otra forma
        }

    }



//
//
//    public List<Turno> findByProfesionalAndOrderByHoraInicioAsc(Profesional p){
//
//
//       return  turnoRepository.findByProfesionalAndOrderByHoraInicioAsc(p);
//    }











    // MODIFICACIONES




    // Para profesionales , admin , secretaria

    public  Turno crearTurno(Turno turno){


        // Verifica si ya existe un turno asignado para el profesional en el mismo horario, excluyendo los cancelados
        Optional<Turno> turnoExistente = turnoRepository.findByProfesionalAndFechaAndHoraInicioAndEstadoNot(
                turno.getProfesional(), turno.getFecha(), turno.getHoraInicio(), EstadoTurno.CANCELADO
        );


        if (turnoExistente.isPresent()) {
            throw new RuntimeException("El profesional ya tiene un turno asignado en este horario.");
        }






        // Asigna el estado 'LIBRE' ya que aún no se ha asignado el cliente ni los servicios
        turno.setEstado(EstadoTurno.LIBRE);

        // El cliente aún no se ha asignado
        turno.setCliente(null);

        // Aun no esta asignado

        turno.setFechaAsignacionTurno(null);

        turno.setServicios(null);

        turno.setHoraFin(null);



        // Guarda el turno como un horario disponible en la agenda del profesional
        return turnoRepository.save(turno);

    }



  // Asignar un turno a un cliente


    public Turno asignarTurno(Long turnoId, Cliente cliente, Set<Servicio> servicios) {
        // Busca el turno por su ID
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        // Obtener la fecha y hora actuales
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaHoraTurno = LocalDateTime.of(turno.getFecha(), turno.getHoraInicio());

        // Verificar que el turno sea al menos 48 horas en el futuro
        if (fechaHoraTurno.isBefore(ahora.plusHours(48))) {
            throw new RuntimeException("No se puede asignar un turno con menos de 48 horas de anticipación.");
        }

        // Asigna el cliente al turno
        turno.setCliente(cliente);

        // Asigna los servicios seleccionados por el cliente
        turno.setServicios(servicios);

        // Calcula la nueva hora de fin basándose en la duración de los servicios
        int duracionTotal = servicios.stream().mapToInt(Servicio::getDuracionMinutos).sum();
        turno.setHoraFin(turno.getHoraInicio().plusMinutes(duracionTotal));

        // Cambia el estado del turno a ASIGNADO
        turno.setEstado(EstadoTurno.ASIGNADO);

        turno.setFechaAsignacionTurno(LocalDateTime.now());


        // El pago se deja como null ya que se procesará después
        turno.setPago(null);

        // Guarda los cambios en el turno
        return turnoRepository.save(turno);
    }


    // Método que se ejecuta automáticamente para verificar y liberar turnos no pagados después de 48 horas
    @Scheduled(fixedRate = 3600000) // Se ejecuta cada 1 hora
    @Transactional
    public void eliminarTurnosNoPagados48Horas() {
        // Obtener la fecha y hora actuales
        LocalDateTime ahora = LocalDateTime.now();

        // Buscar todos los turnos que están en estado ASIGNADO y aún no están pagados
        List<Turno> turnosAsignados = turnoRepository.findByEstadoAndPagoIsNull(EstadoTurno.ASIGNADO);

        // Recorrer la lista de turnos para verificar si han pasado más de 48 horas desde la asignación
        for (Turno turno : turnosAsignados) {
            // Obtener la fecha y hora de asignación del turno
            LocalDateTime fechaAsignacion = turno.getFechaAsignacionTurno();

            // Verificar si han pasado más de 48 horas desde la asignación del turno
            if (fechaAsignacion != null && ahora.isAfter(fechaAsignacion.plusHours(48))) {
                // Liberar el turno en lugar de eliminarlo
                turno.setCliente(null);               // 1. Quitar el cliente
                turno.setFechaAsignacionTurno(null);  // 2. Dejar en null la fecha de asignación
                turno.getServicios().clear();         // 3. Eliminar los servicios asociados
                turno.setEstado(EstadoTurno.LIBRE);   // 4. Cambiar el estado a "LIBRE" o estado similar

                // Guardar los cambios en el turno liberado
                turnoRepository.save(turno);

                // Log o salida informativa
                System.out.println("Turno con ID " + turno.getIdTurno() + " liberado por no haber sido pagado en 48 horas.");
            }
        }
    }






}
