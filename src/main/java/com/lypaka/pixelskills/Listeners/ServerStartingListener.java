package com.lypaka.pixelskills.Listeners;

import com.lypaka.pixelskills.PixelSkills;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = PixelSkills.MOD_ID)
public class ServerStartingListener {

    @SubscribeEvent
    public static void onServerStarting (FMLServerStartingEvent event) {

        Pixelmon.EVENT_BUS.register(new HatchEggListener());
        Pixelmon.EVENT_BUS.register(new CaptureListener());
        Pixelmon.EVENT_BUS.register(new EvolutionListener());
        Pixelmon.EVENT_BUS.register(new PixelmonFishListener());
        Pixelmon.EVENT_BUS.register(new NPCDefeatListener());
        Pixelmon.EVENT_BUS.register(new PokemonDefeatListener());

    }

}
