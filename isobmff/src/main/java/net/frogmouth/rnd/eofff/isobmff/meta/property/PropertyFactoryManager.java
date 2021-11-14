package net.frogmouth.rnd.eofff.isobmff.meta.property;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class PropertyFactoryManager {

    private final ServiceLoader<PropertyParser> loader;
    protected final Map<FourCC, PropertyParser> boxFactories = new HashMap<>();

    private PropertyFactoryManager() {
        loader = ServiceLoader.load(PropertyParser.class);
        for (PropertyParser factory : loader) {
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public PropertyParser findParser(FourCC fourcc) {
        PropertyParser boxParser = boxFactories.getOrDefault(fourcc, new UnknownPropertyParser());
        return boxParser;
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
