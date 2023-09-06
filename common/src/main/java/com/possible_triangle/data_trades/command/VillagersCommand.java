package com.possible_triangle.data_trades.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.stream.StreamSupport;

public class VillagersCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("villagers").requires(it -> it.hasPermission(3))
                .then(Commands.literal("refresh").executes(VillagersCommand::resetVillagers))
        );
    }

    private static int resetVillagers(CommandContext<CommandSourceStack> context) {
        var server = context.getSource().getServer();
        return (int) StreamSupport.stream(server.getAllLevels().spliterator(), false)
                .flatMap(level -> level.getEntities(EntityTypeTest.forClass(Villager.class), $ -> true).stream()
                        .filter(VillagersCommand::resetVillager))
                .count();
    }

    private static boolean resetVillager(Villager villager) {
        var offers = new MerchantOffers();
        var data = villager.getVillagerData();

        var trades = VillagerTrades.TRADES.get(data.getProfession());
        if (trades == null || trades.isEmpty()) return false;

        for (int level = 1; level <= data.getLevel(); level++) {
            var listings = trades.get(level);
            var tradeCount = ProfessionReloader.INSTANCE.takeTradesAmount(villager, level).orElse(2);
            if (listings == null) continue;

            var shuffled = Arrays.asList(listings);
            Collections.shuffle(shuffled, new Random(villager.getRandom().nextLong()));
            shuffled.subList(0, Math.min(shuffled.size(), tradeCount)).stream()
                    .map(it -> it.getOffer(villager, villager.getRandom()))
                    .filter(Objects::nonNull)
                    .forEach(offers::add);
        }

        villager.setOffers(offers);
        return true;
    }

}
