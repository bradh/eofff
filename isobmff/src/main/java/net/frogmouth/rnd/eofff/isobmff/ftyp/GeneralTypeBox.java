package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * General Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 4.3.
 */
public abstract class GeneralTypeBox extends BaseBox {

    protected Brand majorBrand;
    protected int minorVersion;
    protected final List<Brand> compatibleBrands = new ArrayList<>();

    public GeneralTypeBox(FourCC name) {
        super(name);
    }

    @Override
    public long getBodySize() {
        long size = FourCC.BYTES + Integer.BYTES + FourCC.BYTES * compatibleBrands.size();
        return size;
    }

    public FourCC getMajorBrand() {
        return majorBrand;
    }

    public void setMajorBrand(Brand majorBrand) {
        this.majorBrand = majorBrand;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public List<Brand> getCompatibleBrands() {
        return new ArrayList<>(compatibleBrands);
    }

    public void addCompatibleBrand(Brand compatibleBrand) {
        this.compatibleBrands.add(compatibleBrand);
    }

    public void removeCompatibleBrand(Brand brand) {
        this.compatibleBrands.remove(brand);
    }

    public void appendCompatibleBrand(Brand brand) {
        addCompatibleBrand(brand);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeFourCC(this.majorBrand);
        stream.writeUnsignedInt32(minorVersion);
        for (FourCC brand : this.compatibleBrands) {
            stream.writeFourCC(brand);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("major-brand='");
        sb.append(majorBrand);
        sb.append("', compatible-brands=");
        compatibleBrands.forEach(
                brand -> {
                    sb.append("'");
                    sb.append(brand.toString());
                    sb.append("' ");
                });
        return sb.toString();
    }
}
