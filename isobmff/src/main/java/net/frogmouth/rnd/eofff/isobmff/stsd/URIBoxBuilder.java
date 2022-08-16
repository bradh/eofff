package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class URIBoxBuilder {

    private int version;
    private int flags;
    private String theURI;

    public URIBoxBuilder() {}

    public URIBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public URIBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public URIBoxBuilder withURI(String uri) {
        this.theURI = uri;
        return this;
    }

    public URIBox build() {
        URIBox box = new URIBox(new FourCC("uri "));
        box.setVersion(version);
        box.setFlags(flags);
        box.setTheURI(theURI);
        return box;
    }
}
