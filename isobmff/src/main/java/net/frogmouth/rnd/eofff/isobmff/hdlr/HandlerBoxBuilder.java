package net.frogmouth.rnd.eofff.isobmff.hdlr;

public class HandlerBoxBuilder {

    private int version;
    private int flags;
    private String handlerType;
    private String name = "";

    public HandlerBoxBuilder() {}

    public HandlerBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public HandlerBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public HandlerBoxBuilder withHandlerType(String handlerType) {
        this.handlerType = handlerType;
        return this;
    }

    public HandlerBoxBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HandlerBox build() {
        HandlerBox box = new HandlerBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.setPreDefined(0);
        box.setHandlerType(handlerType);
        box.setReserved0(0);
        box.setReserved1(0);
        box.setReserved2(0);
        box.setName(name);
        return box;
    }
}
