package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleGroupEntryFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(SampleGroupEntryFactoryManager.class);
    private final ServiceLoader<SampleGroupEntryParser> loader;
    protected final Map<FourCC, SampleGroupEntryParser> boxFactories = new HashMap<>();

    private SampleGroupEntryFactoryManager() {
        loader = ServiceLoader.load(SampleGroupEntryParser.class);
        for (SampleGroupEntryParser factory : loader) {
            LOG.debug("Loading sample group entry parser for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public SampleGroupEntryParser findParser(FourCC fourcc) {
        LOG.trace("Looking up sample group entry parser for {}", fourcc.toString());
        SampleGroupEntryParser boxParser =
                boxFactories.getOrDefault(fourcc, new FallbackSampleGroupEntryParser());
        LOG.trace("Providing parser: {}", boxParser.toString());
        if (boxParser.getClass().equals(FallbackSampleGroupEntryParser.class)) {
            LOG.warn("Failed to find sample group entry parser for " + fourcc.toString());
        }
        return boxParser;
    }

    public static SampleGroupEntryParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static SampleGroupEntryFactoryManager getInstance() {
        return SampleGroupEntryFactoryManagerHolder.INSTANCE;
    }

    private static class SampleGroupEntryFactoryManagerHolder {

        private static final SampleGroupEntryFactoryManager INSTANCE =
                new SampleGroupEntryFactoryManager();
    }
}
