package net.frogmouth.rnd.eofff.uncompressed.depi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Depth Mapping Information.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 6.1.11.
 */
public class DepthInfoBox extends ItemFullProperty {

    public static final FourCC DEPI_ATOM = new FourCC("depi");
    private final List<Integer> componentIndexes = new ArrayList<>();
    private int nknear = 128;
    private int nkfar = 128;

    public DepthInfoBox() {
        super(DEPI_ATOM);
    }

    @Override
    public String getFullName() {
        return "DepthInfoBox";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': nkNear: ");
        sb.append(nknear);
        sb.append(", nkFar: ");
        sb.append(nkfar);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long count = 0;
        count += Short.BYTES;
        count += (this.componentIndexes.size() * Short.BYTES);
        count += Byte.BYTES;
        count += Byte.BYTES;
        return count;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(this.componentIndexes.size());
        for (int i = 0; i < this.componentIndexes.size(); i++) {
            stream.writeUnsignedInt16(this.componentIndexes.get(i));
        }
        stream.writeByte(nknear);
        stream.writeByte(nkfar);
    }

    public List<Integer> getComponentIndexes() {
        return new ArrayList<>(componentIndexes);
    }

    public void addComponentIndex(int component_index) {
        this.componentIndexes.add(component_index);
    }

    public int getNknear() {
        return nknear;
    }

    public void setNknear(int nknear) {
        this.nknear = nknear;
    }

    public int getNkfar() {
        return nkfar;
    }

    public void setNkfar(int nkfar) {
        this.nkfar = nkfar;
    }
}
