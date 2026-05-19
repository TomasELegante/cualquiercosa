package com.example.arenawallet.config;

import com.example.arenawallet.model.Billetera;
import com.example.arenawallet.repository.BilleteraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BilleteraRepository billeteraRepository;

    public DataInitializer(BilleteraRepository billeteraRepository) {
        this.billeteraRepository = billeteraRepository;
    }

    @Override
    public void run(String... args) {
        if (billeteraRepository.count() == 0) {
            billeteraRepository.save(new Billetera(null, 1L, 5000.0, 5));
            billeteraRepository.save(new Billetera(null, 2L, 12000.0, 12));
            billeteraRepository.save(new Billetera(null, 3L, 1000.0, 1));
            System.out.println(">>> Datos iniciales de billeteras cargados.");
        }
    }
}