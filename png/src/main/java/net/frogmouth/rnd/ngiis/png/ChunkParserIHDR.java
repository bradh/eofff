package net.frogmouth.rnd.ngiis.png;

/**
 * @author bradh
 */
public class ChunkParserIHDR extends ChunkParser {

    public ChunkParserIHDR() {}

    @Override
    PngChunk parse(PngParseContext parser, long chunkSize) {
        ChunkIHDR chunk = new ChunkIHDR();
        chunk.setWidth((int) parser.readUnsignedInt32());
        chunk.setHeight((int) parser.readUnsignedInt32());
        chunk.setBitDepth(parser.readUnsignedInt8());
        chunk.setColorType(parser.readUnsignedInt8());
        chunk.setCompressionMethod(parser.readUnsignedInt8());
        chunk.setFilterMethod(parser.readUnsignedInt8());
        chunk.setInterlaceMethod(parser.readUnsignedInt8());
        return chunk;
    }
}
