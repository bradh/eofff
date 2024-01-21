package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class FileTypeLikeBox extends BaseBox {

    protected Brand majorBrand;
    protected int minorVersion;
    protected final List<Brand> compatibleBrands = new ArrayList<>();

    public FileTypeLikeBox(FourCC name) {
        super(name);
    }

    // @Override
    public long getSize() {
        long size =
                Integer.BYTES
                        + FourCC.BYTES
                        + FourCC.BYTES
                        + Integer.BYTES
                        + FourCC.BYTES * compatibleBrands.size();
        return size;
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
        stream.writeInt(minorVersion);
        for (FourCC brand : this.compatibleBrands) {
            stream.writeFourCC(brand);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("major-brand=");
        sb.append(majorBrand);
        sb.append(", compatible-brands='");
        List<String> brands = new ArrayList<>();
        compatibleBrands.forEach(
                brand -> {
                    brands.add(brand.toString());
                });
        sb.append(String.join(",", brands));
        sb.append("'");
        return sb.toString();
    }
}
