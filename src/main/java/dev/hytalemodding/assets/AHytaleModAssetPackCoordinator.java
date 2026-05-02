package dev.hytalemodding.assets;

import com.hypixel.hytale.assetstore.AssetPack;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.AssetPackRegisterEvent;
import dev.hytalemodding.AHytaleMod;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Keeps AHytaleMod's embedded asset pack visible to the in-game AssetEditor.
 */
public final class AHytaleModAssetPackCoordinator {

    private static final String ASSET_EDITOR_PLUGIN_CLASS = "com.hypixel.hytale.builtin.asseteditor.AssetEditorPlugin";

    private final AHytaleMod plugin;

    public AHytaleModAssetPackCoordinator(@Nonnull AHytaleMod plugin) {
        this.plugin = plugin;
    }

    public void ensureAssetEditorPackVisible() {
        AssetModule assetModule = AssetModule.get();
        if (assetModule == null) {
            plugin.getLogger().at(Level.WARNING).log(
                    "AHytaleMod asset editor sync: AssetModule unavailable."
            );
            return;
        }

        String packId = new PluginIdentifier(plugin.getManifest()).toString();
        AssetPack pack = assetModule.getAssetPack(packId);
        if (pack == null) {
            plugin.getLogger().at(Level.WARNING).log(
                    "AHytaleMod asset editor sync: pack '" + packId + "' not found."
            );
            return;
        }

        try {
            Class<?> assetEditorPluginClass = Class.forName(ASSET_EDITOR_PLUGIN_CLASS);
            Method getMethod = assetEditorPluginClass.getMethod("get");
            Object assetEditorPlugin = getMethod.invoke(null);
            if (assetEditorPlugin == null) {
                plugin.getLogger().at(Level.INFO).log(
                        "AHytaleMod asset editor sync: AssetEditor plugin instance unavailable."
                );
                return;
            }

            Method getDataSourceForPack = assetEditorPluginClass.getMethod("getDataSourceForPack", String.class);
            if (getDataSourceForPack.invoke(assetEditorPlugin, packId) != null) {
                return;
            }

            Method onRegisterAssetPack =
                    assetEditorPluginClass.getDeclaredMethod("onRegisterAssetPack", AssetPackRegisterEvent.class);
            onRegisterAssetPack.setAccessible(true);
            onRegisterAssetPack.invoke(assetEditorPlugin, new AssetPackRegisterEvent(pack));

            if (getDataSourceForPack.invoke(assetEditorPlugin, packId) != null) {
                plugin.getLogger().at(Level.INFO).log(
                        "AHytaleMod asset editor sync: registered read-only data source for pack '" + packId + "'."
                );
                return;
            }

            plugin.getLogger().at(Level.WARNING).log(
                    "AHytaleMod asset editor sync: failed to register data source for pack '" + packId + "'."
            );
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().at(Level.INFO).log(
                    "AHytaleMod asset editor sync: AssetEditor plugin class not found."
            );
        } catch (Exception ex) {
            plugin.getLogger().at(Level.WARNING).withCause(ex).log(
                    "AHytaleMod asset editor sync: failed while registering pack in AssetEditor."
            );
        }
    }
}
