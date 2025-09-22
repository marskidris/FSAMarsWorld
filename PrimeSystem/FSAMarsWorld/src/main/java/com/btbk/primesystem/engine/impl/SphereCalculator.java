package com.btbk.primesystem.engine.impl;

import com.btbk.primesystem.engine.PrimeCalculator;
import com.btbk.primesystem.model.*;

import java.util.ArrayList;
import java.util.List;

public class SphereCalculator implements PrimeCalculator {

    @Override
    public SystemFlavor flavor() { return SystemFlavor.SPHERE; }

    @Override
    public SheetOutput compute(SheetInput in) {
        Attributes a = in.attrs();
        Totals t = initializeTotals(a);

        // Canon checks
        if (!t.powerValid || !t.vitalityValid || !t.selfValid || !t.intentValid) {
            throw new IllegalArgumentException("Missing essential attributes: power, vitality, self, intent.");
        }
        if (!t.enduranceValid && !t.mindValid) {
            throw new IllegalArgumentException("Sustenance required: provide endurance (physical) OR mind (pure magical).");
        }
        if (t.totA <= 0.0) {
            throw new IllegalArgumentException("Total attributes (totA) cannot be zero or negative.");
        }

        // Angles + radius
        double thetaP = acosChecked(t.power / t.totA, "power/totA");
        double thetaD = acosChecked(t.self  / t.totA, "self/totA");
        double radius = Math.sqrt(t.totA / (4.0 * Math.PI));

        // Base ATK/DEF (rounded)
        double baseAtk = (double) Math.round(radius * Math.cos(thetaP) * t.power);
        boolean beyondMortal = in.beyondMortal() != null && in.beyondMortal();
        double baseDefRaw = beyondMortal
                ? (radius * Math.cos(thetaD) * t.self)
                : (radius * Math.cos(thetaD) * t.self) * (1.0 - (t.intent / t.totA));
        double baseDef = (double) Math.round(baseDefRaw);

        // Pools (rounded; may be null)
        Double hp  = t.vitalityValid  ? (double) Math.round(t.vitality * radius) : null;
        Double sp  = t.enduranceValid ? (double) Math.round(t.endurance * radius) : null;
        Double mp  = t.mindValid      ? (double) Math.round(t.mind * radius) : null;
        Double dp  = t.divineValid    ? (double) Math.round(t.divine * radius) : null;

        Double sprPool = t.spiritValid ? (double) Math.round(t.spirit * radius) : null;
        Double fpPool  = t.forceValid  ? (double) Math.round(t.force  * radius) : null;
        Double slPool  = t.soulValid   ? (double) Math.round(t.soul   * radius) : null;

        // Regens (rounded; may be null)
        Double hpHr  = (t.vitalityValid  && hp  != null) ? (double) Math.round(((t.vitality / t.totA) * 3.0) * (hp  / 100.0) * 6.0) : null;
        Double spHr  = (t.enduranceValid && sp  != null) ? (double) Math.round(((t.endurance / t.totA) * 3.0) * (sp  / 100.0) * 6.0) : null;
        Double mpHr  = (t.mindValid      && mp  != null) ? (double) Math.round(((t.mind     / t.totA) * 3.0) * (mp  / 100.0) * 6.0) : null;
        Double dpHr  = (t.divineValid    && dp  != null) ? (double) Math.round(((t.divine   / t.totA) * 3.0) * (dp  / 100.0) * 6.0) : null;

        Double sprHr = (t.spiritValid    && sprPool != null) ? (double) Math.round(((t.spirit / t.totA) * 3.0) * (sprPool / 100.0) * 6.0) : null;
        Double fpHr  = (t.forceValid     && fpPool  != null) ? (double) Math.round(((t.force  / t.totA) * 3.0) * (fpPool  / 100.0) * 6.0) : null;
        Double slHr  = (t.soulValid      && slPool  != null) ? (double) Math.round(((t.soul   / t.totA) * 3.0) * (slPool  / 100.0) * 6.0) : null;

        // IMPORTANT: coalesce nullable Doubles to 0.0 for any primitive fields in SheetOutput
        return new SheetOutput(
                baseAtk,
                baseDef,
                nz(hp),
                nz(sp),
                nz(mp),           // ensure MP is always a number
                dp,           // same here: nz(dp) if primitive
                nz(hpHr),
                nz(spHr),
                nz(mpHr),         // ensure MP/hr is always a number
                dpHr,         // <-- was null; if primitive in your model: nz(dpHr)
                in.formulaVersion()
                // use nz(...) here too if your fields are primitive
                // same—wrap with nz(...) if primitives
        );
    }

