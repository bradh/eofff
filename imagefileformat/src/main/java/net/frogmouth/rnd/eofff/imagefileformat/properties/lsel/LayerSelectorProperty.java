package net.frogmouth.rnd.eofff.imagefileformat.properties.lsel;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;

/**
 * Layer Selector Property (lsel).
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.5.11
 */
public class LayerSelectorProperty extends ItemProperty {
    public static final FourCC LSEL_ATOM = new FourCC("lsel");

    private int layerID;

    public LayerSelectorProperty() {
        super(LSEL_ATOM);
    }

    public int getLayerID() {
        return layerID;
    }

    public void setLayerID(int layerID) {
        this.layerID = layerID;
    }

    @Override
    public long getBodySize() {
        return Short.BYTES;
    }

    @Override
    public String getFullName() {
        return "LayerSelectorProperty";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt16(layerID);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("layer_id=");
        sb.append(layerID);
        return sb.toString();
    }
}
