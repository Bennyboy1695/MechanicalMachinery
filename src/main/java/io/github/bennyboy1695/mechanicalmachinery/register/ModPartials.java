package io.github.bennyboy1695.mechanicalmachinery.register;

import com.jozufozu.flywheel.core.PartialModel;
import io.github.bennyboy1695.mechanicalmachinery.MechanicalMachinery;

public class ModPartials {

    public static final PartialModel SIFTER_TOP = block("sifter_top");
    public static final PartialModel TINY_SHAFT = block("shaft_double_half");

    private static PartialModel block(String path) {
        return new PartialModel(MechanicalMachinery.rl("block/" + path));
    }

    public static void init() {
        //Just for loading the statics
    }
}
