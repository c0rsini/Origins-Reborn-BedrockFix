This patch resolves the issue where Bedrock players (via Geyser/Floodgate) experience permanent Nether fog.

The bug is caused by the Phantom origin's ability, which uses a world border packet to create a red visual overlay. While this works on Java, Bedrock clients misinterpret this packet as dense fog.

To fix this, I modified com/starshootercity/abilities/PhantomizeOverlay.java. The code now checks if the Floodgate API is present; if a player is detected as a Bedrock user, the plugin skips sending the world border overlay packet to them. I also added Floodgate to the softdepend list in plugin.yml to ensure proper load order.

Java players are unaffected and will still see the intended red overlay, while Bedrock players will no longer be blinded by the fog.