package net.frogmouth.rnd.eofff.uncompressed.disi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Disparity Information.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 6.1.10.
 */
public class DisparityInformationBox extends ItemFullProperty {

    public static final FourCC DISI_ATOM = new FourCC("disi");
    private final List<Integer> componentIndexes = new ArrayList<>();
    private int parallax_zero;
    private int parallax_scale;
    private int dref;
    private int wref;

    public DisparityInformationBox() {
        super(DISI_ATOM);
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
        sb.append("': parallax_zero: ");
        sb.append(this.parallax_zero);
        sb.append(", parallax_scale: ");
        sb.append(this.parallax_scale);
        sb.append(", dref: ");
        sb.append(dref);
        sb.append(", wref: ");
        sb.append(wref);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long count = 0;
        count += Short.BYTES;
        count += (this.componentIndexes.size() * Short.BYTES);
        count += Short.BYTES;
        count += Short.BYTES;
        count += Short.BYTES;
        count += Short.BYTES;
        return count;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(this.componentIndexes.size());
        for (int i = 0; i < this.componentIndexes.size(); i++) {
            stream.writeUnsignedInt16(this.componentIndexes.get(i));
        }
        stream.writeUnsignedInt16(this.parallax_zero);
        stream.writeUnsignedInt16(this.parallax_scale);
        stream.writeUnsignedInt16(this.dref);
        stream.writeUnsignedInt16(this.wref);
    }

    public List<Integer> getComponentIndexes() {
        return new ArrayList<>(componentIndexes);
    }

    public void addComponentIndex(int component_index) {
        this.componentIndexes.add(component_index);
    }

    public int getParallax_zero() {
        return parallax_zero;
    }

    public void setParallax_zero(int parallax_zero) {
        this.parallax_zero = parallax_zero;
    }

    public int getParallax_scale() {
        return parallax_scale;
    }

    public void setParallax_scale(int parallax_scale) {
        this.parallax_scale = parallax_scale;
    }

    public int getDref() {
        return dref;
    }

    public void setDref(int dref) {
        this.dref = dref;
    }

    public int getWref() {
        return wref;
    }

    public void setWref(int wref) {
        this.wref = wref;
    }
}
