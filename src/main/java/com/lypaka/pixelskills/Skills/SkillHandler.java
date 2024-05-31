package com.lypaka.pixelskills.Skills;

import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillHandler {

    public static Map<String, BasicConfigManager> skillBCMs;

    public static void loadSkills() throws IOException, ObjectMappingException {

        skillBCMs = new HashMap<>();
        Path dir = ConfigUtils.checkDir(Paths.get("./config/pixelskills"));
        for (Map.Entry<String, Boolean> skills : ConfigGetters.skills.entrySet()) {

            String skillID = skills.getKey();
            boolean enabled = skills.getValue();
            if (enabled) {

                String lowerSkill = skillID.toLowerCase();
                String[] skillFiles = new String[]{lowerSkill + "Settings.conf", lowerSkill + "LevelUps.conf", lowerSkill + "Rewards.conf"};
                Path skillDir = ConfigUtils.checkDir(dir.resolve(skillID));
                BasicConfigManager bcm = new BasicConfigManager(skillFiles, skillDir, PixelSkills.class, PixelSkills.MOD_NAME, PixelSkills.MOD_ID, PixelSkills.logger);
                bcm.init();
                skillBCMs.put(skillID, bcm);

                String accessPermission = PixelSkills.configManager.getConfigNode(0, "Skills", skillID, "Access-Permission").getString();
                String displayName = PixelSkills.configManager.getConfigNode(0, "Skills", skillID, "Display-Name").getString();
                int expPerTask = PixelSkills.configManager.getConfigNode(0, "Skills", skillID, "EXP-Per-Task").getInt();
                boolean multipleRewards = PixelSkills.configManager.getConfigNode(0, "Skills", skillID, "Multiple-Rewards").getBoolean();

                List<SkillReward> rewards = new ArrayList<>();
                Map<String, Map<String, String>> map = bcm.getConfigNode(2, "Rewards").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {

                    String rewardID = entry.getKey();
                    double expChance = bcm.getConfigNode(2, "Rewards", rewardID, "Access", "EXP").getDouble();
                    double levelUpChance = bcm.getConfigNode(2, "Rewards", rewardID, "Access", "Level-Up", "Chance").getDouble();
                    List<Integer> guaranteedLevels = bcm.getConfigNode(2, "Rewards", rewardID, "Access", "Level-Up", "Guaranteed-Levels").getList(TypeToken.of(Integer.class));
                    List<String> commands = bcm.getConfigNode(2, "Rewards", rewardID, "Commands").getList(TypeToken.of(String.class));
                    List<String> levelRequirements = bcm.getConfigNode(2, "Rewards", rewardID, "Requirements", "Levels").getList(TypeToken.of(String.class));
                    List<String> permissionRequirements = bcm.getConfigNode(2, "Rewards", rewardID, "Requirements", "Permissions").getList(TypeToken.of(String.class));
                    SkillReward skillReward = new SkillReward(rewardID, expChance, levelUpChance, guaranteedLevels, commands, levelRequirements, permissionRequirements);
                    rewards.add(skillReward);

                }

                Skill skill = new Skill(skillID, accessPermission, displayName, expPerTask, multipleRewards, rewards);
                PixelSkills.skillConfigManager.put(skillID, skill);

            }

        }

        SkillGetters.load();

    }

}
