package com.adri.api_spa.repositories;

import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.EstadoTurno;
import com.adri.api_spa.models.Pago;
import com.adri.api_spa.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPagoRepository extends JpaRepository<Pago, Long> {



    @Query("SELECT p FROM Pago p JOIN p.turno t WHERE t.cliente.id = :clienteId ")
    List<Pago> findPagosByClienteId(@Param("clienteId") Long clienteId);


    @Query("SELECT p.metodoPago, SUM(p.monto) FROM Pago p WHERE DATE(p.fechaPago) BETWEEN :fechaInicio AND :fechaFin GROUP BY p.metodoPago")
    List<Object[]> findIngresosPorTipoDePagoEnRangoDeFechas(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
}
