package com.lypaka.pixelskills.SkillRegistry;

import com.lypaka.pixelskills.PixelSkills;

public class SkillSettings {

    private final String displayName;
    private final double expPerTask;
    private final boolean multipleRewards;

    public SkillSettings (Skill skill) {

        this.displayName = PixelSkills.configManager.getConfigNode(0, "Skills", skill.getSkillName(), "Display-Name").getString();
        this.expPerTask = PixelSkills.configManager.getConfigNode(0, "Skills", skill.getSkillName(), "EXP-Per-Task").getDouble();
        this.multipleRewards = PixelSkills.configManager.getConfigNode(0, "Skills", skill.getSkillName(), "Multiple-Rewards").getBoolean();

    }

    public String getDisplayName() {

        return this.displayName;

    }

    public double getEXPPerTask() {

        return this.expPerTask;

    }

    public boolean allowsMultipleRewards() {

        return this.multipleRewards;

    }

}
