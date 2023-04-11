package io.github.bennyboy1695.mechanicalsifting.register;

import com.jozufozu.flywheel.core.PartialModel;
import io.github.bennyboy1695.mechanicalsifting.MechanicalSifting;

public class ModPartials {

    public static final PartialModel SIFTER_TOP = block("sifter_top");

    private static PartialModel block(String path) {
        return new PartialModel(MechanicalSifting.rl("block/" + path));
    }

    public static void init() {
        //Just for loading the statics
    }
}
