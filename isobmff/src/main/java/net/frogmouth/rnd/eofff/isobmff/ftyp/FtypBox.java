package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;

public class FtypBox extends BaseBox {

    private String majorBrand;
    private int minorVersion;
    private final List<String> compatibleBrands = new ArrayList<>();

    public FtypBox(long size, String name) {
        super(size, name);
    }
    
    @Override
    public String getFullName() {
        return "FileTypeBox";
    }
    
    public String getFourCC() {
        return "ftyp";
    }

    public String getMajorBrand() {
        return majorBrand;
    }

    public void setMajorBrand(String majorBrand) {
        this.majorBrand = majorBrand;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public List<String> getCompatibleBrands() {
        return new ArrayList<>(compatibleBrands);
    }

    public void addCompatibleBrand(String compatibleBrand) {
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
        sb.append(String.join(",",compatibleBrands));
        sb.append("'");
        return sb.toString();
    }
    
}
