package com.adri.api_spa.repositories;

import com.adri.api_spa.models.Usuarios;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUsuariosRepository extends JpaRepository<Usuarios, Long> {
    //Método para poder buscar un usuario mediante su nombre
    Optional<Usuarios> findByUsername(String username);

    //Método para poder verificar si un usuario existe en nuestra base de datos
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByDni(String dni);


    Optional<Usuarios> findById(Long idUsuario);

    // Método para obtener el último usuario creado
     Usuarios findFirstByOrderByIdUsuarioDesc();

    // Consulta personalizada para buscar usuarios con rol CLIENTE
    @Query("SELECT u FROM Usuarios u INNER JOIN u.roles r WHERE r.name = :roleName")
    List<Usuarios> findAllByRole(@Param("roleName") String roleName);

    Optional<Usuarios> findByEmail(String email);

    Optional<Usuarios> findByTokenRecuperacion(String tokenRecuperacion);
}
