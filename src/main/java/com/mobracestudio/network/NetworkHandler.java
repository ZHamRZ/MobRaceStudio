package com.mobracestudio.network;

import com.mobracestudio.MobRaceStudio;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NetworkHandler {
    public static final Identifier ARENA_SYNC_CHANNEL = new Identifier(MobRaceStudio.MOD_ID, "arena_sync");
    public static final Identifier RACE_STATE_CHANNEL = new Identifier(MobRaceStudio.MOD_ID, "race_state");
    public static final Identifier MOB_CONFIG_CHANNEL = new Identifier(MobRaceStudio.MOD_ID, "mob_config");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(ARENA_SYNC_CHANNEL, (server, player, handler, buf, responseSender) -> {
            MobRaceStudio.LOGGER.debug("Received arena sync request from: {}", player.getName().getString());
        });

        ServerPlayNetworking.registerGlobalReceiver(RACE_STATE_CHANNEL, (server, player, handler, buf, responseSender) -> {
            MobRaceStudio.LOGGER.debug("Received race state from: {}", player.getName().getString());
        });

        ServerPlayNetworking.registerGlobalReceiver(MOB_CONFIG_CHANNEL, (server, player, handler, buf, responseSender) -> {
            MobRaceStudio.LOGGER.debug("Received mob config from: {}", player.getName().getString());
        });
    }

    public static void sendArenaSync(ServerPlayerEntity player, String arenaJson) {
        if (ServerPlayNetworking.canSend(player, ARENA_SYNC_CHANNEL)) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(arenaJson);
            ServerPlayNetworking.send(player, ARENA_SYNC_CHANNEL, buf);
        }
    }

    public static void sendRaceState(ServerPlayerEntity player, int state, float time) {
        if (ServerPlayNetworking.canSend(player, RACE_STATE_CHANNEL)) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(state);
            buf.writeFloat(time);
            ServerPlayNetworking.send(player, RACE_STATE_CHANNEL, buf);
        }
    }
}
