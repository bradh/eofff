package net.frogmouth.rnd.eofff.isobmff.iref;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemReferenceFactoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(ItemReferenceFactoryManager.class);
    private final ServiceLoader<ItemReferenceFactory> loader;
    protected final Map<FourCC, ItemReferenceFactory> boxFactories = new HashMap<>();

    private ItemReferenceFactoryManager() {
        loader = ServiceLoader.load(ItemReferenceFactory.class);
        for (ItemReferenceFactory factory : loader) {
            LOG.debug("Loading item reference factory for {}", factory.getFourCC().toString());
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public ItemReferenceFactory findFactory(FourCC fourcc) {
        LOG.trace("Looking up item reference factory for {}", fourcc.toString());
        ItemReferenceFactory factory =
                boxFactories.getOrDefault(fourcc, new DefaultItemReferenceFactory());
        LOG.trace("Providing factory: {}", factory.toString());
        if (factory.getClass().equals(DefaultItemReferenceFactory.class)) {
            LOG.warn("Failed to find item reference factory for " + fourcc.toString());
        }
        return factory;
    }

    public static ItemReferenceFactory getFactory(FourCC fourcc) {
        return getInstance().findFactory(fourcc);
    }

    public static ItemReferenceFactoryManager getInstance() {
        return ItemReferenceFactoryManagerHolder.INSTANCE;
    }

    private static class ItemReferenceFactoryManagerHolder {

        private static final ItemReferenceFactoryManager INSTANCE =
                new ItemReferenceFactoryManager();
    }
}
