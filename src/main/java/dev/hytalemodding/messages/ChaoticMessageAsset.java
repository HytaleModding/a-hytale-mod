package dev.hytalemodding.messages;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import javax.annotation.Nullable;

public class ChaoticMessageAsset implements JsonAssetWithMap<String, DefaultAssetMap<String, ChaoticMessageAsset>> {

    public static final String DEFAULT_ID = "Default";

    public static final AssetBuilderCodec<String, ChaoticMessageAsset> CODEC =
            AssetBuilderCodec.builder(
                            ChaoticMessageAsset.class,
                            ChaoticMessageAsset::new,
                            Codec.STRING,
                            (asset, id) -> asset.id = id,
                            asset -> asset.id,
                            (asset, data) -> asset.data = data,
                            asset -> asset.data
                    )
                    .documentation("Configurable chaotic server message sets.")
                    .<Boolean>append(new KeyedCodec<>("Enabled", Codec.BOOLEAN), (o, v) -> o.enabled = v == null || v, o -> o.enabled)
                    .add()
                    .<Boolean>append(new KeyedCodec<>("JoinMessagesEnabled", Codec.BOOLEAN), (o, v) -> o.joinMessagesEnabled = v == null || v, o -> o.joinMessagesEnabled)
                    .add()
                    .<Boolean>append(new KeyedCodec<>("PeriodicMessagesEnabled", Codec.BOOLEAN), (o, v) -> o.periodicMessagesEnabled = v == null || v, o -> o.periodicMessagesEnabled)
                    .add()
                    .<Double>append(new KeyedCodec<>("RareChance", Codec.DOUBLE), (o, v) -> o.rareChance = v == null ? 0.05 : v, o -> o.rareChance)
                    .add()
                    .<Double>append(new KeyedCodec<>("PeriodicIntervalSeconds", Codec.DOUBLE), (o, v) -> o.periodicIntervalSeconds = v == null ? 600.0 : v, o -> o.periodicIntervalSeconds)
                    .add()
                    .<MessageCategory>append(new KeyedCodec<>("Join", MessageCategory.CODEC), (o, v) -> o.join = v == null ? new MessageCategory() : v, o -> o.join)
                    .add()
                    .<MessageCategory>append(new KeyedCodec<>("JoinRare", MessageCategory.CODEC), (o, v) -> o.joinRare = v == null ? new MessageCategory() : v, o -> o.joinRare)
                    .add()
                    .<MessageCategory>append(new KeyedCodec<>("Periodic", MessageCategory.CODEC), (o, v) -> o.periodic = v == null ? new MessageCategory() : v, o -> o.periodic)
                    .add()
                    .build();

    private static AssetStore<String, ChaoticMessageAsset, DefaultAssetMap<String, ChaoticMessageAsset>> ASSET_STORE;

    private AssetExtraInfo.Data data;
    private String id;
    private boolean enabled = true;
    private boolean joinMessagesEnabled = true;
    private boolean periodicMessagesEnabled = true;
    private double rareChance = 0.05;
    private double periodicIntervalSeconds = 600.0;
    private MessageCategory join = new MessageCategory();
    private MessageCategory joinRare = new MessageCategory();
    private MessageCategory periodic = new MessageCategory();

    public static AssetStore<String, ChaoticMessageAsset, DefaultAssetMap<String, ChaoticMessageAsset>> getAssetStore() {
        if (ASSET_STORE == null) {
            ASSET_STORE = AssetRegistry.getAssetStore(ChaoticMessageAsset.class);
        }
        return ASSET_STORE;
    }

    @Nullable
    public static DefaultAssetMap<String, ChaoticMessageAsset> getAssetMap() {
        AssetStore<String, ChaoticMessageAsset, DefaultAssetMap<String, ChaoticMessageAsset>> store = getAssetStore();
        if (store == null) {
            return null;
        }
        return (DefaultAssetMap<String, ChaoticMessageAsset>) store.getAssetMap();
    }

    @Nullable
    public static ChaoticMessageAsset getDefaultAsset() {
        DefaultAssetMap<String, ChaoticMessageAsset> assetMap = getAssetMap();
        if (assetMap == null) {
            return null;
        }
        return assetMap.getAsset(DEFAULT_ID);
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isJoinMessagesEnabled() {
        return joinMessagesEnabled;
    }

    public boolean isPeriodicMessagesEnabled() {
        return periodicMessagesEnabled;
    }

    public double getRareChance() {
        return Math.clamp(rareChance, 0.0, 1.0);
    }

    public long getPeriodicIntervalSeconds() {
        return Math.max(1L, Math.round(periodicIntervalSeconds));
    }

    public MessageCategory getJoin() {
        return join;
    }

    public MessageCategory getJoinRare() {
        return joinRare;
    }

    public MessageCategory getPeriodic() {
        return periodic;
    }

    public static class MessageCategory {

        public static final BuilderCodec<MessageCategory> CODEC = BuilderCodec.builder(
                        MessageCategory.class,
                        MessageCategory::new
                )
                .<Boolean>append(new KeyedCodec<>("Enabled", Codec.BOOLEAN), (o, v) -> o.enabled = v == null || v, o -> o.enabled)
                .add()
                .<String>append(new KeyedCodec<>("Delivery", Codec.STRING), (o, v) -> o.delivery = v == null ? "Server" : v, o -> o.delivery)
                .add()
                .<String[]>append(new KeyedCodec<>("Messages", Codec.STRING_ARRAY), (o, v) -> o.messages = v == null ? new String[0] : v, o -> o.messages)
                .add()
                .build();

        private boolean enabled = true;
        private String delivery = "Server";
        private String[] messages = new String[0];

        public boolean isEnabled() {
            return enabled;
        }

        public String getDelivery() {
            return delivery;
        }

        public String[] getMessages() {
            return messages;
        }
    }
}
