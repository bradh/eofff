package net.frogmouth.rnd.ngiis.png;

public class ChunkParserPLTE extends ChunkParser {

    public ChunkParserPLTE() {}

    @Override
    PngChunk parse(PngParseContext parser, long chunkSize) {
        ChunkPLTE chunk = new ChunkPLTE();
        for (int i = 0; i < chunkSize / 3; i++) {
            int red = parser.readUnsignedInt8();
            int green = parser.readUnsignedInt8();
            int blue = parser.readUnsignedInt8();
            PaletteEntry entry = new PaletteEntry(red, green, blue);
            chunk.addPaletteEntry(entry);
        }
        return chunk;
    }
}
