package com.saveworld.saveworld.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import org.apache.commons.io.FileUtils;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import java.io.File;
import java.io.IOException;

public class SaveWorldCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("saveworld")
            .then(Commands.argument("dimension", ResourceLocationArgument.id())
                .executes(ctx -> {
                    ResourceLocation dimId = ResourceLocationArgument.getId(ctx, "dimension");
                    CommandSourceStack source = ctx.getSource();
                    MinecraftServer server = source.getServer();

                    ServerLevel overworld = server.getLevel(Level.OVERWORLD);
                    if (overworld == null) {
                        return 0;
                    }

                    // Sauvegarde
                    overworld.save(null, true, true);

                    File worldFolder = server.getWorldPath(LevelResource.ROOT).toFile();
                    File sourceRegion = new File(worldFolder, "region");
                    File sourcePoi = new File(worldFolder, "poi");
                    File sourceEntities = new File(worldFolder, "entities");

                    // Avertir si la dimension est chargée
                    ResourceKey<Level> targetKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimId);
                    ServerLevel targetLevel = server.getLevel(targetKey);

                    File destBase = new File(worldFolder, "dimensions/" + dimId.getNamespace() + "/" + dimId.getPath());
                    File destRegion = new File(destBase, "region");
                    File destPoi = new File(destBase, "poi");
                    File destEntities = new File(destBase, "entities");

                    try {
                        if (destRegion.exists()) {
                            FileUtils.deleteDirectory(destRegion);
                        }
                        FileUtils.copyDirectory(sourceRegion, destRegion);
                        if (destPoi.exists()) {
                            FileUtils.deleteDirectory(destPoi);
                        }
                        if (sourcePoi.exists()) {
                            FileUtils.copyDirectory(sourcePoi, destPoi);
                        }

                        if (destEntities.exists()) {
                            FileUtils.deleteDirectory(destEntities);
                        }
                        if (sourceEntities.exists()) {
                            FileUtils.copyDirectory(sourceEntities, destEntities);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return 1;
                }))
        );
    }
}
