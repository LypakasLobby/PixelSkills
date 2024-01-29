package com.lypaka.pixelskills.Skills;

import java.util.List;

public class SkillReward {

    private final String rewardIdentifier;
    private final double expChance;
    private final double levelUpChance;
    private final List<Integer> guaranteedLevels;
    private final List<String> commands;
    private final List<String> levelRequirements;
    private final List<String> permissionRequirements;


    public SkillReward (String rewardIdentifier, double expChance, double levelUpChance, List<Integer> guaranteedLevels, List<String> commands,
                        List<String> levelRequirements, List<String> permissionRequirements) {

        this.rewardIdentifier = rewardIdentifier;
        this.expChance = expChance;
        this.levelUpChance = levelUpChance;
        this.guaranteedLevels = guaranteedLevels;
        this.commands = commands;
        this.levelRequirements = levelRequirements;
        this.permissionRequirements = permissionRequirements;

    }

    public String getRewardIdentifier() {

        return this.rewardIdentifier;

    }

    public double getEXPChance() {

        return this.expChance;

    }

    public double getLevelUpChance() {

        return this.levelUpChance;

    }

    public List<Integer> getGuaranteedLevels() {

        return this.guaranteedLevels;

    }

    public List<String> getCommands() {

        return this.commands;

    }

    public List<String> getLevelRequirements() {

        return this.levelRequirements;

    }

    public List<String> getPermissionRequirements() {

        return this.permissionRequirements;

    }

}
