package net.frogmouth.rnd.eofff.uncompressed.sbpm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sensor Bad Pixel Map Box.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 6.1.7.
 */
public class SensorBadPixelsMapBox extends FullBox {

    public static final FourCC SBPM_ATOM = new FourCC("sbpm");
    private final List<Integer> componentIndexes = new ArrayList<>();
    private boolean correctionApplied;

    public SensorBadPixelsMapBox() {
        super(SBPM_ATOM);
    }

    @Override
    public String getFullName() {
        return "SensorBadPixelsMap";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        // TODO
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long count = 0;
        count += Short.BYTES;
        count += (this.componentIndexes.size() * Short.BYTES);
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
        stream.writeByte(correctionApplied ? (byte) 0x80 : 0x00);
    }

    public List<Integer> getComponentIndexes() {
        return new ArrayList<>(componentIndexes);
    }

    public void addComponentIndex(int component_index) {
        this.componentIndexes.add(component_index);
    }

    public boolean isCorrectionApplied() {
        return correctionApplied;
    }

    public void setCorrectionApplied(boolean correctionApplied) {
        this.correctionApplied = correctionApplied;
    }
}
