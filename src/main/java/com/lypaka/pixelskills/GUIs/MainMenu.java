package com.lypaka.pixelskills.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu {

    public static void open (ServerPlayerEntity player) throws ObjectMappingException {

        Account account = JoinListener.accountMap.get(player.getUniqueID());
        ChestTemplate template = ChestTemplate.builder(ConfigGetters.guiRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString(ConfigGetters.displayTitle))
                .build();

        for (int i : ConfigGetters.borderSlots) {

            page.getTemplate().getSlot(i).setButton(getBorderButton());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.skillSlots.entrySet()) {

            String skill = entry.getKey();
            Map<String, String> data = entry.getValue();
            String id = data.get("ID");
            String displayName = data.get("Display-Name");
            List<String> loreStrings = new ArrayList<>();
            if (!PixelSkills.configManager.getConfigNode(2, "Slots", skill, "Lore").isVirtual()) {

                loreStrings = PixelSkills.configManager.getConfigNode(2, "Slots", skill, "Lore").getList(TypeToken.of(String.class));

            }

            int slot = Integer.parseInt(data.get("Slot"));
            page.getTemplate().getSlot(slot).setButton(getSkillButton(skill, id, displayName, loreStrings, account));

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getSkillButton (String skill, String id, String displayName, List<String> loreString, Account account) {

        ItemStack item = ItemStackBuilder.buildFromStringID(id);
        if (!loreString.isEmpty()) {

            int level = account.getLevel(skill);
            double exp = account.getEXP(skill);
            double needed = 0;
            Map<String, Double> levelUpMap = SkillGetters.skillLevelUpMaps.get(skill);
            int nextLevel = level + 1;
            if (levelUpMap.containsKey("Level-" + nextLevel)) {

                needed = levelUpMap.get("Level-" + nextLevel);

            }

            item.setDisplayName(FancyText.getFormattedText(displayName));
            ListNBT lore = new ListNBT();
            for (String s : loreString) {

                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s
                        .replace("%level%", String.valueOf(level))
                        .replace("%current%", String.valueOf(exp))
                        .replace("%needed%", String.valueOf(needed))
                ))));

            }

            item.getOrCreateChildTag("display").put("Lore", lore);

        }

        return GooeyButton.builder().display(item).build();

    }

    private static Button getBorderButton() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.borderID);
        item.setDisplayName(FancyText.getFormattedText(""));
        return GooeyButton.builder().display(item).build();

    }

}
