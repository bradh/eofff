/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.frogmouth.rnd.eofff.isobmff;

import java.nio.ByteBuffer;

public class BaseBoxParser extends BoxParser {

    @Override
    public String getFourCC() {
        throw new UnsupportedOperationException("BaseBox getFourCC() should not be called directly");
    }

    @Override
    public Box parse(ByteBuffer byteBuffer, long offset, long boxSize, String boxName) {
        BaseBox box = new BaseBox(boxSize, boxName);
        byteBuffer.position((int) (offset + boxSize));
        return box;
    }

}
