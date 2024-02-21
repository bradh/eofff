package net.frogmouth.rnd.eofff.isobmff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoxFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(BoxFactoryManager.class);

    // See ISO/IEC 14496-12:2022 Section 4.2.3.
    private static final byte[] MAGIC_UUID_BYTES =
            new byte[] {
                0x00,
                0x11,
                0x00,
                0x10,
                (byte) 0x80,
                0x00,
                0x00,
                (byte) 0xAA,
                0x00,
                0x38,
                (byte) 0x9B,
                0x71
            };
    public static final FourCC UUID_FOURCC = new FourCC("uuid");

    private final ServiceLoader<BoxParser> loader;
    protected final Map<FourCC, BoxParser> boxFactories = new HashMap<>();
    protected final Map<UUID, BoxParser> uuidBoxFactories = new HashMap<>();

    private BoxFactoryManager() {
        loader = ServiceLoader.load(BoxParser.class);
        for (BoxParser factory : loader) {
            LOG.debug("Loading box parser for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
            UUID fourCCAsUUID = makeUUID(factory.getFourCC());
            if (fourCCAsUUID != null) {
                uuidBoxFactories.put(fourCCAsUUID, factory);
            }
        }
    }

    public BoxParser findParser(FourCC fourcc) {
        LOG.trace("Looking up box parser for {}", fourcc.toString());
        BoxParser boxParser = boxFactories.getOrDefault(fourcc, new BaseBoxParser());
        LOG.trace("Providing parser: {}", boxParser.toString());
        if (boxParser.getClass().equals(BaseBoxParser.class)) {
            LOG.warn("Failed to find box parser for " + fourcc.toString());
        }
        return boxParser;
    }

    public BoxParser findParser(UUID uuid) {
        LOG.trace("Looking up UUID box parser for {}", uuid.toString());
        BoxParser boxParser = uuidBoxFactories.getOrDefault(uuid, new UUIDBaseBoxParser());
        LOG.trace("Providing parser: {}", boxParser.toString());
        if (boxParser.getClass().equals(UUIDBaseBoxParser.class)) {
            LOG.warn("Failed to find UUID box parser for " + uuid.toString());
        }
        return boxParser;
    }

    public static BoxParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static BoxParser getParser(UUID uuid) {
        return getInstance().findParser(uuid);
    }

    public static BoxFactoryManager getInstance() {
        return BoxFactoryManagerHolder.INSTANCE;
    }

    private UUID makeUUID(FourCC fourCC) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(fourCC.toBytes());
            baos.write(MAGIC_UUID_BYTES);
            byte[] uuidBytes = baos.toByteArray();
            return getUuidFromByteArray(uuidBytes);
        } catch (IOException ex) {
            return null;
        }
    }

    public static UUID getUuidFromByteArray(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        UUID uuid = new UUID(high, low);
        return uuid;
    }

    private static class BoxFactoryManagerHolder {

        private static final BoxFactoryManager INSTANCE = new BoxFactoryManager();
    }
}
