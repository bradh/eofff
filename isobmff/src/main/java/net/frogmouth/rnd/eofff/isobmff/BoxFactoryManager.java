package net.frogmouth.rnd.eofff.isobmff;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoxFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(BoxFactoryManager.class);
    private final ServiceLoader<BoxParser> loader;
    protected final Map<FourCC, BoxParser> boxFactories = new HashMap<>();

    private BoxFactoryManager() {
        loader = ServiceLoader.load(BoxParser.class);
        for (BoxParser factory : loader) {
            LOG.debug("Loading box parser for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
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

    public static BoxParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static BoxFactoryManager getInstance() {
        return BoxFactoryManagerHolder.INSTANCE;
    }

    private static class BoxFactoryManagerHolder {

        private static final BoxFactoryManager INSTANCE = new BoxFactoryManager();
    }
}
