package com.example.arenainventory.config;

import com.example.arenainventory.model.Producto;
import com.example.arenainventory.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DataInitializer(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            productoRepository.save(new Producto(null, "PlayStation 5",
                    Producto.Categoria.CONSOLA, 3, 2500.0));
            productoRepository.save(new Producto(null, "Mouse Logitech G502",
                    Producto.Categoria.PERIFERICO, 10, 500.0));
            productoRepository.save(new Producto(null, "FIFA 25",
                    Producto.Categoria.JUEGO, 5, 800.0));
            System.out.println(">>> Datos iniciales de productos cargados.");
        }
    }
}