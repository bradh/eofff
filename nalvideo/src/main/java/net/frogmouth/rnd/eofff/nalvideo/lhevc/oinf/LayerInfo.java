package net.frogmouth.rnd.eofff.nalvideo.lhevc.oinf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

class LayerInfo {

    private int layerID;
    private final List<Integer> directRefLayerIDs = new ArrayList<>();
    private final List<Integer> dimensionIdentifiers = new ArrayList<>();

    void parseFrom(ParseContext parseContext, int scaleabilityMask) {
        layerID = parseContext.readUnsignedInt8();
        int numDirectRefLayers = parseContext.readUnsignedInt8();
        for (int j = 0; j < numDirectRefLayers; j++) {
            directRefLayerIDs.add(parseContext.readUnsignedInt8());
        }
        for (int j = 0; j < 16; j++) {
            if ((scaleabilityMask & (1 << j)) != 0) {
                dimensionIdentifiers.add(parseContext.readUnsignedInt8());
            } else {
                dimensionIdentifiers.add(-1);
            }
        }
    }

    public int getLayerID() {
        return layerID;
    }

    public void setLayerID(int layerID) {
        this.layerID = layerID;
    }

    public List<Integer> getDirectRefLayerIDs() {
        return directRefLayerIDs;
    }

    public void addDirectRefLayerId(int id) {
        directRefLayerIDs.add(id);
    }

    public List<Integer> getDimensionIdentifiers() {
        return dimensionIdentifiers;
    }

    public void addDimensionIdentifier(int id) {
        dimensionIdentifiers.add(id);
    }
}
