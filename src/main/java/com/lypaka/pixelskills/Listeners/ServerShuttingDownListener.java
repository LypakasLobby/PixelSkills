package com.lypaka.pixelskills.Listeners;

import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.PixelSkills;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

@Mod.EventBusSubscriber(modid = PixelSkills.MOD_ID)
public class ServerShuttingDownListener {

    @SubscribeEvent
    public static void onShutDown (FMLServerStoppingEvent event) {

        PixelSkills.configManager.getConfigNode(1, "Storage", "Chat").setValue(ConfigGetters.chatList);
        PixelSkills.configManager.getConfigNode(1, "Storage", "None").setValue(ConfigGetters.noneList);
        PixelSkills.configManager.save();

    }

}
