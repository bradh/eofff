package net.frogmouth.rnd.eofff.isobmff.dref;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataReferenceFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(DataReferenceFactoryManager.class);
    private final ServiceLoader<DataReferenceParser> loader;
    protected final Map<FourCC, DataReferenceParser> boxFactories = new HashMap<>();

    private DataReferenceFactoryManager() {
        loader = ServiceLoader.load(DataReferenceParser.class);
        for (DataReferenceParser factory : loader) {
            LOG.debug("Loading data reference parser for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public DataReferenceParser findParser(FourCC fourcc) {
        LOG.trace("Looking up data reference parser for {}", fourcc.toString());
        DataReferenceParser boxParser =
                boxFactories.getOrDefault(fourcc, new DataEntryBaseBoxParser());
        LOG.trace("Providing parser: {}", boxParser.toString());
        if (boxParser.getClass().equals(DataEntryBaseBoxParser.class)) {
            LOG.warn("Failed to find data entry parser for " + fourcc.toString());
        }
        return boxParser;
    }

    public static DataReferenceParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static DataReferenceFactoryManager getInstance() {
        return DataReferenceFactoryManagerHolder.INSTANCE;
    }

    private static class DataReferenceFactoryManagerHolder {

        private static final DataReferenceFactoryManager INSTANCE =
                new DataReferenceFactoryManager();
    }
}
