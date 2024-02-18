package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackGroupTypeFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(TrackGroupTypeFactoryManager.class);
    private final ServiceLoader<TrackGroupTypeParser> loader;
    protected final Map<FourCC, TrackGroupTypeParser> boxFactories = new HashMap<>();

    private TrackGroupTypeFactoryManager() {
        loader = ServiceLoader.load(TrackGroupTypeParser.class);
        for (TrackGroupTypeParser factory : loader) {
            LOG.debug("Loading track group type parser for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public TrackGroupTypeParser findParser(FourCC fourcc) {
        LOG.trace("Looking up track group parser for {}", fourcc.toString());
        TrackGroupTypeParser boxParser =
                boxFactories.getOrDefault(fourcc, new BaseTrackGroupTypeParser());
        LOG.trace("Providing parser: {}", boxParser.toString());
        if (boxParser.getClass().equals(BaseTrackGroupTypeParser.class)) {
            LOG.warn("Failed to find track group parser for " + fourcc.toString());
        }
        return boxParser;
    }

    public static TrackGroupTypeParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static TrackGroupTypeFactoryManager getInstance() {
        return TrackGroupTypeFactoryManagerHolder.INSTANCE;
    }

    private static class TrackGroupTypeFactoryManagerHolder {

        private static final TrackGroupTypeFactoryManager INSTANCE =
                new TrackGroupTypeFactoryManager();
    }
}
