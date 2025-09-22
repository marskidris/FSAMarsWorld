package com.btbk.primesystem.model;

public record SheetInput(
        Attributes attrs,
        Role role,
        Aspect aspect,
        SystemFlavor flavor,
        Boolean fullTank,       // used by LEGACY; ignored by SPHERE
        String formulaVersion,
        Boolean beyondMortal
) {}
