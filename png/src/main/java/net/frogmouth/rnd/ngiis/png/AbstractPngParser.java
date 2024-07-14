package net.frogmouth.rnd.ngiis.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractPngParser {

    private static final long PNG_SIGNATURE_LEN = 8;
    private static final byte[] PNG_SIGNATURE_BYTES =
            new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
    private static final long CHECKSUM_SIZE = 4;

    protected List<PngChunk> parseMemorySegment(MemorySegment segment) throws IOException {
        PngParseContext parser = new PngParseContext(segment);
        parseHeader(parser);
        List<PngChunk> chunks = new ArrayList<>();
        while (parser.hasRemaining()) {
            long chunkSize = parser.readUnsignedInt32();
            // System.out.println("chunkSize: " + chunkSize);
            PngChunkType chunkType = parser.readChunkType();
            ChunkParser chunkParser = ChunkFactoryManager.getParser(chunkType);
            PngChunk chunk = chunkParser.parse(parser, chunkSize);
            // TODO: validate checksum
            parser.skipBytes(CHECKSUM_SIZE);
            chunks.add(chunk);
            // System.out.println(chunk);
        }
        if (parser.hasRemaining()) {
            System.out.println(
                    "parsed only "
                            + parser.getCursorPosition()
                            + " of "
                            + segment.byteSize()
                            + " bytes");
        }
        return chunks;
    }

    private void parseHeader(PngParseContext parser) throws IOException {
        byte[] signature = parser.getBytes(PNG_SIGNATURE_LEN);
        if (!Arrays.equals(signature, PNG_SIGNATURE_BYTES)) {
            throw new IOException("Not a PNG file");
        }
    }

    public BufferedImage render(MemorySegment segment) throws IOException {
        List<PngChunk> chunks = parseMemorySegment(segment);
        ChunkIHDR ihdr = null;
        ChunkPLTE plte = null;
        ChunkIDAT idat = null;
        for (PngChunk chunk : chunks) {
            if (chunk instanceof ChunkIHDR chunkIHDR) {
                ihdr = chunkIHDR;
            }
            if (chunk instanceof ChunkPLTE chunkPLTE) {
                plte = chunkPLTE;
            }
            if (chunk instanceof ChunkIDAT chunkIDAT) {
                idat = chunkIDAT;
            }
        }
        if (ihdr == null) {
            throw new IOException("missing IHDR from PNG file");
        }
        if (plte == null) {
            throw new IOException("missing PLTE from PNG file");
        }
        if (idat == null) {
            throw new IOException("missing IDAT from PNG file");
        }
        // TODO: we should use ihdr types here
        BufferedImage bufferedImage =
                new BufferedImage(ihdr.getWidth(), ihdr.getHeight(), BufferedImage.TYPE_INT_RGB);
        BitReader bitReader = new BitReader(idat.getDecompressedData());
        for (int y = 0; y < ihdr.getHeight(); y++) {
            int filter = bitReader.readBits(8);
            if (filter != 0) {
                throw new IOException("need to support");
            }
            for (int x = 0; x < ihdr.getWidth(); x++) {
                int paletteIndex = bitReader.readBits(ihdr.getBitDepth());
                PaletteEntry paletteEntry = plte.getEntries().get(paletteIndex);
                int rgb =
                        (paletteEntry.red() << 16)
                                | (paletteEntry.green() << 8)
                                | (paletteEntry.blue());
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        return bufferedImage;
    }
}
