package team.lodestar.lodestone.systems.network;
/*
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class LodestoneClientNBTPacket extends LodestoneClientPacket {
    protected CompoundTag data;

    public LodestoneClientNBTPacket(CompoundTag data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    @Override
    public final void execute(Supplier<NetworkEvent.Context> context) {
        execute(context, data);
    }

    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }
}

 */