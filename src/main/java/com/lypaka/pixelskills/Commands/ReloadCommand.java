package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : PixelSkillsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("reload")
                                    .executes(c -> {

                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                            if (!PermissionHandler.hasPermission(player, "pixelskills.command.admin")) {

                                                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                return 0;

                                            }

                                        }

                                        try {

                                            PixelSkills.configManager.load();
                                            ConfigGetters.load();
                                            PixelSkills.skillConfigManager = new HashMap<>();
                                            for (String skillString : ConfigGetters.enabledSkills) {

                                                String lowerSkill = skillString.toLowerCase();
                                                String[] skillFiles = new String[]{lowerSkill + "Settings.conf", lowerSkill + "LevelUps.conf", lowerSkill + "Rewards.conf"};
                                                Path skillDir = ConfigUtils.checkDir(PixelSkills.dir.resolve(skillString));
                                                BasicConfigManager bcm = new BasicConfigManager(skillFiles, skillDir, PixelSkills.class, PixelSkills.MOD_NAME, PixelSkills.MOD_ID, PixelSkills.logger);
                                                bcm.init();
                                                Skill skill = new Skill(skillString, PixelSkills.configManager.getConfigNode(0, "Skills", skillString, "Access-Permission").getString(), bcm);
                                                PixelSkills.skillConfigManager.put(skillString, skill);

                                            }

                                            SkillGetters.load();
                                            c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully reloaded PixelSkills configuration!"), true);

                                        } catch (ObjectMappingException | IOException e) {

                                            e.printStackTrace();

                                        }

                                        return 1;

                                    })
                            )
            );

        }

    }

}
