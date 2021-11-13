package net.frogmouth.rnd.eofff.isobmff;

public interface Box {

    public String getFullName();

    FourCC getFourCC();

    long getSize();
}
