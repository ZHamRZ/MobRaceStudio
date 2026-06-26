package com.mobracestudio;

import com.mobracestudio.editor.EditorManager;
import com.mobracestudio.event.ClientEvents;
import com.mobracestudio.gui.hud.RaceHUD;
import com.mobracestudio.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class MobRaceStudioClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(MobRaceStudio.MOD_ID + "-client");

    private static MobRaceStudioClient INSTANCE;
    private EditorManager editorManager;
    private RaceHUD raceHUD;

    public static MobRaceStudioClient getInstance() {
        return INSTANCE;
    }

    public EditorManager getEditorManager() {
        return editorManager;
    }

    public RaceHUD getRaceHUD() {
        return raceHUD;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        this.editorManager = new EditorManager();
        this.raceHUD = new RaceHUD();

        ClientEvents.register();

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            raceHUD.render(drawContext, tickDelta);
        });

        EntityRegistry.discoverAllEntities();

        LOGGER.info("Mob Race Studio client initialized!");
    }
}
