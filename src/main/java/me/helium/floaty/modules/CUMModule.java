package me.helium.floaty.modules;

import me.helium.floaty.Addon;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class CUMModule extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> cum = sgGeneral.add(new IntSetting.Builder()
        .name("Ammount")
        .description("Ammount of thrown projectiles")
        .min(1)
        .max(16)
        .defaultValue(1)
            .sliderMin(1)
            .sliderMax(16)
        .build()
    );

    private final Setting<Item> items = sgGeneral.add(new ItemSetting.Builder()
        .name("Items")
        .description("Item to throw")
        .filter(item -> item == Items.SNOWBALL || item == Items.EGG || item == Items.ENDER_PEARL || item == Items.EXPERIENCE_BOTTLE || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION)
        .build()
    );

    public CUMModule() {
        super(Addon.CATEGORY, "CUM", "Funy");
    }

    @Override
    public void onActivate() {
        if(mc.player == null) return;
        for (int i = 0; i < cum.get(); i++) {
            FindItemResult item = InvUtils.findInHotbar(items.get());
            if(!item.found()){
                ChatUtils.error("You dont have any(more) of your selected item in your hotbar.");
                toggle();
                return;
            }
            useItem(item);
        }
        toggle();
    }

    private void useItem(FindItemResult item) {
        if(item.isOffhand()){
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
        } else {
            InvUtils.swap(item.slot(), true);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            InvUtils.swapBack();
        }
    }

}
