package team.lodestar.lodestone.networkold.interaction;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class UpdateLeftClickPacket extends LodestoneServerPacket {

    private final boolean leftClickHeld;

    public UpdateLeftClickPacket(boolean rightClick) {
        this.leftClickHeld = rightClick;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(leftClickHeld);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        LodestonePlayerDataCapability.getCapabilityOptional(context.get().getSender()).ifPresent(c -> c.leftClickHeld = leftClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateLeftClickPacket.class, UpdateLeftClickPacket::encode, UpdateLeftClickPacket::decode, UpdateLeftClickPacket::handle);
    }

    public static UpdateLeftClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateLeftClickPacket(buf.readBoolean());
    }
}