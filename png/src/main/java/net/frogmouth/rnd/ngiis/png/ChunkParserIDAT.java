package net.frogmouth.rnd.ngiis.png;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ChunkParserIDAT extends ChunkParser {

    public ChunkParserIDAT() {}

    @Override
    PngChunk parse(PngParseContext parser, long chunkSize) {
        ChunkIDAT chunk = new ChunkIDAT();
        byte[] compressedData = parser.getBytes(chunkSize);
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            try {
                int validBytes = inflater.inflate(buffer);
                // System.out.println(" valid bytes: " + validBytes);
                baos.write(buffer, 0, validBytes);
            } catch (DataFormatException ex) {
                ex.printStackTrace();
            }
        }
        byte[] decompressedData = baos.toByteArray();
        chunk.setDecompressedData(decompressedData);
        // System.out.println(
        //         HexFormat.of().withDelimiter(" ").withPrefix("0x").formatHex(decompressedData));
        return chunk;
    }
}
