package io.github.bennyboy1695.mechanicalmachinery.block.sifter.client;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.utility.AngleHelper;
import io.github.bennyboy1695.mechanicalmachinery.block.sifter.SifterBlockEntity;
import io.github.bennyboy1695.mechanicalmachinery.register.ModPartials;

public class SifterInstance extends TinyShaftInstance implements DynamicInstance {

    private final OrientedData sifterTop;
    private final SifterBlockEntity sifter;
    private TinyShaftInstance tinyShaftInstance;

    public SifterInstance(MaterialManager dispatcher, SifterBlockEntity tile) {
        super(dispatcher, tile);
        sifter = tile;
        tinyShaftInstance = new TinyShaftInstance(dispatcher, tile);
        sifterTop = dispatcher.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(ModPartials.SIFTER_TOP, blockState)
                .createInstance();

        Quaternion q = Vector3f.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)));

        sifterTop.setRotation(q);

        transformModels();
    }

    @Override
    public void beginFrame() {
        transformModels();
    }

    private void transformModels() {
        float renderedHeadOffset = getRenderedTopOffset(sifter);
        if (sifter.shouldTopMove()) {
            sifterTop.setPosition(getInstancePosition())
                    .nudge(getActualValue(sifter.renderStep().x()), -renderedHeadOffset, getActualValue(sifter.renderStep().z()));
        } else {
            sifterTop.setPosition(getInstancePosition())
                    .nudge(0, -renderedHeadOffset, 0);
        }
    }

    private float getActualValue(float value) {
        return (value == 0f ? 0f : (value == 1f ? value - 0.980f : (value == -1 ? value + 0.980f : value)));
    }

    private float getRenderedTopOffset(SifterBlockEntity sifter) {
        return 0;
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, sifterTop);
    }

    @Override
    public void remove() {
        super.remove();
        sifterTop.delete();
    }
}
