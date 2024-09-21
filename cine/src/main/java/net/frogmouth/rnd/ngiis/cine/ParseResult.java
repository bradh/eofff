package net.frogmouth.rnd.ngiis.cine;

import java.util.ArrayList;
import java.util.List;

public class ParseResult {

    private final CineParseContext parser;
    private long offImageOffsets;
    private long imageCount;
    private final List<Long> imageOffsets = new ArrayList<>();
    private final List<Time64> timestamps = new ArrayList<>();

    ParseResult(CineParseContext parser) {
        this.parser = parser;
    }

    public void addImageOffset(long offset) {
        imageOffsets.add(offset);
    }

    public void addTimestamp(Time64 timestamp) {
        timestamps.add(timestamp);
    }

    public long getOffImageOffsets() {
        return offImageOffsets;
    }

    public void setOffImageOffsets(long offImageOffsets) {
        this.offImageOffsets = offImageOffsets;
    }

    public long getImageCount() {
        return imageCount;
    }

    public void setImageCount(long imageCount) {
        this.imageCount = imageCount;
    }

    public CineImage getImage(int imageIndex) {
        long offset = this.imageOffsets.get(imageIndex);
        parser.skipToOffset(offset);
        return parser.readCineImage();
    }

    public Time64 getTimestamp(int imageIndex) {
        return timestamps.get(imageIndex);
    }
}
