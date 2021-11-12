package net.frogmouth.rnd.eofff.isobmff;

public interface Box {

    public String getFullName();

    String getBoxName();

    long getSize();
    
}
