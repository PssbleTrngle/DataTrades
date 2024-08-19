package com.possible_triangle.data_trades;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.awt.*;
import java.util.List;

public class DyeItemFunction extends LootItemConditionalFunction {

    protected DyeItemFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    public static final MapCodec<DyeItemFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> {
        return commonFields(builder).apply(builder, DyeItemFunction::new);
    });

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if(!stack.is(ItemTags.DYEABLE)) return stack;

        if (context.getRandom().nextInt(5) == 0) {
            var brightness = context.getRandom().nextFloat();
            var color = Color.getHSBColor(0F, 0F, brightness);
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getRGB(), true));
        } else {
            var hue = context.getRandom().nextFloat();
            var saturation = context.getRandom().nextFloat() * 0.55F + 0.4F;
            var brightness = context.getRandom().nextFloat() * 0.4F + 0.4F;
            var color = Color.getHSBColor(hue, saturation, brightness);
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getRGB(), true));
        }

        return stack;
    }

    @Override
    public LootItemFunctionType<DyeItemFunction> getType() {
        return CommonClass.DYE_ITEM_FUNCTION.get();
    }

}
