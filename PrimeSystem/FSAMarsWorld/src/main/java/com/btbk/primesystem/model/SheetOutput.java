package com.btbk.primesystem.model;

public record SheetOutput(
        // Always present
        double baseAttack,
        double baseDefense,

        // Core pools
        Double hp,
        Double sp,
        Double mp,
        Double dp,

        // Core regens
        Double hpPerHour,
        Double spPerHour,
        Double mpPerHour,
        Double dpPerHour,

        // Metadata
        String formulaVersion

        // Extra pools
        // Force
        // Soul
        // Spirit

        // Extra regens
) {}