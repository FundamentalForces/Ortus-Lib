package team.lodestar.lodestone.mixin.common;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.lodestar.lodestone.registry.common.LodestoneAttributes;

import java.util.Map;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private AttributeModifier lodestone$attributeModifier;

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getId()Ljava/util/UUID;", ordinal = 0), index = 13)
    private AttributeModifier lodestone$getTooltip(AttributeModifier value) {
        this.lodestone$attributeModifier = value;
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;", ordinal = 0), index = 16)
    private boolean lodestone$getTooltip(boolean value, @Nullable Player player, TooltipFlag flag) {
        if (player != null) {
            if (lodestone$attributeModifier.getId().equals(LodestoneAttributes.UUIDS.get(LodestoneAttributes.MAGIC_DAMAGE))) {
                return true;
            }
        }
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"))
    private Multimap<Attribute, AttributeModifier> lodestone$getTooltip(Multimap<Attribute, AttributeModifier> map, @Nullable Player player, TooltipFlag flag) {
        if (player != null) {
            Multimap<Attribute, AttributeModifier> copied = LinkedHashMultimap.create();
            for (Map.Entry<Attribute, AttributeModifier> entry : map.entries()) {
                Attribute key = entry.getKey();
                AttributeModifier modifier = entry.getValue();
                double amount = modifier.getAmount();
                if (modifier.getId().equals(LodestoneAttributes.UUIDS.get(LodestoneAttributes.MAGIC_DAMAGE))) {
                    AttributeInstance instance = player.getAttribute(LodestoneAttributes.MAGIC_PROFICIENCY.get());
                    if (instance != null && instance.getValue() > 0) {
                        amount *= (1 + instance.getValue() * 0.1f);
                    }
                    copied.put(key, new AttributeModifier(
                            modifier.getId(), modifier.getName(), amount, modifier.getOperation()
                    ));
                } else {
                    copied.put(key, modifier);
                }
            }

            return copied;
        }
        return map;
    }
}