package com.lypaka.pixelskills.PlayerAccounts;

import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.API.SkillLevelUpEvent;
import com.lypaka.pixelskills.API.SkillRewardEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.lypaka.pixelskills.SkillRegistry.SkillReward;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {

    private final ServerPlayerEntity player;
    private final Map<String, Map<String, String>> infoMap;

    public Account (ServerPlayerEntity player) {

        this.player = player;
        this.infoMap = new HashMap<>();

    }

    public void load() {

        for (Map.Entry<String, Skill> entry : PixelSkills.skillConfigManager.entrySet()) {

            String skillName = entry.getKey();
            Map<String, String> miniMap = new HashMap<>();
            if (PixelSkills.playerConfigManager.getPlayerConfigNode(this.player.getUniqueID(), "Skill-Info", skillName).isVirtual()) {

                miniMap.put("Level", "1");
                miniMap.put("EXP", "0.0");

            } else {

                String level = PixelSkills.playerConfigManager.getPlayerConfigNode(this.player.getUniqueID(), "Skill-Info", skillName, "Level").getString();
                String exp = PixelSkills.playerConfigManager.getPlayerConfigNode(this.player.getUniqueID(), "Skill-Info", skillName, "EXP").getString();
                miniMap.put("Level", level);
                miniMap.put("EXP", exp);

            }

            infoMap.put(skillName, miniMap);

        }

        JoinListener.accountMap.put(this.player.getUniqueID(), this);

    }

    public Map<String, String> getSkillInfo (String skill) {

        Map<String, String> map = new HashMap<>();
        if (this.infoMap.containsKey(skill)) {

            map = this.infoMap.get(skill);

        }

        return map;

    }

    public int getLevel (String skillName) {

        int level = 0;
        if (this.infoMap.containsKey(skillName)) {

            level = Integer.parseInt(this.infoMap.get(skillName).get("Level"));

        }

        return level;

    }

    public int getLevel (Skill skill) {

        int level = 0;
        if (this.infoMap.containsKey(skill.getSkillName())) {

            level = Integer.parseInt(this.infoMap.get(skill.getSkillName()).get("Level"));

        }

        return level;

    }

    public void setLevel (Skill skill, int level) {

        this.infoMap.get(skill.getSkillName()).put("Level", String.valueOf(level));

    }

    public double getEXP (String skillName) {

        double exp = 0;
        if (this.infoMap.containsKey(skillName)) {

            exp = Double.parseDouble(this.infoMap.get(skillName).get("EXP"));

        }

        return exp;

    }

    public double getEXP (Skill skill) {

        double exp = 0;
        if (this.infoMap.containsKey(skill.getSkillName())) {

            exp = Double.parseDouble(this.infoMap.get(skill.getSkillName()).get("EXP"));

        }

        return exp;

    }

    public void setEXP (Skill skill, double exp) {

        this.infoMap.get(skill.getSkillName()).put("EXP", String.valueOf(exp));

    }

    public void awardEXP (Skill skill, double gained) {

        double current = 0;
        String skillName = skill.getSkillName();
        Map<String, String> skillInfoMap = new HashMap<>();
        if (this.infoMap.containsKey(skillName)) {

            skillInfoMap = this.getSkillInfo(skillName);
            current = Double.parseDouble(skillInfoMap.get("EXP"));

        }
        double updated = current + gained;

        // With our new current value, we check for level up
        int currentLevel = Integer.parseInt(skillInfoMap.get("Level"));
        int nextLevel = currentLevel + 1;
        Map<String, Double> levelUpMap = SkillGetters.skillLevelUpMaps.get(skillName);
        int maxLevel = levelUpMap.size() + 1; // +1 because we don't start at level 1 so we need to account for it (i.e. 2-50 is 49, so +1 for max level of 50)
        if (levelUpMap.containsKey("Level-" + nextLevel)) {

            double needed = levelUpMap.get("Level-" + nextLevel);
            int levelUps = 0;
            if (updated == needed) {

                // 1 level up
                levelUps = 1;

            } else if (updated > needed) {

                boolean isMax = false;
                do {

                    levelUps++;
                    nextLevel = nextLevel + 1;
                    if (levelUpMap.containsKey("Level-" + nextLevel)) {

                        needed = levelUpMap.get("Level-" + nextLevel);

                    } else {

                        // player has hit max level so set updated to 0 so it breaks out of the do/while loop
                        updated = 0;
                        isMax = true;

                    }

                } while (needed < updated);

                if (isMax) {

                    // now we reset updated to value of max skill in case user adds more levels later on
                    updated = levelUpMap.get("Level-" + maxLevel);

                }

            }

            DecimalFormat df = new DecimalFormat("#.##");
            skillInfoMap.put("EXP", String.valueOf(df.format(updated)));
            if (levelUps == 0) {

                if (!ConfigGetters.defaultMessageSystem.equalsIgnoreCase("disabled")) {

                    if (ConfigGetters.chatList.contains(this.player.getUniqueID().toString())) {

                        MessageSystem.sendChatEXP(this.player, skill, gained);

                    } else if (!ConfigGetters.noneList.contains(this.player.getUniqueID().toString())) {

                        if (ConfigGetters.defaultMessageSystem.equalsIgnoreCase("chat")) {

                            MessageSystem.sendChatEXP(this.player, skill, gained);

                        }

                    }

                }
                attemptEXPReward(skill, currentLevel);

            } else {

                int finalizedLevel = Math.min(currentLevel + levelUps, maxLevel);
                SkillLevelUpEvent event = new SkillLevelUpEvent(this.player, skill, currentLevel, finalizedLevel);
                MinecraftForge.EVENT_BUS.post(event);
                skillInfoMap.put("Level", String.valueOf(finalizedLevel));
                if (!ConfigGetters.defaultMessageSystem.equalsIgnoreCase("disabled")) {

                    if (ConfigGetters.chatList.contains(this.player.getUniqueID().toString())) {

                        MessageSystem.sendChatLevelUp(this.player, skill, finalizedLevel);

                    } else if (!ConfigGetters.noneList.contains(this.player.getUniqueID().toString())) {

                        if (ConfigGetters.defaultMessageSystem.equalsIgnoreCase("chat")) {

                            MessageSystem.sendChatLevelUp(this.player, skill, finalizedLevel);

                        }

                    }

                }

                attemptLevelUpReward(skill, currentLevel, finalizedLevel);

            }

        }

        this.infoMap.put(skillName, skillInfoMap);

    }

    public void attemptEXPReward (Skill skill, int playerLevel) {

        SkillReward reward = null;
        boolean multiple = skill.getSettings().allowsMultipleRewards();
        List<SkillReward> rewardsToGive = new ArrayList<>();
        for (SkillReward sr : skill.getRewards()) {

            boolean passesRequirements = true;
            if (!sr.getLevelRequirements().isEmpty()) {

                for (String levelRequirement : sr.getLevelRequirements()) {

                    if (!passesRequirements) break;
                    String[] split = levelRequirement.split(" ");
                    String operator = split[0];
                    int level = Integer.parseInt(split[1]);
                    if (operator.equals(">")) {

                        if (playerLevel <= level) {

                            passesRequirements = false;

                        }

                    } else if (operator.equals("<")) {

                        if (playerLevel >= level) {

                            passesRequirements = false;

                        }

                    }

                }

            }
            if (passesRequirements) {

                if (!sr.getPermissionRequirements().isEmpty()) {

                    for (String permission : sr.getPermissionRequirements()) {

                        if (!passesRequirements) break;
                        if (!PermissionHandler.hasPermission(player, permission)) {

                            passesRequirements = false;

                        }

                    }

                }

            }
            if (!passesRequirements) continue;
            double chance = sr.getEXPChance();
            if (RandomHelper.getRandomChance(chance)) {

                reward = sr;
                if (multiple) {

                    rewardsToGive.add(reward);

                } else {

                    break;

                }

            }

        }
        if (multiple) {

            if (rewardsToGive.size() > 0) {

                for (SkillReward given : rewardsToGive) {

                    SkillRewardEvent event = new SkillRewardEvent(this.player, skill, given);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled()) {

                        for (String command : event.getCommands()) {

                            this.player.world.getServer().getCommandManager().handleCommand(
                                    this.player.world.getServer().getCommandSource(),
                                    command.replace("%player%", this.player.getName().getString())
                            );

                        }

                    }

                }

            }

        } else {

            if (reward != null) {

                SkillRewardEvent event = new SkillRewardEvent(this.player, skill, reward);
                MinecraftForge.EVENT_BUS.post(event);
                if (!event.isCanceled()) {

                    for (String command : event.getCommands()) {

                        this.player.world.getServer().getCommandManager().handleCommand(
                                this.player.world.getServer().getCommandSource(),
                                command.replace("%player%", this.player.getName().getString())
                        );

                    }

                }

            }

        }

    }

    public void attemptLevelUpReward (Skill skill, int startingLevel, int endingLevel) {

        SkillReward reward = null;
        int difference = endingLevel - startingLevel;
        boolean multiple = skill.getSettings().allowsMultipleRewards();
        List<SkillReward> rewardsToGive = new ArrayList<>();
        for (SkillReward sr : skill.getRewards()) {

            boolean guaranteed = false;
            List<Integer> guaranteedLevels = sr.getGuaranteedLevels();
            int lvlCount = 0;
            if (difference > 1) {

                int begin = startingLevel + 1;
                for (int i = begin; i <= endingLevel; i++) {

                    if (guaranteedLevels.contains(i)) {

                        guaranteed = true;
                        lvlCount++;

                    }

                }

            } else if (difference == 1) {

                if (guaranteedLevels.contains(endingLevel)) {

                    guaranteed = true;

                }

            }
            boolean passesRequirements = true;
            if (!guaranteed) {

                if (!sr.getLevelRequirements().isEmpty()) {

                    for (String levelRequirement : sr.getLevelRequirements()) {

                        if (!passesRequirements) break;
                        String[] split = levelRequirement.split(" ");
                        String operator = split[0];
                        int level = Integer.parseInt(split[1]);
                        if (operator.equals(">")) {

                            if (endingLevel <= level) {

                                passesRequirements = false;

                            }

                        } else if (operator.equals("<")) {

                            if (endingLevel >= level) {

                                passesRequirements = false;

                            }

                        }

                    }

                }
                if (passesRequirements) {

                    if (!sr.getPermissionRequirements().isEmpty()) {

                        for (String permission : sr.getPermissionRequirements()) {

                            if (!passesRequirements) break;
                            if (!PermissionHandler.hasPermission(player, permission)) {

                                passesRequirements = false;

                            }

                        }

                    }

                }

            }
            if (!passesRequirements) continue;
            double chance = sr.getLevelUpChance();
            if (RandomHelper.getRandomChance(chance) || guaranteed) {

                reward = sr;
                if (multiple) {

                    if (lvlCount > 0) {

                        for (int i = 0; i < lvlCount; i++) {

                            rewardsToGive.add(reward);

                        }

                    } else {

                        rewardsToGive.add(reward);

                    }

                }

            }

        }
        if (multiple) {

            if (rewardsToGive.size() > 0) {

                for (SkillReward given : rewardsToGive) {

                    SkillRewardEvent event = new SkillRewardEvent(this.player, skill, given);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled()) {

                        for (String command : event.getCommands()) {

                            this.player.world.getServer().getCommandManager().handleCommand(
                                    this.player.world.getServer().getCommandSource(),
                                    command.replace("%player%", this.player.getName().getString())
                            );

                        }

                    }

                }

            }

        } else {

            if (reward != null) {

                SkillRewardEvent event = new SkillRewardEvent(this.player, skill, reward);
                MinecraftForge.EVENT_BUS.post(event);
                if (!event.isCanceled()) {

                    for (String command : event.getCommands()) {

                        this.player.world.getServer().getCommandManager().handleCommand(
                                this.player.world.getServer().getCommandSource(),
                                command.replace("%player%", this.player.getName().getString())
                        );

                    }

                }

            }

        }

    }

    public void save() {

        try {

            PixelSkills.playerConfigManager.getPlayerConfigNode(this.player.getUniqueID(), "Skill-Info").setValue(this.infoMap);
            PixelSkills.playerConfigManager.savePlayer(this.player.getUniqueID());

        } catch (NullPointerException er) {

            PixelSkills.logger.error("Could not save account! Printing info...");
            PixelSkills.logger.error("Player: " + this.player);
            PixelSkills.logger.error("Info Map: " + this.infoMap);

        }

    }

}
