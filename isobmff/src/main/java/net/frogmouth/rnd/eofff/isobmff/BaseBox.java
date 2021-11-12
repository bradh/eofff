package net.frogmouth.rnd.eofff.isobmff;

public class BaseBox implements Box {
    private long size;
    private String boxName;

    public BaseBox(long size, String name) {
        setBoxName(name);
        setSize(size);
    }

    @Override
    public long getSize() {
        return size;
    }

    public final void setSize(long size) {
        this.size = size;
    }

    @Override
    public String getBoxName() {
        return boxName;
    }

    public final void setBoxName(String name) {
        this.boxName = name;
    }

    @Override
    public String getFullName() {
        return "Unimplemented Box";
    }

    @Override
    public String toString() {
        return getFullName() + " '" + getBoxName() + "'";
    }
}
