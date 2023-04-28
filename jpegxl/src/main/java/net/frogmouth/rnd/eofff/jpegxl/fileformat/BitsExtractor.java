package net.frogmouth.rnd.eofff.jpegxl.fileformat;

/**
 * Extract bits from byte array at offset.
 *
 * <p>This is modified from https://stackoverflow.com/a/40767932/1413799 by
 * https://stackoverflow.com/users/5801661/margaret-bloom (CC-BY-SA).
 */
public class BitsExtractor {

    private byte[] bits;
    private int currentByte = 0;
    private int bitsLeftInByte = Byte.SIZE;

    public BitsExtractor(byte[] bits) {
        this.bits = bits;
    }

    /**
     * Get bits, starting at least significant bit.
     *
     * @param numBits
     * @return
     */
    public int readBits(final int numBits) {
        int bitsStillRequired = numBits;
        int res = 0;
        while ((currentByte < bits.length) && (bitsStillRequired > 0)) {
            int numBitsToExtract = Math.min(bitsStillRequired, bitsLeftInByte);
            int byteMask = (1 << numBitsToExtract) - 1;
            int newBits =
                    (bits[currentByte] & (byteMask << (Byte.SIZE - bitsLeftInByte)))
                            >> (Byte.SIZE - bitsLeftInByte);
            res = (res << numBitsToExtract) | newBits;
            bitsLeftInByte -= numBitsToExtract;
            if (bitsLeftInByte == 0) {
                bitsLeftInByte = Byte.SIZE;
                currentByte++;
            }
            bitsStillRequired -= numBitsToExtract;
        }
        return res;
    }
}
