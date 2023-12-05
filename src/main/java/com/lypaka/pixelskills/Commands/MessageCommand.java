package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;

public class MessageCommand {

    public MessageCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : PixelSkillsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("message")
                                    .then(Commands.argument("value", StringArgumentType.string())
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "pixelskills.command.message")) {

                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                        return 0;

                                                    }
                                                    String value = StringArgumentType.getString(c, "value");
                                                    if (value.equalsIgnoreCase("off") || value.equals("chat")) {

                                                        ArrayList<String> listToAddTo;
                                                        String type;
                                                        if (value.equalsIgnoreCase("chat")) {

                                                            listToAddTo = ConfigGetters.chatList;
                                                            type = "chat";

                                                        } else {

                                                            listToAddTo = ConfigGetters.noneList;
                                                            type = "disabled";

                                                        }

                                                        if (!listToAddTo.contains(player.getUniqueID().toString())) {

                                                            removeFromOtherListIfThere(player.getUniqueID().toString(), type);
                                                            listToAddTo.add(player.getUniqueID().toString());
                                                            player.sendMessage(FancyText.getFormattedText("&aSuccessfully updated your chat system to the &e" + type + " &amodule!"), player.getUniqueID());

                                                        } else {

                                                            player.sendMessage(FancyText.getFormattedText("&cYou're already added to this list!"), player.getUniqueID());

                                                        }

                                                    }

                                                }

                                                return 1;

                                            })
                                    )
                            )
            );

        }

    }

    private void removeFromOtherListIfThere (String uuid, String moduleUsed) {

        ArrayList<String> list;

        if (moduleUsed.equals("chat")) {

            list = ConfigGetters.noneList;

        } else {

            list = ConfigGetters.chatList;

        }

        list.removeIf(entry -> entry.equalsIgnoreCase(uuid));

    }

}
