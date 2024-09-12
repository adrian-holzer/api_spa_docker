package com.adri.api_spa.services;

import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

                    if (!solapado) {


                        // Comprobar que el cliente no tenga un turno en esa fecha en ese horario

                        List<Turno> t = turnoRepository.findByFechaAndClienteAndHoraInicioAndEstado(fecha,usuarioLoggeado.getCliente(),horaInicio,EstadoTurno.ASIGNADO);

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





}
