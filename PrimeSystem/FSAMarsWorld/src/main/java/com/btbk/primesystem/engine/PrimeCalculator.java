package com.btbk.primesystem.engine;

import com.btbk.primesystem.model.SheetInput;
import com.btbk.primesystem.model.SheetOutput;
import com.btbk.primesystem.model.SystemFlavor;

public interface PrimeCalculator {
    SystemFlavor flavor();
    SheetOutput compute(SheetInput in);
}
