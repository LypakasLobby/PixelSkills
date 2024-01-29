package com.lypaka.pixelskills.Listeners;

import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.Skills.Skill;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class NPCDefeatListener {

    @SubscribeEvent
    public void onNPCDefeat (BeatTrainerEvent event) {

        ServerPlayerEntity player = event.player;
        NPCTrainer trainer = event.trainer;
        String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
        int x = trainer.getPosition().getX();
        int y = trainer.getPosition().getY();
        int z = trainer.getPosition().getZ();
        String location = worldName + "," + x + "," + y + "," + z;

        if (!ConfigGetters.isSkillEnabled("Gladiator")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Gladiator");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        Map<String, Boolean> map = SkillGetters.gladiatorTaskMap;
        if (map.containsKey("Defeating-NPC-Trainers")) {

            if (!map.get("Defeating-NPC-Trainers")) return;

        }

        if (!SkillGetters.gladiatorNPCBlacklist.isEmpty()) {

            if (SkillGetters.gladiatorNPCBlacklist.contains(location)) return;

        }

        double exp = skill.getEXPPerTask();
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
