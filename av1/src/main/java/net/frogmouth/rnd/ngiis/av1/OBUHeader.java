package net.frogmouth.rnd.ngiis.av1;

public class OBUHeader {
    private int forbiddenBit;
    private OBUType type;
    private boolean extensionFlag;
    private boolean hasSizeField;
    private int temporalId;
    private int spatialId;

    public int getForbiddenBit() {
        return forbiddenBit;
    }

    public void setForbiddenBit(int forbiddenBit) {
        this.forbiddenBit = forbiddenBit;
    }

    public OBUType getType() {
        return type;
    }

    public void setType(OBUType type) {
        this.type = type;
    }

    public boolean isExtensionFlag() {
        return extensionFlag;
    }

    public void setExtensionFlag(boolean extensionFlag) {
        this.extensionFlag = extensionFlag;
    }

    public boolean isHasSizeField() {
        return hasSizeField;
    }

    public void setHasSizeField(boolean hasSizeField) {
        this.hasSizeField = hasSizeField;
    }

    public int getTemporalId() {
        return temporalId;
    }

    public void setTemporalId(int temporalId) {
        this.temporalId = temporalId;
    }

    public int getSpatialId() {
        return spatialId;
    }

    public void setSpatialId(int spatialId) {
        this.spatialId = spatialId;
    }
}
