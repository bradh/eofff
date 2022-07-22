package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FileTypeLikeBox extends BaseBox {

    protected FourCC majorBrand;
    protected int minorVersion;
    protected final List<FourCC> compatibleBrands = new ArrayList<>();

    public FileTypeLikeBox(long size, FourCC name) {
        super(size, name);
    }

    public FourCC getMajorBrand() {
        return majorBrand;
    }

    public void setMajorBrand(FourCC majorBrand) {
        this.majorBrand = majorBrand;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public List<FourCC> getCompatibleBrands() {
        return new ArrayList<>(compatibleBrands);
    }

    public void addCompatibleBrand(FourCC compatibleBrand) {
        this.compatibleBrands.add(compatibleBrand);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(this.majorBrand.toBytes());
        stream.write(intToBytes(minorVersion));
        for (FourCC brand : this.compatibleBrands) {
            stream.write(brand.toBytes());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': ");
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
