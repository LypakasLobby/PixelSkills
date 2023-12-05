package com.lypaka.pixelskills.SkillRegistry;

import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class SkillReward {

    private double expChance;
    private double levelUpChance;
    private List<Integer> guaranteedLevels;
    private List<String> commands;
    private List<String> levelRequirements;
    private List<String> permissionRequirements;
    private final Skill skill;
    private final String rewardIdentifier;

    public SkillReward (Skill skill, String rewardIdentifier) {

        this.skill = skill;
        this.rewardIdentifier = rewardIdentifier;

    }

    public void create() throws ObjectMappingException {

        BasicConfigManager bcm = this.skill.getConfigManager();
        this.expChance = bcm.getConfigNode(2, "Rewards", this.rewardIdentifier, "Access", "EXP").getDouble();
        this.levelUpChance = bcm.getConfigNode(2, "Rewards", this.rewardIdentifier, "Access", "Level-Up", "Chance").getDouble();
        this.guaranteedLevels = bcm.getConfigNode(2, "Rewards", this.rewardIdentifier, "Access", "Level-Up", "Guaranteed-Levels").getList(TypeToken.of(Integer.class));
        this.commands = bcm.getConfigNode(2, "Rewards", this.rewardIdentifier, "Commands").getList(TypeToken.of(String.class));
        this.levelRequirements = bcm.getConfigNode(2, "Rewards", this.rewardIdentifier, "Requirements", "Levels").getList(TypeToken.of(String.class));
        this.permissionRequirements = bcm.getConfigNode(2, "Rewards", this.rewardIdentifier, "Requirements", "Permissions").getList(TypeToken.of(String.class));

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
