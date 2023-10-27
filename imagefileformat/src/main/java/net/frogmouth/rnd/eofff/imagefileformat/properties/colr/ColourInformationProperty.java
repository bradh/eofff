package net.frogmouth.rnd.eofff.imagefileformat.properties.colr;

import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ColourInformationProperty extends ItemProperty {
    public static final FourCC COLR_ATOM = new FourCC("colr");
    private static final FourCC NCLX = new FourCC("nclx");
    private static final FourCC RICC = new FourCC("rICC");
    private static final FourCC PROF = new FourCC("prof");

    private FourCC colourType;
    // for nclx on-screen case
    private short colourPrimaries;
    private short transferCharacteristics;
    private short matrixCoefficients;
    private boolean fullRange;

    public ColourInformationProperty() {
        super(COLR_ATOM);
    }

    public FourCC getColourType() {
        return colourType;
    }

    public void setColourType(FourCC colourType) {
        this.colourType = colourType;
    }

    public short getColourPrimaries() {
        return colourPrimaries;
    }

    public void setColourPrimaries(short colourPrimaries) {
        this.colourPrimaries = colourPrimaries;
    }

    public short getTransferCharacteristics() {
        return transferCharacteristics;
    }

    public void setTransferCharacteristics(short transferCharacteristics) {
        this.transferCharacteristics = transferCharacteristics;
    }

    public short getMatrixCoefficients() {
        return matrixCoefficients;
    }

    public void setMatrixCoefficients(short matrixCoefficients) {
        this.matrixCoefficients = matrixCoefficients;
    }

    public boolean isFullRange() {
        return fullRange;
    }

    public void setFullRange(boolean fullRange) {
        this.fullRange = fullRange;
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += FourCC.BYTES;
        if (colourType.equals(NCLX)) {
            size += Short.BYTES;
            size += Short.BYTES;
            size += Short.BYTES;
            size += Byte.BYTES;
        } else if (colourType.equals(RICC)) {
            throw new UnsupportedOperationException("implement rICC");
        } else if (colourType.equals(PROF)) {
            throw new UnsupportedOperationException("implement prof");
        } else {
            throw new UnsupportedOperationException("implement " + colourType.toString());
        }
        return size;
    }

    @Override
    public String getFullName() {
        return "ColourInformationProperty";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeFourCC(colourType);
        if (colourType.equals(NCLX)) {
            writer.writeShort(colourPrimaries);
            writer.writeShort(transferCharacteristics);
            writer.writeShort(matrixCoefficients);
            writer.writeByte(this.fullRange ? 0x80 : 0x00);
        } else {
            throw new UnsupportedOperationException("implement write of " + colourType.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': colour_type=");
        sb.append(colourType);
        if (colourType.equals(NCLX)) {
            sb.append(", colour_primaries=");
            sb.append(String.format("0x%04x", this.colourPrimaries));
            sb.append(", transfer_characteristics=");
            sb.append(String.format("0x%04x", this.transferCharacteristics));
            sb.append(", matrix_coefficients=");
            sb.append(String.format("0x%04x", this.matrixCoefficients));
            sb.append(", full_range_flag=");
            sb.append((this.fullRange ? "1" : "0"));
        } else {
            throw new UnsupportedOperationException(
                    "implement toString() for " + colourType.toString());
        }
        return sb.toString();
    }
}
