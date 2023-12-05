package com.lypaka.pixelskills.Listeners;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PixelSkills.MOD_ID)
public class JoinListener {

    public static Map<UUID, Account> accountMap = new HashMap<>();

    @SubscribeEvent
    public static void onJoin (PlayerEvent.PlayerLoggedInEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        PixelSkills.playerConfigManager.loadPlayer(player.getUniqueID());
        Account account;
        if (!accountMap.containsKey(player.getUniqueID())) {

            account = new Account(player);
            account.load();

        }

    }

    @SubscribeEvent
    public static void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        Account account = accountMap.get(event.getPlayer().getUniqueID());
        if (account == null) return; // in the event a player joins the server and gets immediately kicked (due to like wrong Pixelmon mod or whatever) and this is not here, server crashes - don't remove it
        account.save();
        accountMap.entrySet().removeIf(entry -> entry.getKey().toString().equals(event.getPlayer().getUniqueID().toString()));

    }

}
