package net.frogmouth.rnd.eofff.isobmff;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class BoxFactoryManager {

    private final ServiceLoader<BoxParser> loader;
    protected final Map<String, BoxParser> boxFactories = new HashMap<>();

    private BoxFactoryManager() {
        loader = ServiceLoader.load(BoxParser.class);
        for (BoxParser factory : loader) {
            boxFactories.put(factory.getFourCC(), factory);
        }
    }

    public BoxParser findParser(String fourcc) {
        BoxParser boxParser = boxFactories.getOrDefault(fourcc, new BaseBoxParser());
        return boxParser;
    }
    
    public static BoxParser getParser(String fourcc) {
            return getInstance().findParser(fourcc);
    }

    public static BoxFactoryManager getInstance() {
        return BoxFactoryManagerHolder.INSTANCE;
    }

    private static class BoxFactoryManagerHolder {

        private static final BoxFactoryManager INSTANCE = new BoxFactoryManager();
    }
}
