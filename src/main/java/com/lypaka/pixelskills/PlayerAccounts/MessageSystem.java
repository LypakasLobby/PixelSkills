package com.lypaka.pixelskills.PlayerAccounts;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.entity.player.ServerPlayerEntity;

public class MessageSystem {

    public static void sendChatEXP (ServerPlayerEntity player, Skill skill, double exp) {

        player.sendMessage(FancyText.getFormattedText(ConfigGetters.chatEXP
                .replace("%skill%", skill.getSettings().getDisplayName())
                .replace("%exp%", String.valueOf(exp))
        ), player.getUniqueID());

    }

    public static void sendChatLevelUp (ServerPlayerEntity player, Skill skill, int level) {

        player.sendMessage(FancyText.getFormattedText(ConfigGetters.chatLevelUp
                .replace("%skill%", skill.getSettings().getDisplayName())
                .replace("%level%", String.valueOf(level))
        ), player.getUniqueID());

    }

}
