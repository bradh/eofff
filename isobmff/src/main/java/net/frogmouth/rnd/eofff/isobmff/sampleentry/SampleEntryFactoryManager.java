package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleEntryFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(SampleEntryFactoryManager.class);
    private final ServiceLoader<SampleEntryParser> loader;
    protected final Map<FourCC, SampleEntryParser> boxFactories = new HashMap<>();

    private SampleEntryFactoryManager() {
        loader = ServiceLoader.load(SampleEntryParser.class);
        for (SampleEntryParser factory : loader) {
            LOG.debug("Loading sample entry parser for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public SampleEntryParser findParser(FourCC fourcc) {
        LOG.trace("Looking up sample entry parser for {}", fourcc.toString());
        SampleEntryParser boxParser =
                boxFactories.getOrDefault(fourcc, new SampleEntryBaseBoxParser());
        LOG.trace("Providing parser: {}", boxParser.toString());
        if (boxParser.getClass().equals(SampleEntryBaseBoxParser.class)) {
            LOG.warn("Failed to find data entry parser for " + fourcc.toString());
        }
        return boxParser;
    }

    public static SampleEntryParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static SampleEntryFactoryManager getInstance() {
        return SampleEntryFactoryManagerHolder.INSTANCE;
    }

    private static class SampleEntryFactoryManagerHolder {

        private static final SampleEntryFactoryManager INSTANCE = new SampleEntryFactoryManager();
    }
}
