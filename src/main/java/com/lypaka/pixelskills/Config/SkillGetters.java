package com.lypaka.pixelskills.Config;

import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.pixelskills.PixelSkills;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillGetters {

    /** Breeder */
    public static List<String> hatchingBlacklist;
    public static List<String> makingOffspringBlacklist;
    public static List<String> makingParentBlacklist;
    public static Map<String, Double> breederHatchingModifiers;
    public static double breederMakingModifier;
    public static Map<String, Double> breederLevelUpMap = new HashMap<>();

    /** Collector */
    public static List<String> collectorBlackList;
    public static Map<String, Double> collectorModifiers;
    public static Map<String, Double> collectorWhiteList;
    public static Map<String, Double> collectorLevelUpMap = new HashMap<>();;

    /** Darwinist */
    public static List<String> darwinistBlacklist;
    public static double darwinistShinyModifier;
    public static Map<String, Double> darwinistLevelUpMap = new HashMap<>();;

    /** Fisherman */
    public static List<String> fishermanBlacklist;
    public static Map<String, Double> fishermanRodModifiers;
    public static double fishermanShinyModifier;
    public static Map<String, Double> fishermanWhitelist;
    public static Map<String, Double> fishermanLevelUpMap = new HashMap<>();;

    /** Gladiator */
    public static Map<String, Double> gladiatorPokemonModifiers;
    public static List<String> gladiatorNPCBlacklist;
    public static List<String> gladiatorPokemonBlacklist;
    public static Map<String, Boolean> gladiatorTaskMap;
    public static Map<String, Double> gladiatorLevelUpMap = new HashMap<>();;

    public static Map<String, Map<String, Double>> skillLevelUpMaps = new HashMap<>();

    public static void load() throws ObjectMappingException {

        skillLevelUpMaps = new HashMap<>();

        BasicConfigManager bcm;
        /** Breeder */
        if (PixelSkills.skillConfigManager.containsKey("Breeder")) {

            bcm = PixelSkills.skillConfigManager.get("Breeder").getConfigManager();
            hatchingBlacklist = bcm.getConfigNode(0, "Blacklists", "Hatching").getList(TypeToken.of(String.class));
            makingOffspringBlacklist = bcm.getConfigNode(0, "Blacklists", "Making", "Offspring").getList(TypeToken.of(String.class));
            makingParentBlacklist = bcm.getConfigNode(0, "Blacklists", "Making", "Parents").getList(TypeToken.of(String.class));
            breederHatchingModifiers = bcm.getConfigNode(0, "Modifiers", "Hatching").getValue(new TypeToken<Map<String, Double>>() {});
            breederMakingModifier = bcm.getConfigNode(0, "Modifiers", "Making").getDouble();
            breederLevelUpMap = bcm.getConfigNode(1, "Levels").getValue(new TypeToken<Map<String, Double>>() {});

        }

        /** Collector */
        if (PixelSkills.skillConfigManager.containsKey("Collector")) {

            bcm = PixelSkills.skillConfigManager.get("Collector").getConfigManager();
            collectorBlackList = bcm.getConfigNode(0, "Blacklist").getList(TypeToken.of(String.class));
            collectorModifiers = bcm.getConfigNode(0, "Modifiers").getValue(new TypeToken<Map<String, Double>>() {});
            collectorWhiteList = bcm.getConfigNode(0, "Whitelist").getValue(new TypeToken<Map<String, Double>>() {});
            collectorLevelUpMap = bcm.getConfigNode(1, "Levels").getValue(new TypeToken<Map<String, Double>>() {});

        }

        /** Darwinist */
        if (PixelSkills.skillConfigManager.containsKey("Darwinist")) {

            bcm = PixelSkills.skillConfigManager.get("Darwinist").getConfigManager();
            darwinistBlacklist = bcm.getConfigNode(0, "Blacklist").getList(TypeToken.of(String.class));
            darwinistShinyModifier = bcm.getConfigNode(0, "Shiny-Modifier").getDouble();
            darwinistLevelUpMap = bcm.getConfigNode(1, "Levels").getValue(new TypeToken<Map<String, Double>>() {});

        }

        /** Fisherman */
        if (PixelSkills.skillConfigManager.containsKey("Fisherman")) {

            bcm = PixelSkills.skillConfigManager.get("Fisherman").getConfigManager();
            fishermanBlacklist = bcm.getConfigNode(0, "Blacklist").getList(TypeToken.of(String.class));
            fishermanRodModifiers = bcm.getConfigNode(0, "Rod-Modifiers").getValue(new TypeToken<Map<String, Double>>() {});
            fishermanShinyModifier = bcm.getConfigNode(0, "Shiny-Modifier").getDouble();
            fishermanWhitelist = bcm.getConfigNode(0, "Whitelist").getValue(new TypeToken<Map<String, Double>>() {});
            fishermanLevelUpMap = bcm.getConfigNode(1, "Levels").getValue(new TypeToken<Map<String, Double>>() {});

        }

        /** Gladiator */
        if (PixelSkills.skillConfigManager.containsKey("Gladiator")) {

            bcm = PixelSkills.skillConfigManager.get("Gladiator").getConfigManager();
            gladiatorPokemonModifiers = bcm.getConfigNode(0, "Modifiers").getValue(new TypeToken<Map<String, Double>>() {});
            gladiatorNPCBlacklist = bcm.getConfigNode(0, "NPC-Blacklist").getList(TypeToken.of(String.class));
            gladiatorPokemonBlacklist = bcm.getConfigNode(0, "Pokemon-Blacklist").getList(TypeToken.of(String.class));
            gladiatorTaskMap = bcm.getConfigNode(0, "Tasks").getValue(new TypeToken<Map<String, Boolean>>() {});
            gladiatorLevelUpMap = bcm.getConfigNode(1, "Levels").getValue(new TypeToken<Map<String, Double>>() {});

        }

        skillLevelUpMaps.put("Breeder", breederLevelUpMap);
        skillLevelUpMaps.put("Collector", collectorLevelUpMap);
        skillLevelUpMaps.put("Darwinist", darwinistLevelUpMap);
        skillLevelUpMaps.put("Fisherman", fishermanLevelUpMap);
        skillLevelUpMaps.put("Gladiator", gladiatorLevelUpMap);

    }

}
