package com.btbk.primesystem.config;

import com.btbk.primesystem.engine.PrimeEngine;
import com.btbk.primesystem.engine.impl.LegacyCalculator;
import com.btbk.primesystem.engine.impl.SphereCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EngineConfig {

    @Bean
    public PrimeEngine primeEngine() {
        return new PrimeEngine(List.of(
                new LegacyCalculator(),
                new SphereCalculator()
        ));
    }
}
