package me.konicai;

import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.session.GeyserSession;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class TransferApi {

    private static final int UDP_MAX_PORT = 65535;

    @Nullable
    private static GeyserImpl GEYSER;
    
    private TransferApi() {
        // no-op
    }

    /**
     * Setup the handle on Geyser.
     * @return True if the was success initializing. If this returns false, {@link #transfer(UUID, String, int)} will still
     * be unavailable
     */
    public static boolean initialize() {
        GEYSER = GeyserImpl.getInstance();
        return GEYSER != null;
    }

    public static boolean isInitialized() {
        return GEYSER != null;
    }

    public static void transfer(@Nonnull UUID uuid, @Nonnull String address, int port) {
        if (GEYSER == null) {
            throw new UnsupportedOperationException("Cannot transfer player without having called initialize() on TransferApi");
        }
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(address);
        if (port < 0 || port > UDP_MAX_PORT) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }
        GeyserSession session = GEYSER.connectionByUuid(uuid);
        if (session == null) {
            throw new IllegalArgumentException("Failed to find GeyserSession from UUID: " + uuid);
        }
        TransferPacket packet = new TransferPacket();
        packet.setAddress(address);
        packet.setPort(port);
        session.sendUpstreamPacket(packet);
    }
}
