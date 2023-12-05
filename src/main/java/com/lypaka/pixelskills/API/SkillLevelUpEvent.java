package com.lypaka.pixelskills.API;

import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a player is set to level up a skill to its next level
 */
public class SkillLevelUpEvent extends Event {

    private final ServerPlayerEntity player;
    private final Skill skill;
    private final int startingLevel;
    private final int endingLevel;

    public SkillLevelUpEvent (ServerPlayerEntity player, Skill skill, int startingLevel, int endingLevel) {

        this.player = player;
        this.skill = skill;
        this.startingLevel = startingLevel;
        this.endingLevel = endingLevel;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Skill getSkill() {

        return this.skill;

    }

    public int getStartingLevel() {

        return this.startingLevel;

    }

    public int getEndingLevel() {

        return this.endingLevel;

    }

    public int getLevelsIncreased() {

        return this.endingLevel - this.startingLevel;

    }

}
