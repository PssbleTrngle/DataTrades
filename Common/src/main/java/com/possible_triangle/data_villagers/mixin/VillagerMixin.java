package com.possible_triangle.data_villagers.mixin;

import com.possible_triangle.data_villagers.data.ProfessionReloader;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Villager.class)
public abstract class VillagerMixin {

    @Shadow
    public abstract VillagerData getVillagerData();

    @ModifyConstant(method = "updateTrades()V", constant = @Constant(intValue = 2))
    private int injected(int value) {
        var self = (Villager) (Object) this;
        return ProfessionReloader.takeTradesAmount(self, getVillagerData().getLevel()).orElse(value);
    }

}
