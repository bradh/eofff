package net.frogmouth.rnd.eofff.isobmff.iprp;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyFactoryManager.class);
    private final ServiceLoader<PropertyParser> loader;
    protected final Map<FourCC, PropertyParser> factories = new HashMap<>();

    private PropertyFactoryManager() {
        loader = ServiceLoader.load(PropertyParser.class);
        for (PropertyParser factory : loader) {
            LOG.debug("Loading property parser for {}", factory.getFourCC().toString());
            factories.put(factory.getFourCC(), factory);
        }
    }

    public PropertyParser findParser(FourCC fourcc) {
        LOG.trace("Looking up property parser for {}", fourcc.toString());
        PropertyParser parser = factories.getOrDefault(fourcc, new UnknownPropertyParser());
        LOG.trace("Providing parser: {}", parser.toString());
        if (parser instanceof UnknownPropertyParser) {
            LOG.warn("Failed to find property parser for " + fourcc.toString());
        }
        return parser;
    }

    public static PropertyParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static PropertyFactoryManager getInstance() {
        return BoxFactoryManagerHolder.INSTANCE;
    }

    private static class BoxFactoryManagerHolder {

        private static final PropertyFactoryManager INSTANCE = new PropertyFactoryManager();
    }
}