    // ---------- helpers ----------

    private static double nz(Double v) { return v == null ? 0.0 : v; }

    private static Totals initializeTotals(Attributes a) {
        double vitality = safe(a.vitality());
        double power    = safe(a.power());
        double self     = safe(a.self());
        double intent   = safe(a.intent());
        double endurance = safe(a.endurance());
        double mind      = safe(a.mind());
        double divine    = safe(a.divine());
        double spirit    = safe(a.spirit());
        double force     = safe(a.force());
        double soul      = safe(a.soul());

        // Only include attributes that are not -1.0 in the total sum, matching SphereOrigin
        List<Double> TA = new ArrayList<>();
        TA.add(vitality);
        TA.add(power);
        TA.add(self);
        if (endurance != -1.0) TA.add(endurance);
        if (mind != -1.0) TA.add(mind);
        if (intent != -1.0) TA.add(intent);
        if (divine != -1.0) TA.add(divine);
        double totA = TA.stream().mapToDouble(Double::doubleValue).sum();

        boolean vitalityValid = isValid(vitality);
        boolean powerValid    = isValid(power);
        boolean selfValid     = isValid(self);
        boolean intentValid   = isValid(intent);
        boolean enduranceValid= isValid(endurance);
        boolean mindValid     = isValid(mind);
        boolean divineValid   = isValid(divine);
        boolean spiritValid   = isValid(spirit);
        boolean forceValid    = isValid(force);
        boolean soulValid     = isValid(soul);

        return new Totals(vitality, power, self, intent, endurance, mind, divine, spirit, force, soul,
                vitalityValid, powerValid, selfValid, intentValid, enduranceValid, mindValid, divineValid, spiritValid, forceValid, soulValid, totA);
    }

    private static boolean isValid(Double v){ return v != null && v != -1.0; }
    private static double safe(Double v){ return v == null ? 0.0 : v; }
    private static double acosChecked(double x, String label){
        if (x < -1.0 || x > 1.0) throw new IllegalArgumentException("Value out of range for acos: " + label + " = " + x);
        return Math.acos(x);
    }

    public static final class Totals {
        double totA;
        double vitality, power, self, intent;
        double endurance, mind, divine;
        double spirit, force, soul;
        boolean vitalityValid, powerValid, selfValid, intentValid;
        boolean enduranceValid, mindValid, divineValid;
        boolean spiritValid, forceValid, soulValid;

        public Totals(double vitality, double power, double self, double intent, double endurance, double mind, double divine,
               double spirit, double force, double soul,
               boolean vitalityValid, boolean powerValid, boolean selfValid, boolean intentValid,
               boolean enduranceValid, boolean mindValid, boolean divineValid,
               boolean spiritValid, boolean forceValid, boolean soulValid, double totA) {
            this.vitality = vitality;
            this.power = power;
            this.self = self;
            this.intent = intent;
            this.endurance = endurance;
            this.mind = mind;
            this.divine = divine;
            this.spirit = spirit;
            this.force = force;
            this.soul = soul;
            this.totA = totA;

            this.vitalityValid = vitalityValid;
            this.powerValid = powerValid;
            this.selfValid = selfValid;
            this.intentValid = intentValid;
            this.enduranceValid = enduranceValid;
            this.mindValid = mindValid;
            this.divineValid = divineValid;
            this.spiritValid = spiritValid;
            this.forceValid = forceValid;
            this.soulValid = soulValid;
        }
    }
}