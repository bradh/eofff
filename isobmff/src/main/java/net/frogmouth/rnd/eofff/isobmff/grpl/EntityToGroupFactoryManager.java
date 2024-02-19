package net.frogmouth.rnd.eofff.isobmff.grpl;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityToGroupFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(EntityToGroupFactoryManager.class);
    private final ServiceLoader<EntityToGroupParser> loader;
    protected final Map<FourCC, EntityToGroupParser> factories = new HashMap<>();

    private EntityToGroupFactoryManager() {
        loader = ServiceLoader.load(EntityToGroupParser.class);
        for (EntityToGroupParser factory : loader) {
            LOG.debug("Loading entity-to-group parser for {}", factory.getFourCC().toString());
            factories.put(factory.getFourCC(), factory);
        }
    }

    public EntityToGroupParser findParser(FourCC fourcc) {
        LOG.trace("Looking up entity-to-group parser for {}", fourcc.toString());
        EntityToGroupParser parser =
                factories.getOrDefault(fourcc, new UnknownEntityToGroupParser());
        LOG.trace("Providing parser: {}", parser.toString());
        if (parser instanceof UnknownEntityToGroupParser) {
            LOG.warn("Failed to find entity-to-group parser for " + fourcc.toString());
        }
        return parser;
    }

    public static EntityToGroupParser getParser(FourCC fourcc) {
        return getInstance().findParser(fourcc);
    }

    public static EntityToGroupFactoryManager getInstance() {
        return EntityToGroupFactoryManagerHolder.INSTANCE;
    }

    private static class EntityToGroupFactoryManagerHolder {

        private static final EntityToGroupFactoryManager INSTANCE =
                new EntityToGroupFactoryManager();
    }
}
