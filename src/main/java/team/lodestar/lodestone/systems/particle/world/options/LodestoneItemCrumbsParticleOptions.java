package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.systems.particle.world.type.*;

import java.util.function.*;

public class LodestoneItemCrumbsParticleOptions extends WorldParticleOptions {

    public final ItemStack stack;

    public LodestoneItemCrumbsParticleOptions(ParticleType<LodestoneItemCrumbsParticleOptions> type, ItemStack stack) {
        super(type);
        this.stack = stack;
    }

    public LodestoneItemCrumbsParticleOptions(Supplier<? extends LodestoneItemCrumbsParticleType> type, ItemStack stack) {
        this(type.get(), stack);
    }
}