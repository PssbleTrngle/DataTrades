package com.possible_triangle.data_trades;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.awt.Color;

public class DyeItemFunction extends LootItemConditionalFunction {

    protected DyeItemFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!(stack.getItem() instanceof DyeableLeatherItem item)) return stack;

        if(context.getRandom().nextInt(5) == 0) {
            var brightness = context.getRandom().nextFloat();
            var color = Color.getHSBColor(0F, 0F, brightness);
            item.setColor(stack, color.getRGB());
        } else {
            var hue = context.getRandom().nextFloat();
            var saturation = context.getRandom().nextFloat() * 0.55F + 0.4F;
            var brightness = context.getRandom().nextFloat() * 0.4F + 0.4F;
            var color = Color.getHSBColor(hue, saturation, brightness);
            item.setColor(stack, color.getRGB());
        }

        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return ForgeEntrypoint.DYE_ITEM_FUNCTION.get();
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<DyeItemFunction> {
        @Override
        public DyeItemFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new DyeItemFunction(conditions);
        }
    }

}
