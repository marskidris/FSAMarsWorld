package com.btbk.primesystem.engine.impl;

import com.btbk.primesystem.model.Attributes;
import com.btbk.primesystem.model.SheetInput;
import com.btbk.primesystem.model.SheetOutput;
import com.btbk.primesystem.model.SystemFlavor;
import com.btbk.primesystem.engine.PrimeCalculator;

public class LegacyCalculator implements PrimeCalculator {

    @Override public SystemFlavor flavor() { return SystemFlavor.LEGACY; }

    @Override
    public SheetOutput compute(SheetInput in) {
        Attributes a = in.attrs();
        double s = safe(a.self());
        double i = safe(a.intent());

        double HP = (double) Math.round(a.vitality() * 3.0);
        double SP = (double) Math.round(a.endurance() * 3.0);
        Double MP = (a.mind() == null ? null : (double) Math.round(a.mind() * 3.0));
        Double DP = (a.divine() == null ? null : (double) Math.round(a.divine() * 3.0));

        double defTerm = (i / 3.0) + s;
        double baseAtk;
        double baseDef;

        switch (in.role()) {
            case MAGE -> {
                baseAtk = (double) Math.round(a.power() * 0.3);
                baseDef = (double) Math.round(defTerm   * 0.3);
            }
            case WARRIOR -> {
                boolean fullTank = in.fullTank() != null && in.fullTank();
                if (fullTank) {
                    baseAtk = (double) Math.round(a.power() * 0.3);
                    baseDef = (double) Math.round(defTerm   * 0.5);
                } else {
                    baseAtk = (double) Math.round(a.power() * 0.5);
                    baseDef = (double) Math.round(defTerm   * 0.3);
                }
            }
            case TANK -> {
                baseAtk = (double) Math.round(a.power() * 0.3);
                baseDef = (double) Math.round(defTerm   * 0.5);
            }
            default -> { // HYBRID
                baseAtk = (double) Math.round(a.power() * 0.25);
                baseDef = (double) Math.round(defTerm   * 0.25);
            }
        }

        double denom = s + (i / 3.0);
        if (denom <= 0.0) denom = 1.0;

        double hpHr = (double) Math.round((HP / denom) * 12.0);
        double spHr = (double) Math.round((SP / denom) * 12.0);
        Double mpHr = (MP == null ? null : (double) Math.round((MP / denom) * 12.0));
        Double dpHr = (DP == null ? null : (double) Math.round((DP / denom) * 12.0));

        return new SheetOutput(baseAtk, baseDef, HP, SP, MP, DP, hpHr, spHr, mpHr, dpHr, in.formulaVersion());
    }

    private static double safe(Double v){ return v==null?0.0:v; }
}
