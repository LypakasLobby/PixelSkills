package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.Skills.SkillHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
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
                                            SkillHandler.loadSkills();
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
