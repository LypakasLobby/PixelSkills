package com.lypaka.pixelskills.Listeners;

import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CaptureListener {

    @SubscribeEvent
    public void onCapture (CaptureEvent.SuccessfulCapture event) {

        ServerPlayerEntity player = event.getPlayer();
        PixelmonEntity pokemon = event.getPokemon();

        if (!ConfigGetters.isSkillEnabled("Collector")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Collector");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.collectorBlackList.contains(pokemon.getPokemonName())) return;

        double exp = skill.getSettings().getEXPPerTask();
        if (SkillGetters.collectorWhiteList.containsKey(pokemon.getPokemonName())) {

            exp = SkillGetters.collectorWhiteList.get(pokemon.getPokemonName());

        }

        if (pokemon.getPokemon().isShiny() && SkillGetters.collectorModifiers.containsKey("Shiny")) {

            exp = exp + SkillGetters.collectorModifiers.get("Shiny");

        } else if (SkillGetters.collectorModifiers.containsKey("Legendaries")) {

            if (pokemon.getSpecies().isLegendary() || pokemon.getSpecies().isMythical() || pokemon.getSpecies().isUltraBeast()) {

                exp = exp + SkillGetters.collectorModifiers.get("Legendaries");

            }

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
