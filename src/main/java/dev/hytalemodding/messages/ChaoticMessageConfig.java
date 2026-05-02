package dev.hytalemodding.messages;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

@SuppressWarnings("deprecation")
public class ChaoticMessageConfig {

    public static final BuilderCodec<ChaoticMessageConfig> CODEC = BuilderCodec.builder(
                    ChaoticMessageConfig.class,
                    ChaoticMessageConfig::new
            )
            .addField(new KeyedCodec<>("Enabled", Codec.BOOLEAN, false), (o, v) -> o.enabled = v, o -> o.enabled)
            .addField(new KeyedCodec<>("JoinMessagesEnabled", Codec.BOOLEAN, false), (o, v) -> o.joinMessagesEnabled = v, o -> o.joinMessagesEnabled)
            .addField(new KeyedCodec<>("PeriodicMessagesEnabled", Codec.BOOLEAN, false), (o, v) -> o.periodicMessagesEnabled = v, o -> o.periodicMessagesEnabled)
            .addField(new KeyedCodec<>("RareChance", Codec.DOUBLE, false), (o, v) -> o.rareChance = v, o -> o.rareChance)
            .addField(new KeyedCodec<>("PeriodicIntervalSeconds", Codec.DOUBLE, false), (o, v) -> o.periodicIntervalSeconds = v, o -> o.periodicIntervalSeconds)
            .addField(new KeyedCodec<>("PillowLore", MessageCategory.CODEC, false), (o, v) -> o.pillowLore = v, o -> o.pillowLore)
            .addField(new KeyedCodec<>("RepoChaos", MessageCategory.CODEC, false), (o, v) -> o.repoChaos = v, o -> o.repoChaos)
            .addField(new KeyedCodec<>("FakeSystem", MessageCategory.CODEC, false), (o, v) -> o.fakeSystem = v, o -> o.fakeSystem)
            .addField(new KeyedCodec<>("OverdramaticFantasy", MessageCategory.CODEC, false), (o, v) -> o.overdramaticFantasy = v, o -> o.overdramaticFantasy)
            .addField(new KeyedCodec<>("RareLegendary", MessageCategory.CODEC, false), (o, v) -> o.rareLegendary = v, o -> o.rareLegendary)
            .addField(new KeyedCodec<>("Periodic", MessageCategory.CODEC, false), (o, v) -> o.periodic = v, o -> o.periodic)
            .build();

    private boolean enabled = true;
    private boolean joinMessagesEnabled = true;
    private boolean periodicMessagesEnabled = true;
    private double rareChance = 0.05;
    private double periodicIntervalSeconds = 600.0;
    private MessageCategory pillowLore = new MessageCategory(
            "Server",
            "Welcome, {player}. The pillow remembers.",
            "{player} has joined. Everyone act normal around the pillow.",
            "The Gaia body pillow has accepted {player} as temporarily real.",
            "{player} entered the world. Comfort levels are now unstable."
    );
    private MessageCategory repoChaos = new MessageCategory(
            "Server",
            "Welcome {player}. The project still compiles, somehow.",
            "{player} joined. Please do not remove any lines of code.",
            "A new contributor approaches. May their merge conflicts be decorative.",
            "{player} loaded successfully. Source jar not included."
    );
    private MessageCategory fakeSystem = new MessageCategory(
            "Server",
            "Warning: {player} has spawned with experimental confidence.",
            "Server notice: {player} is not a valid crafting ingredient.",
            "Initializing {player}... done. Regret module enabled.",
            "Patch notes: Added {player}. Known issue: {player}."
    );
    private MessageCategory overdramaticFantasy = new MessageCategory(
            "Server",
            "{player} has crossed the threshold. The logs whisper.",
            "A traveler arrives, carrying absolutely no context.",
            "{player} appears. The realm lowers its expectations.",
            "The prophecy mentioned someone else, but {player} will do."
    );
    private MessageCategory rareLegendary = new MessageCategory(
            "Server",
            "THE PILLOW HAS CHOSEN {player}.",
            "{player} joined and the server briefly considered becoming stable.",
            "A hush falls over the world. {player} has found the join button.",
            "All hail {player}, temporary maintainer of the vibe."
    );
    private MessageCategory periodic = new MessageCategory(
            "Server",
            "The pillow emits a low hum. This is probably fine.",
            "Reminder: the codebase is legally distinct from a plan.",
            "Server wellness check: still compiling in spirit.",
            "A distant voice whispers: do not remove any lines of code."
    );

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

    public MessageCategory getPillowLore() {
        return pillowLore;
    }

    public MessageCategory getRepoChaos() {
        return repoChaos;
    }

    public MessageCategory getFakeSystem() {
        return fakeSystem;
    }

    public MessageCategory getOverdramaticFantasy() {
        return overdramaticFantasy;
    }

    public MessageCategory getRareLegendary() {
        return rareLegendary;
    }

    public MessageCategory getPeriodic() {
        return periodic;
    }

    public static class MessageCategory {

        public static final BuilderCodec<MessageCategory> CODEC = BuilderCodec.builder(
                        MessageCategory.class,
                        MessageCategory::new
                )
                .addField(new KeyedCodec<>("Enabled", Codec.BOOLEAN, false), (o, v) -> o.enabled = v, o -> o.enabled)
                .addField(new KeyedCodec<>("Delivery", Codec.STRING, false), (o, v) -> o.delivery = v, o -> o.delivery)
                .addField(new KeyedCodec<>("Messages", Codec.STRING_ARRAY, false), (o, v) -> o.messages = v, o -> o.messages)
                .build();

        private boolean enabled = true;
        private String delivery = "Server";
        private String[] messages = new String[0];

        public MessageCategory() {
        }

        public MessageCategory(String delivery, String... messages) {
            this.delivery = delivery;
            this.messages = messages;
        }

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
