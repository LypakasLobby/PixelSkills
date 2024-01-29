package com.lypaka.pixelskills.Listeners;

import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.Skills.Skill;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EvolutionListener {

    @SubscribeEvent
    public void onEvolve (EvolveEvent.Post event) {

        PixelmonEntity pokemon = event.getEntity();
        ServerPlayerEntity player = event.getPlayer();

        if (!ConfigGetters.isSkillEnabled("Darwinist")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Darwinist");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.darwinistBlacklist.contains(pokemon.getPokemonName())) return;

        double exp = skill.getEXPPerTask();
        if (SkillGetters.darwinistShinyModifier > 0) {

            exp = exp + SkillGetters.darwinistShinyModifier;

        }

        if (exp > 0) {

            SkillEXPEvent expEvent = new SkillEXPEvent(player, skill, exp);
            MinecraftForge.EVENT_BUS.post(expEvent);
            Account account = JoinListener.accountMap.get(player.getUniqueID());
            if (!expEvent.isCanceled()) {

                account.awardEXP(skill, expEvent.getEXP());

            }

        }

    }

}
