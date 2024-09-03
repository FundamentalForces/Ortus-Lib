package team.lodestar.lodestone.systems.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.registry.common.*;

public class LodestoneSignBlockEntity extends SignBlockEntity {
    public LodestoneSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return LodestoneBlockEntities.SIGN.get();
    }
}
