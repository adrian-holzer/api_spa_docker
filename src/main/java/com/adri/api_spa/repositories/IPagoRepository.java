package com.adri.api_spa.repositories;

import com.adri.api_spa.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPagoRepository extends JpaRepository<Pago, Long> {
}
