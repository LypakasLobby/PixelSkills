package com.lypaka.pixelskills.SkillRegistry;

import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Skill {

    private final String skillName;
    private final String accessPermission;
    private final BasicConfigManager bcm;
    private final SkillSettings settings;
    private List<SkillReward> rewards;

    public Skill (String skillName, String accessPermission, BasicConfigManager bcm) throws ObjectMappingException {

        this.skillName = skillName;
        this.accessPermission = accessPermission;
        this.bcm = bcm;
        this.settings = new SkillSettings(this);
        this.rewards = new ArrayList<>();
        Map<String, Map<String, String>> map = this.bcm.getConfigNode(2, "Rewards").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {

            String identifier = entry.getKey();
            SkillReward reward = new SkillReward(this, identifier);
            reward.create();
            this.rewards.add(reward);

        }

    }

    public String getSkillName() {

        return this.skillName;

    }

    public String getAccessPermission() {

        return this.accessPermission;

    }

    public BasicConfigManager getConfigManager() {

        return this.bcm;

    }

    public SkillSettings getSettings() {

        return this.settings;

    }

    public List<SkillReward> getRewards() {

        return this.rewards;

    }

}
