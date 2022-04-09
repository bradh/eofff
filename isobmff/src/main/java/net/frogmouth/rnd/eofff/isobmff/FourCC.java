package net.frogmouth.rnd.eofff.isobmff;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FourCC {

    public static final int BYTES = 4;
    private final int integerCode;

    public FourCC(int code) {
        integerCode = code;
    }

    public FourCC(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
        integerCode = ByteBuffer.wrap(bytes).getInt();
    }

    @Override
    public String toString() {
        ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
        bb.putInt(integerCode);
        return new String(bb.array(), StandardCharsets.US_ASCII);
    }

    @Override
    public int hashCode() {
        return integerCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FourCC other = (FourCC) obj;
        if (this.integerCode != other.integerCode) {
            return false;
        }
        return true;
    }
}
