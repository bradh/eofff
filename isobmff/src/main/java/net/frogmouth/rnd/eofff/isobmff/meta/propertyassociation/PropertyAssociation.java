package net.frogmouth.rnd.eofff.isobmff.meta.propertyassociation;

public class PropertyAssociation {
    private boolean essential;
    private int propertyIndex;

    public boolean isEssential() {
        return essential;
    }

    public void setEssential(boolean essential) {
        this.essential = essential;
    }

    public int getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(int propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    @Override
    public String toString() {
        return "association to "
                + propertyIndex
                + (essential ? " - (Essential)" : " - (Not Essential)");
    }
}
