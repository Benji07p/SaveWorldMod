package com.saveworld.saveworld;

import com.saveworld.saveworld.commands.SaveWorldCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod("saveworld")
public class SaveWorldMod {

    public SaveWorldMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        SaveWorldCommand.register(event.getDispatcher());
    }
}