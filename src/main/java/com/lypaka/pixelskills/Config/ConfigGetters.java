package com.lypaka.pixelskills.Config;

import com.google.common.reflect.TypeToken;
import com.lypaka.pixelskills.PixelSkills;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static List<String> enabledSkills = new ArrayList<>();

    public static String bossBarColor;
    public static String bossBarEXP;
    public static String bossBarLevelUp;
    public static String chatEXP;
    public static String chatLevelUp;
    public static String defaultMessageSystem;
    public static ArrayList<String> chatList = new ArrayList<>();
    public static ArrayList<String> noneList = new ArrayList<>();

    public static int guiRows;
    public static String displayTitle;
    public static String borderID;
    public static List<Integer> borderSlots;
    public static Map<String, Map<String, String>> skillSlots;

    public static void load() throws ObjectMappingException {

        Map<String, Map<String, String>> skillsMap = PixelSkills.configManager.getConfigNode(0, "Skills").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        for (Map.Entry<String, Map<String, String>> entry : skillsMap.entrySet()) {

            boolean enabled = Boolean.parseBoolean(entry.getValue().get("Enabled"));
            if (enabled) {

                String skillName = entry.getKey();
                enabledSkills.add(skillName);

            }

        }

        bossBarColor = PixelSkills.configManager.getConfigNode(1, "Messages", "Boss-Bar-Color").getString();
        bossBarEXP = PixelSkills.configManager.getConfigNode(1, "Messages", "Boss-Bar-EXP").getString();
        bossBarLevelUp = PixelSkills.configManager.getConfigNode(1, "Messages", "Boss-Bar-Level-Up").getString();
        chatEXP = PixelSkills.configManager.getConfigNode(1, "Messages", "Chat-EXP").getString();
        chatLevelUp = PixelSkills.configManager.getConfigNode(1, "Messages", "Chat-Level-Up").getString();
        defaultMessageSystem = PixelSkills.configManager.getConfigNode(1, "Messages", "Default-Message-System").getString();
        chatList = new ArrayList<>(PixelSkills.configManager.getConfigNode(1, "Storage", "Chat").getList(TypeToken.of(String.class)));
        noneList = new ArrayList<>(PixelSkills.configManager.getConfigNode(1, "Storage", "None").getList(TypeToken.of(String.class)));

        guiRows = PixelSkills.configManager.getConfigNode(2, "Amount").getInt();
        displayTitle = PixelSkills.configManager.getConfigNode(2, "Display-Title").getString();
        borderID = PixelSkills.configManager.getConfigNode(2, "GUI-Border", "ID").getString();
        borderSlots = PixelSkills.configManager.getConfigNode(2, "GUI-Border", "Slots").getList(TypeToken.of(Integer.class));
        skillSlots = PixelSkills.configManager.getConfigNode(2, "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }

    public static boolean isSkillEnabled (String skill) {

        return enabledSkills.contains(skill);

    }

}
