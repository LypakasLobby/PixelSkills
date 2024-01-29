package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.Skills.Skill;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Map;

public class SetLevelCommand {

    public SetLevelCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : PixelSkillsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("setlevel")
                                    .then(Commands.argument("player", EntityArgument.players())
                                            .then(Commands.argument("skill", StringArgumentType.string())
                                                    .then(Commands.argument("level", IntegerArgumentType.integer(1))
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    if (!PermissionHandler.hasPermission(player, "pixelskills.command.admin")) {

                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                        return 0;

                                                                    }

                                                                }

                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                String skillName = StringArgumentType.getString(c, "skill");
                                                                int level = IntegerArgumentType.getInteger(c, "level");
                                                                Skill skill = null;
                                                                for (Map.Entry<String, Skill> entry : PixelSkills.skillConfigManager.entrySet()) {

                                                                    if (skillName.equalsIgnoreCase(entry.getKey())) {

                                                                        skill = entry.getValue();
                                                                        break;

                                                                    }

                                                                }

                                                                if (skill == null) {

                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid skill name!"));
                                                                    return 0;

                                                                }

                                                                Account account = JoinListener.accountMap.get(target.getUniqueID());
                                                                int maxLevel = SkillGetters.skillLevelUpMaps.get(skill.getSkillID()).size();
                                                                if (level > maxLevel) {

                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cCannot set level higher than max level!"));
                                                                    return 0;

                                                                }
                                                                if (level <= 0) {

                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cMinimum level support is level 1!"));
                                                                    return 0;

                                                                }

                                                                int currentLevel = account.getLevel(skill);
                                                                if (level == currentLevel) {

                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cCannot set level to player's current level!"));
                                                                    return 0;

                                                                }

                                                                double neededEXP;
                                                                if (level == 1) {

                                                                    neededEXP = 0.0;

                                                                } else {

                                                                    neededEXP = SkillGetters.skillLevelUpMaps.get(skill.getSkillID()).get("Level-" + level);

                                                                }
                                                                account.setLevel(skill, level);
                                                                account.setEXP(skill, neededEXP);
                                                                c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully set " + target.getName() + "'s level in " + skill.getSkillID() + " to " + level + "!"), true);
                                                                target.sendMessage(FancyText.getFormattedText("&eYour level in " + skill.getSkillID() + " was set to " + level + "."), target.getUniqueID());
                                                                return 1;

                                                            })
                                                    )
                                            )
                                    )
                            )
            );

        }

    }

}
