package com.lypaka.pixelskills.Listeners;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.Skills.SkillHandler;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = PixelSkills.MOD_ID)
public class ServerStartingListener {

    @SubscribeEvent
    public static void onServerStarting (FMLServerStartingEvent event) throws IOException, ObjectMappingException {

        SkillHandler.loadSkills();

        Pixelmon.EVENT_BUS.register(new HatchEggListener());
        Pixelmon.EVENT_BUS.register(new CaptureListener());
        Pixelmon.EVENT_BUS.register(new EvolutionListener());
        Pixelmon.EVENT_BUS.register(new PixelmonFishListener());
        Pixelmon.EVENT_BUS.register(new NPCDefeatListener());
        Pixelmon.EVENT_BUS.register(new PokemonDefeatListener());

    }

}
