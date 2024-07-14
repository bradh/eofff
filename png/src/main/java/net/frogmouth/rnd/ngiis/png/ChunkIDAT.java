package net.frogmouth.rnd.ngiis.png;

public class ChunkIDAT extends PngChunk {
    private byte[] decompressedData;

    public byte[] getDecompressedData() {
        return decompressedData;
    }

    public void setDecompressedData(byte[] decompressedData) {
        this.decompressedData = decompressedData;
    }

    @Override
    public String toString() {
        return "IDAT. decompressedData length=" + decompressedData.length;
    }
}
