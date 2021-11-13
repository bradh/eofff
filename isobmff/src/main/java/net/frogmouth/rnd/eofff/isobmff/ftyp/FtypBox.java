package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FtypBox extends BaseBox {

    private FourCC majorBrand;
    private int minorVersion;
    private final List<FourCC> compatibleBrands = new ArrayList<>();

    public FtypBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "FileTypeBox";
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
