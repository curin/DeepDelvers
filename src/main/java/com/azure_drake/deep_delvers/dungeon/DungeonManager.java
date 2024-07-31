package com.azure_drake.deep_delvers.dungeon;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.portal.DungeonPortal;
import com.azure_drake.deep_delvers.portal.PortalID;
import net.minecraft.BlockUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DungeonManager
{
    public static List<ResourceLocation> DungeonEntrances = new ArrayList<>();

    public static ResourceKey<Level> DEEP_DUGEON = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(DeepDelversMod.MODID, "deep_dungeon"));

    public static void SpawnNewDungeon(MinecraftServer server, PortalID id, DungeonPortal portal)
    {
        ServerLevel sLevel = server.getLevel(DEEP_DUGEON);

        if (DungeonEntrances.size() > 0)
        {
            Optional<StructureTemplate> template = Optional.empty();
            for (int i = 0; i < DungeonEntrances.size() && template.isEmpty(); i++) {
                 template = sLevel.getStructureManager().get(DungeonEntrances.get(sLevel.random.nextInt(DungeonEntrances.size())));
            }

            if (template.isPresent())
            {
                Rotation rot;
                Mirror mirror;
                int rand = sLevel.random.nextInt(3);
                switch (rand)
                {
                    case 1:
                        mirror = Mirror.LEFT_RIGHT;
                        break;
                    case 2:
                        mirror = Mirror.FRONT_BACK;
                        break;
                    default:
                        mirror = Mirror.NONE;
                        break;
                }

                rand = sLevel.random.nextInt(4);
                switch (rand)
                {
                    case 1:
                        rot = Rotation.CLOCKWISE_90;
                        break;
                    case 2:
                        rot = Rotation.CLOCKWISE_180;
                        break;
                    case 3:
                        rot = Rotation.COUNTERCLOCKWISE_90;
                        break;
                    default:
                        rot = Rotation.NONE;
                        break;
                }

                placeStructure(sLevel, template.get(), portal.DungeonBounds.minCorner, mirror, rot);
            }
        }
    }

    private static void placeStructure(ServerLevel sLevel, StructureTemplate pStructureTemplate, BlockPos pPos, Mirror mirror, Rotation rotation) {
        StructurePlaceSettings structureplacesettings = new StructurePlaceSettings()
                .setMirror(mirror)
                .setRotation(rotation)
                .setIgnoreEntities(false);

        pPos = pPos.offset(0, -5, 0);
        pStructureTemplate.placeInWorld(sLevel, pPos, pPos, structureplacesettings, sLevel.random, 2);
    }

    public static RandomSource createRandom(long pSeed) {
        return pSeed == 0L ? RandomSource.create(Util.getMillis()) : RandomSource.create(pSeed);
    }
}
