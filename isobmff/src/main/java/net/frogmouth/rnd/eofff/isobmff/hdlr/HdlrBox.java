package net.frogmouth.rnd.eofff.isobmff.hdlr;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class HdlrBox extends FullBox {
    private int preDefined;
    private  String handlerType;
    private  int reserved0;
    private int reserved1;
    private  int reserved2;
    private String name;

    public HdlrBox(long size, String name) {
        super(size, name);
    }
    
    @Override
    public String getFullName() {
        return "HandlerBox";
    }
    
    public String getFourCC() {
        return "hdlr";
    }

    public int getPreDefined() {
        return preDefined;
    }

    public void setPreDefined(int preDefined) {
        this.preDefined = preDefined;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public int getReserved0() {
        return reserved0;
    }

    public void setReserved0(int reserved0) {
        this.reserved0 = reserved0;
    }

    public int getReserved1() {
        return reserved1;
    }

    public void setReserved1(int reserved1) {
        this.reserved1 = reserved1;
    }

    public int getReserved2() {
        return reserved2;
    }

    public void setReserved2(int reserved2) {
        this.reserved2 = reserved2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': '");
        sb.append(getHandlerType());
        sb.append("'");
        return sb.toString();
    }
    
}
