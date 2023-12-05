package com.lypaka.pixelskills.API;

import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.lypaka.pixelskills.SkillRegistry.SkillReward;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * Called when a player is about to be rewarded with skill rewards
 * Supports external customization via use of the setCommands() method
 */
@Cancelable
public class SkillRewardEvent extends Event {

    private final ServerPlayerEntity player;
    private final Skill skill;
    private final SkillReward reward;
    private List<String> commands;

    public SkillRewardEvent (ServerPlayerEntity player, Skill skill, SkillReward reward) {

        this.player = player;
        this.skill = skill;
        this.reward = reward;
        this.commands = reward.getCommands();

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Skill getSkill() {

        return this.skill;

    }

    public SkillReward getReward() {

        return this.reward;

    }

    public List<String> getCommands() {

        return this.commands;

    }

    public void setCommands (List<String> commands) {

        this.commands = commands;

    }

}
