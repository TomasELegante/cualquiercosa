package com.example.stationservice.config;

import com.example.stationservice.model.Station;
import com.example.stationservice.repository.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StationRepository stationRepository;

    public DataLoader(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void run(String... args) {
        if (stationRepository.count() == 0) {
            Station s1 = new Station();
            s1.setName("PC-01");
            s1.setType(Station.StationType.PC);
            s1.setStatus(Station.StationStatus.OPERATIONAL);
            s1.setSpecs("RTX 4090 / i9-13900K / 32GB RAM");
            s1.setAvailable(true);

            Station s2 = new Station();
            s2.setName("PC-02");
            s2.setType(Station.StationType.PC);
            s2.setStatus(Station.StationStatus.OPERATIONAL);
            s2.setSpecs("RTX 3080 / i7-12700K / 16GB RAM");
            s2.setAvailable(true);

            Station s3 = new Station();
            s3.setName("PC-03");
            s3.setType(Station.StationType.PC);
            s3.setStatus(Station.StationStatus.MAINTENANCE);
            s3.setSpecs("RTX 3070 / i5-12600K / 16GB RAM");
            s3.setAvailable(false);

            Station s4 = new Station();
            s4.setName("CONSOLE-01");
            s4.setType(Station.StationType.CONSOLE);
            s4.setStatus(Station.StationStatus.OPERATIONAL);
            s4.setSpecs("PS5 / 4K / 120fps");
            s4.setAvailable(true);

            Station s5 = new Station();
            s5.setName("CONSOLE-02");
            s5.setType(Station.StationType.CONSOLE);
            s5.setStatus(Station.StationStatus.OFFLINE);
            s5.setSpecs("Xbox Series X / 4K / 120fps");
            s5.setAvailable(false);

            stationRepository.save(s1);
            stationRepository.save(s2);
            stationRepository.save(s3);
            stationRepository.save(s4);
            stationRepository.save(s5);
        }
    }
}