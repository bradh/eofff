package net.frogmouth.rnd.ngiis.png;

public class ChunkParserIEND extends ChunkParser {

    public ChunkParserIEND() {}

    @Override
    PngChunk parse(PngParseContext parser, long chunkSize) {
        assert (chunkSize == 0);
        return new ChunkIEND();
    }
}
