package io.github.bennyboy1695.mechanicalmachinery.block.sifter.client;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;
import io.github.bennyboy1695.mechanicalmachinery.register.ModPartials;
import net.minecraft.core.Direction;

public class TinyShaftInstance extends SingleRotatingInstance {

    public TinyShaftInstance(MaterialManager modelManager, KineticTileEntity tile) {
        super(modelManager, tile);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        Direction dir = getShaftDirection();
        return getRotatingMaterial().getModel(ModPartials.TINY_SHAFT, blockState, dir);
    }

    protected Direction getShaftDirection() {
        return Direction.DOWN;
    }
}
