package com.example.arenawallet.repository;

import com.example.arenawallet.model.BilleteraHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilleteraHistoryRepository extends JpaRepository<BilleteraHistory, Long> {

    List<BilleteraHistory> findByBilleteraIdOrderByFechaOperacionDesc(Long billeteraId);
}