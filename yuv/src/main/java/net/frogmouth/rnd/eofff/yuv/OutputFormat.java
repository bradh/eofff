package net.frogmouth.rnd.eofff.yuv;

public interface OutputFormat {

    public void putRGB(int r, int g, int b);

    public byte[] getBytes();
}
