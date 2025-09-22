package com.btbk.primesystem.engine;

import com.btbk.primesystem.model.SheetInput;
import com.btbk.primesystem.model.SheetOutput;
import com.btbk.primesystem.model.SystemFlavor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrimeEngine {
    private final Map<SystemFlavor, PrimeCalculator> calculators;

    public PrimeEngine(List<PrimeCalculator> calcs) {
        this.calculators = calcs.stream()
                .collect(Collectors.toMap(PrimeCalculator::flavor, c -> c));
    }

    public SheetOutput compute(SheetInput input) {
        PrimeCalculator calc = calculators.get(input.flavor());
        if (calc == null) {
            throw new IllegalArgumentException("No calculator registered for flavor: " + input.flavor());
        }
        return calc.compute(input);
    }
}
