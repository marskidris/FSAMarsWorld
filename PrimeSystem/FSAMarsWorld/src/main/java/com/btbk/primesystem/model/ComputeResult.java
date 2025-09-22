package com.btbk.primesystem.model;

public record ComputeResult(
        String role,
        String aspect,
        String flavor,
        Boolean fullTank,
        String formulaVersion,
        SheetOutput output,
        Boolean beyondMortal,
        String s) {}
