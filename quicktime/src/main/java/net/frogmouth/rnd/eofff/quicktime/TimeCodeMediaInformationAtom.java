package net.frogmouth.rnd.eofff.quicktime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Timecode media information atom.
 *
 * <p>See
 * https://developer.apple.com/documentation/quicktime-file-format/timecode_media_information_atom
 */
public class TimeCodeMediaInformationAtom extends FullBox {

    private short textFont;
    private short textFace;
    private short textSize;
    private short textcolorRed;
    private short textcolorGreen;
    private short textcolorBlue;
    private short backgroundColorRed;
    private short backgroundColorGreen;
    private short backgroundColorBlue;
    private String fontName;

    public static final FourCC TCMI_ATOM = new FourCC("tcmi");

    public TimeCodeMediaInformationAtom() {
        super(TCMI_ATOM);
    }

    @Override
    public String getFullName() {
        return "Timecode media information atom";
    }

    public short getTextFont() {
        return textFont;
    }

    public void setTextFont(short textFont) {
        this.textFont = textFont;
    }

    public short getTextFace() {
        return textFace;
    }

    public void setTextFace(short textFace) {
        this.textFace = textFace;
    }

    public short getTextSize() {
        return textSize;
    }

    public void setTextSize(short textSize) {
        this.textSize = textSize;
    }

    public short getTextcolorRed() {
        return textcolorRed;
    }

    public void setTextcolorRed(short textcolorRed) {
        this.textcolorRed = textcolorRed;
    }

    public short getTextcolorGreen() {
        return textcolorGreen;
    }

    public void setTextcolorGreen(short textcolorGreen) {
        this.textcolorGreen = textcolorGreen;
    }

    public short getTextcolorBlue() {
        return textcolorBlue;
    }

    public void setTextcolorBlue(short textcolorBlue) {
        this.textcolorBlue = textcolorBlue;
    }

    public short getBackgroundColorRed() {
        return backgroundColorRed;
    }

    public void setBackgroundColorRed(short backgroundColorRed) {
        this.backgroundColorRed = backgroundColorRed;
    }

    public short getBackgroundColorGreen() {
        return backgroundColorGreen;
    }

    public void setBackgroundColorGreen(short backgroundColorGreen) {
        this.backgroundColorGreen = backgroundColorGreen;
    }

    public short getBackgroundColorBlue() {
        return backgroundColorBlue;
    }

    public void setBackgroundColorBlue(short backgroundColorBlue) {
        this.backgroundColorBlue = backgroundColorBlue;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeShort(textFont);
        stream.writeShort(textFace);
        stream.writeShort(textSize);
        stream.writeShort((short) 0);
        stream.writeShort(textcolorRed);
        stream.writeShort(textcolorGreen);
        stream.writeShort(textcolorBlue);
        stream.writeShort(backgroundColorRed);
        stream.writeShort(backgroundColorGreen);
        stream.writeShort(backgroundColorBlue);
        byte[] fontNameBytes = fontName.getBytes(StandardCharsets.US_ASCII);
        stream.writeByte(fontNameBytes.length);
        stream.write(fontNameBytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("text font=");
        sb.append(textFont);
        sb.append(", text face=");
        sb.append(textFace);
        sb.append(", text size=");
        sb.append(textSize);
        sb.append(", text color=[0x");
        sb.append(HexFormat.of().toHexDigits(textcolorRed));
        sb.append(",0x");
        sb.append(HexFormat.of().toHexDigits(textcolorGreen));
        sb.append(",0x");
        sb.append(HexFormat.of().toHexDigits(textcolorBlue));
        sb.append("], background color=[0x");
        sb.append(HexFormat.of().toHexDigits(backgroundColorRed));
        sb.append(",0x");
        sb.append(HexFormat.of().toHexDigits(backgroundColorGreen));
        sb.append(",0x");
        sb.append(HexFormat.of().toHexDigits(backgroundColorBlue));
        sb.append("], font name=");
        sb.append(fontName);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Short.BYTES; // reserved
        size += Short.BYTES * 3; // text color
        size += Short.BYTES * 3; // background color
        size += fontName.getBytes(StandardCharsets.US_ASCII).length;
        size += 1; // for the length
        return size;
    }
}
