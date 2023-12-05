package com.lypaka.pixelskills.Commands;

import com.lypaka.pixelskills.PixelSkills;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = PixelSkills.MOD_ID)
public class PixelSkillsCommand {

    public static List<String> ALIASES = Arrays.asList("pixelskills", "pskills", "skills");

    @SubscribeEvent
    public static void onCommandRegister (RegisterCommandsEvent event) {

        new MenuCommand(event.getDispatcher());
        new MessageCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        new SetLevelCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
