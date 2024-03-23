package net.frogmouth.rnd.eofff.nalvideo.lhevc.oinf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class OperatingPoint {

    public record Layer(
            int ptlIdx, int layerId, boolean outputLayer, boolean alternateOutputLayer) {}
    ;

    private int outputLayerSetIdx;
    private int maxTemporalId;
    private final List<Layer> layers = new ArrayList<>();
    private int minPicWidth;
    private int minPicHeight;
    private int maxPicWidth;
    private int maxPicHeight;
    private int maxChromaFormat;
    private int maxBitDepthMinus8;
    private Integer avgFrameRate = null;
    private Integer constantFrameRate = null;
    private Long maxBitRate = null;
    private Long avgBitRate = null;

    public int getOutputLayerSetIdx() {
        return outputLayerSetIdx;
    }

    public void setOutputLayerSetIdx(int outputLayerSetIdx) {
        this.outputLayerSetIdx = outputLayerSetIdx;
    }

    public int getMaxTemporalId() {
        return maxTemporalId;
    }

    public void setMaxTemporalId(int maxTemporalId) {
        this.maxTemporalId = maxTemporalId;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public int getMinPicWidth() {
        return minPicWidth;
    }

    public void setMinPicWidth(int minPicWidth) {
        this.minPicWidth = minPicWidth;
    }

    public int getMinPicHeight() {
        return minPicHeight;
    }

    public void setMinPicHeight(int minPicHeight) {
        this.minPicHeight = minPicHeight;
    }

    public int getMaxPicWidth() {
        return maxPicWidth;
    }

    public void setMaxPicWidth(int maxPicWidth) {
        this.maxPicWidth = maxPicWidth;
    }

    public int getMaxPicHeight() {
        return maxPicHeight;
    }

    public void setMaxPicHeight(int maxPicHeight) {
        this.maxPicHeight = maxPicHeight;
    }

    public int getMaxChromaFormat() {
        return maxChromaFormat;
    }

    public void setMaxChromaFormat(int maxChromaFormat) {
        this.maxChromaFormat = maxChromaFormat;
    }

    public int getMaxBitDepthMinus8() {
        return maxBitDepthMinus8;
    }

    public void setMaxBitDepthMinus8(int maxBitDepthMinus8) {
        this.maxBitDepthMinus8 = maxBitDepthMinus8;
    }

    public Integer getAvgFrameRate() {
        return avgFrameRate;
    }

    public void setAvgFrameRate(Integer avgFrameRate) {
        this.avgFrameRate = avgFrameRate;
    }

    public Integer getConstantFrameRate() {
        return constantFrameRate;
    }

    public void setConstantFrameRate(Integer constantFrameRate) {
        this.constantFrameRate = constantFrameRate;
    }

    public Long getMaxBitRate() {
        return maxBitRate;
    }

    public void setMaxBitRate(Long maxBitRate) {
        this.maxBitRate = maxBitRate;
    }

    public Long getAvgBitRate() {
        return avgBitRate;
    }

    public void setAvgBitRate(Long avgBitRate) {
        this.avgBitRate = avgBitRate;
    }

    void parseFrom(ParseContext parseContext) {
        outputLayerSetIdx = parseContext.readUnsignedInt16();
        maxTemporalId = parseContext.readUnsignedInt8();
        int layerCount = parseContext.readUnsignedInt8();
        for (int j = 0; j < layerCount; j++) {
            int ptlIdx = parseContext.readUnsignedInt8();
            int v = parseContext.readUnsignedInt8();
            int layerId = v >> 2;
            boolean isOutputLayer = ((v & 0x02) == 0x02);
            boolean isAlternateOutputLayer = ((v & 0x01) == 0x01);
            layers.add(new Layer(ptlIdx, layerId, isOutputLayer, isAlternateOutputLayer));
        }
        minPicWidth = parseContext.readUnsignedInt16();
        minPicHeight = parseContext.readUnsignedInt16();
        maxPicWidth = parseContext.readUnsignedInt16();
        maxPicHeight = parseContext.readUnsignedInt16();
        int v = parseContext.readUnsignedInt8();
        maxChromaFormat = v >> 6;
        maxBitDepthMinus8 = ((v & 0x38) >> 3);
        boolean frameRateInfoFlag = ((v & 0x2) == 0x02);
        boolean bitRateInfoFlag = ((v & 0x1) == 0x01);
        if (frameRateInfoFlag) {
            this.avgFrameRate = parseContext.readUnsignedInt16();
            this.constantFrameRate = (parseContext.readUnsignedInt8() & 0x03);
        }
        if (bitRateInfoFlag) {
            this.maxBitRate = parseContext.readUnsignedInt32();
            this.avgBitRate = parseContext.readUnsignedInt32();
        }
    }
}
