package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FileTypeBoxBuilder {

    private FourCC majorBrand;
    private int minorVersion;
    private final List<FourCC> compatibleBrands = new ArrayList<>();

    public FileTypeBoxBuilder() {}

    public FileTypeBoxBuilder withMajorBrand(FourCC fourCC) {
        this.majorBrand = fourCC;
        return this;
    }

    public FileTypeBoxBuilder withMinorVersion(int version) {
        this.minorVersion = version;
        return this;
    }

    public FileTypeBoxBuilder addCompatibleBrand(FourCC brand) {
        this.compatibleBrands.add(brand);
        return this;
    }

    public FileTypeBox build() {
        int size =
                Integer.BYTES
                        + FourCC.BYTES
                        + FourCC.BYTES
                        + Integer.BYTES
                        + FourCC.BYTES * compatibleBrands.size();
        FileTypeBox box = new FileTypeBox(size, new FourCC("ftyp"));
        box.setMajorBrand(this.majorBrand);
        box.setMinorVersion(this.minorVersion);
        for (FourCC brand : this.compatibleBrands) {
            box.addCompatibleBrand(brand);
        }
        return box;
    }
}
