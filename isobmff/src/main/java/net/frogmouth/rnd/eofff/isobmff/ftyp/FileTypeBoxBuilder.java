package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.util.ArrayList;
import java.util.List;

public class FileTypeBoxBuilder {

    private Brand majorBrand;
    private int minorVersion;
    private final List<Brand> compatibleBrands = new ArrayList<>();

    public FileTypeBoxBuilder() {}

    public FileTypeBoxBuilder withMajorBrand(Brand fourCC) {
        this.majorBrand = fourCC;
        return this;
    }

    public FileTypeBoxBuilder withMinorVersion(int version) {
        this.minorVersion = version;
        return this;
    }

    public FileTypeBoxBuilder addCompatibleBrand(Brand brand) {
        this.compatibleBrands.add(brand);
        return this;
    }

    public FileTypeBox build() {
        FileTypeBox box = new FileTypeBox();
        box.setMajorBrand(this.majorBrand);
        box.setMinorVersion(this.minorVersion);
        for (Brand brand : this.compatibleBrands) {
            box.addCompatibleBrand(brand);
        }
        return box;
    }
}
