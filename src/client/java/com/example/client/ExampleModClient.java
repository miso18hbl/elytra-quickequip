package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    private static KeyBinding elytraKey;

    @Override
    public void onInitializeClient() {
        elytraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.elytraquickequip.equip",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.elytraquickequip"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (elytraKey.wasPressed()) {
                if (client.player == null) return;
                toggleElytra(client.player);
            }
        });
    }

    private void toggleElytra(net.minecraft.client.network.ClientPlayerEntity player) {
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        PlayerInventory inv = player.getInventory();

        if (chest.getItem() instanceof ElytraItem) {
            for (int i = 0; i < 36; i++) {
                if (inv.getStack(i).isEmpty()) {
                    inv.setStack(i, chest.copy());
                    player.equipStack(EquipmentSlot.CHEST, ItemStack.EMPTY);
                    player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, 1f, 0.8f);
                    return;
                }
            }
            return;
        }

        for (int i = 0; i < inv.size(); i++) {
            if (inv.getStack(i).getItem() instanceof ElytraItem) {
                ItemStack elytra = inv.getStack(i).copy();
                inv.setStack(i, chest.copy());
                player.equipStack(EquipmentSlot.CHEST, elytra);
                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, 1f, 1f);
                return;
            }
        }
    }
}
