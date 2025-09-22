package com.btbk.primesystem.api;

import com.btbk.primesystem.engine.PrimeEngine;
import com.btbk.primesystem.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prime")
public class PrimeController {

    private final PrimeEngine engine;

    public PrimeController(PrimeEngine engine) {
        this.engine = engine;
    }

    @PostMapping("/compute")
    @ResponseStatus(HttpStatus.OK)
    public ComputeResult compute(@RequestBody SheetInput input) {
        SheetOutput out = engine.compute(input);
        return new ComputeResult(
                input.role().name(),
                input.aspect().name(),
                input.flavor().name(),
                input.fullTank(),
                input.formulaVersion(),
                out,
                input.beyondMortal() == null ? Boolean.FALSE : input.beyondMortal(),
                resourceNameFor(input.aspect())
        );
    }

    // Quick compare Sphere vs Legacy (Mage) with optional beyondMortal & gating
    @GetMapping("/example")
    public Map<String, Object> example(
            @RequestParam(name = "beyondMortal", defaultValue = "false") boolean beyondMortal,
            @RequestParam(name = "characterType", defaultValue = "HUMANOID") CharacterType characterType,
            @RequestParam(name = "allowNonSentient", defaultValue = "false") boolean allowNonSentient
    ) {
        Attributes base = new Attributes(
                20, 18, 16, 14.0, 8.0, 12.0, 0.0,
                -1.0, -1.0, -1.0 // force, soul, spirit absent by default
        );
        Attributes gated = applyCharacterGates(base, characterType);

        // If gates removed an essential and we don't allow non-sentient, error
        if (!allowNonSentient && violatesEssentials(gated)) {
            throw new IllegalArgumentException("CharacterType " + characterType +
                    " removes an essential (power/vitality/self/intent). Set allowNonSentient=true to test anyway.");
        }

        SheetInput sphereInput = new SheetInput(
                gated, Role.MAGE, Aspect.MAGIC, SystemFlavor.SPHERE,
                Boolean.FALSE, "sphere-2.0", beyondMortal
        );
        SheetInput legacyInput = new SheetInput(
                gated, Role.MAGE, Aspect.MAGIC, SystemFlavor.LEGACY,
                Boolean.FALSE, "legacy-1.0", null
        );

        return Map.of(
                "role", Role.MAGE.name(),
                "aspect", Aspect.MAGIC.name(),
                "characterType", characterType.name(),
                "sphere", new ComputeResult(Role.MAGE.name(), Aspect.MAGIC.name(),
                        SystemFlavor.SPHERE.name(), Boolean.FALSE, "sphere-2.0",
                        engine.compute(sphereInput), beyondMortal, resourceNameFor(Aspect.MAGIC)),
                "legacy", new ComputeResult(Role.MAGE.name(), Aspect.MAGIC.name(),
                        SystemFlavor.LEGACY.name(), Boolean.FALSE, "legacy-1.0",
                        engine.compute(legacyInput), null, resourceNameFor(Aspect.MAGIC))
        );
    }

    // Try arbitrary numbers with SPHERE via query params + auto-gate
    @GetMapping("/try-sphere")
    public ComputeResult trySphere(
            @RequestParam(name = "power",     defaultValue = "20") double power,
            @RequestParam(name = "vitality",  defaultValue = "18") double vitality,
            @RequestParam(name = "endurance", defaultValue = "16") double endurance,
            @RequestParam(name = "mind",      defaultValue = "14") Double mind,
            @RequestParam(name = "self",      defaultValue = "8")  Double self,
            @RequestParam(name = "intent",    defaultValue = "12") Double intent,
            @RequestParam(name = "divine",    defaultValue = "0")  Double divine,
            @RequestParam(name = "force",     defaultValue = "-1") Double force,
            @RequestParam(name = "soul",      defaultValue = "-1") Double soul,
            @RequestParam(name = "spirit",    defaultValue = "-1") Double spirit,
            @RequestParam(name = "role",      defaultValue = "MAGE") Role role,
            @RequestParam(name = "aspect",    defaultValue = "MAGIC") Aspect aspect,
            @RequestParam(name = "formulaVersion", defaultValue = "sphere-2.0") String formulaVersion,
            @RequestParam(name = "beyondMortal", defaultValue = "false") boolean beyondMortal,
            @RequestParam(name = "characterType", defaultValue = "HUMANOID") CharacterType characterType,
            @RequestParam(name = "allowNonSentient", defaultValue = "false") boolean allowNonSentient
    ) {
        Attributes base = new Attributes(
                power, vitality, endurance, mind, self, intent, divine,
                force, soul, spirit
        );
        Attributes gated = applyCharacterGates(base, characterType);

        if (!allowNonSentient && violatesEssentials(gated)) {
            throw new IllegalArgumentException("CharacterType " + characterType +
                    " removes an essential (power/vitality/self/intent). Set allowNonSentient=true to test anyway.");
        }

        SheetInput input = new SheetInput(
                gated, role, aspect, SystemFlavor.SPHERE,
                Boolean.FALSE, formulaVersion, beyondMortal
        );
        SheetOutput out = engine.compute(input);
        return new ComputeResult(role.name(), aspect.name(), SystemFlavor.SPHERE.name(),
                Boolean.FALSE, formulaVersion, out, beyondMortal, resourceNameFor(aspect));
    }

    // ---------- CharacterType + gating (mirrors your original switch) ----------

    public enum CharacterType { MAGIC, HUMANOID, BEAST, MBEAST, GOLEM, DIV, DIV2 }

    private static Attributes applyCharacterGates(Attributes a, CharacterType t) {
        // Start with current values
        double power = a.power();
        double vitality = a.vitality();
        double endurance = nz(a.endurance());
        double mind = nz(a.mind());
        double self = nz(a.self());
        double intent = nz(a.intent());
        double divine = nz(a.divine());
        double force = nz(a.force());
        double soul  = nz(a.soul());
        double spirit= nz(a.spirit());

        switch (t) {
            case MAGIC -> {
                divine = -1.0;
            }
            case HUMANOID -> {
                divine = -1.0;
            }
            case BEAST -> {
                mind = -1.0;
                intent = -1.0;  // Non-sentient → will violate essentials unless allowNonSentient=true
                divine = -1.0;
            }
            case MBEAST -> {
                intent = -1.0; // Non-sentient
                divine = -1.0;
            }
            case GOLEM -> {
                endurance = -1.0;
                intent = -1.0; // Non-sentient
                divine = -1.0;
                spirit = -1.0;
                force = -1.0;
                soul = -1.0;
            }
            case DIV -> {
                divine = nz(a.divine()); // keep divine enabled
            }
            case DIV2 -> {
                mind = -1.0;
            }
            default -> { /* no-op */ }
        }

        // Build the new record (10 args)
        return new Attributes(
                power, vitality, endurance, mind, self, intent, divine,
                force, soul, spirit
        );
    }

    private static boolean violatesEssentials(Attributes a) {
        return isInvalid(a.power()) || isInvalid(a.vitality()) || isInvalid(a.self()) || isInvalid(a.intent());
    }

    private static boolean isInvalid(Double v) { return v == null || v == -1.0; }
    private static double nz(Double v) { return v == null ? 0.0 : v; }

    private static String resourceNameFor(Aspect aspect) {
        return switch (aspect) {
            case MAGIC -> "MP";
            case CHI -> "CP";
            case ENERGY -> "EP";
            case DIVINE -> "DP";
            default -> "MP";
        };
    }
}