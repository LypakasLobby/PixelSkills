package com.lypaka.pixelskills;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.ConfigurationLoaders.PlayerConfigManager;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pixelskills")
public class PixelSkills {

    public static final String MOD_ID = "pixelskills";
    public static final String MOD_NAME = "PixelSkills";
    public static final String VERSION = "5.1.2";
    public static final Logger logger = LogManager.getLogger();
    public static BasicConfigManager configManager;
    public static PlayerConfigManager playerConfigManager;
    public static Map<String, Skill> skillConfigManager = new HashMap<>();
    public static Path dir;
    public static List<String> alolans = new ArrayList<>();
    public static List<String> galarians = new ArrayList<>();
    public static List<String> hisuians = new ArrayList<>();

    public PixelSkills() throws IOException, ObjectMappingException {

        dir = ConfigUtils.checkDir(Paths.get("./config/pixelskills"));
        String[] files = new String[]{"skills.conf", "messages.conf", "gui.conf"};
        configManager = new BasicConfigManager(files, dir, PixelSkills.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        playerConfigManager = new PlayerConfigManager("account.conf", "player-accounts", dir, PixelSkills.class, MOD_NAME, MOD_ID, logger);
        playerConfigManager.init();
        ConfigGetters.load();
        for (String skillString : ConfigGetters.enabledSkills) {

            String lowerSkill = skillString.toLowerCase();
            String[] skillFiles = new String[]{lowerSkill + "Settings.conf", lowerSkill + "LevelUps.conf", lowerSkill + "Rewards.conf"};
            Path skillDir = ConfigUtils.checkDir(dir.resolve(skillString));
            BasicConfigManager bcm = new BasicConfigManager(skillFiles, skillDir, PixelSkills.class, MOD_NAME, MOD_ID, logger);
            bcm.init();
            Skill skill = new Skill(skillString, configManager.getConfigNode(0, "Skills", skillString, "Access-Permission").getString(), bcm);
            skillConfigManager.put(skillString, skill);

        }

        SkillGetters.load();
        loadRegionalLists();

    }

    private static void loadRegionalLists() {

        alolans.add("Rattata");
        alolans.add("Raticate");
        alolans.add("Raichu");
        alolans.add("Sandshrew");
        alolans.add("Sandslash");
        alolans.add("Vulpix");
        alolans.add("Ninetales");
        alolans.add("Diglett");
        alolans.add("Dugtrio");
        alolans.add("Meowth");
        alolans.add("Persian");
        alolans.add("Geodude");
        alolans.add("Graveler");
        alolans.add("Golem");
        alolans.add("Grimer");
        alolans.add("Muk");
        alolans.add("Exeggutor");
        alolans.add("Marowak");

        galarians.add("Meowth");
        galarians.add("Ponyta");
        galarians.add("Rapidash");
        galarians.add("Slowpoke");
        galarians.add("Slowbro");
        galarians.add("Farfetchd");
        galarians.add("Weezing");
        galarians.add("MrMime");
        galarians.add("Articuno");
        galarians.add("Zapdos");
        galarians.add("Moltres");
        galarians.add("Slowking");
        galarians.add("Corsola");
        galarians.add("Zigzagoon");
        galarians.add("Linoone");
        galarians.add("Darumaka");
        galarians.add("Darmanitan");
        galarians.add("Yamask");
        galarians.add("Stunfisk");

        hisuians.add("Growlithe");
        hisuians.add("Arcanine");
        hisuians.add("Voltorb");
        hisuians.add("Electrode");
        hisuians.add("Typhlosion");
        hisuians.add("Qwilfish");
        hisuians.add("Sneasel");
        hisuians.add("Samurott");
        hisuians.add("Lilligant");
        hisuians.add("Zorua");
        hisuians.add("Zoroark");
        hisuians.add("Braviary");
        hisuians.add("Sliggoo");
        hisuians.add("Goodra");
        hisuians.add("Avalugg");
        hisuians.add("Decidueye");

    }

}
