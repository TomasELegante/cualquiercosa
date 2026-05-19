package com.example.arenawallet.repository;

import com.example.arenawallet.model.Billetera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BilleteraRepository extends JpaRepository<Billetera, Long> {

    boolean existsByIdUsuario(Long idUsuario);

    boolean existsByIdUsuarioAndIdNot(Long idUsuario, Long id);
}