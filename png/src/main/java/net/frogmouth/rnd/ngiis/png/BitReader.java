package net.frogmouth.rnd.ngiis.png;

public class BitReader {

    private final byte[] data;
    private int currentByte = 0;
    private int bitsLeftInByte = Byte.SIZE;

    public BitReader(byte[] bytes) {
        this.data = bytes;
    }

    /**
     * Get bits, starting with the most significant bits.
     *
     * @param numBits the number of bits
     * @return the bits as an integer
     */
    public int readBits(final int numBits) {
        int bitsStillRequired = numBits;
        int res = 0;

        while ((currentByte < data.length) && (bitsStillRequired > 0)) {
            int numBitsToExtract = Math.min(bitsStillRequired, bitsLeftInByte);
            int byteMask = (1 << numBitsToExtract) - 1;
            int lowBitOffset = (bitsLeftInByte - numBitsToExtract);
            int newBits = (data[currentByte] & (byteMask << lowBitOffset)) >> lowBitOffset;
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

    public boolean hasRemaining() {
        return ((currentByte <= (data.length - 1)) && (bitsLeftInByte > 0));
    }
}
