package com.lypaka.pixelskills.Skills;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class Skill {

    private final String skillID;
    private final String accessPermission;
    private final String displayName;
    private final double expPerTask;
    private final boolean multipleRewards;
    private final List<SkillReward> rewards;

    public Skill (String skillID, String accessPermission, String displayName, double expPerTask, boolean multipleRewards, List<SkillReward> rewards) throws ObjectMappingException {

        this.skillID = skillID;
        this.accessPermission = accessPermission;
        this.displayName = displayName;
        this.expPerTask = expPerTask;
        this.multipleRewards = multipleRewards;
        this.rewards = rewards;

    }

    public String getSkillID() {

        return this.skillID;

    }

    public String getAccessPermission() {

        return this.accessPermission;

    }

    public String getDisplayName() {

        return this.displayName;

    }

    public double getEXPPerTask() {

        return this.expPerTask;

    }

    public boolean doesMultipleRewards() {

        return this.multipleRewards;

    }

    public List<SkillReward> getRewards() {

        return this.rewards;

    }

}
