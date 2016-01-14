package org.mobicents.protocols.ss7.m3ua.impl.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import org.testng.annotations.Test;

public class ByteBufTest {
    @Test
    public void testSingleAspInAs() throws Exception {
        try {
            CompositeByteBuf comp = Unpooled.compositeBuffer();
            ByteBuf top = comp;

            ByteBuf b1 = Unpooled.buffer();
            ByteBuf b2 = Unpooled.buffer();
            ByteBuf b3 = Unpooled.buffer();
            b1.writeByte(1);
            b1.writeByte(2);
            b1.writeByte(3);
            b2.writeByte(11);
            b2.writeByte(12);
            b3.writeByte(21);
            b3.writeByte(22);
            b3.writeByte(23);
            b3.writeByte(24);

            comp.addComponent(b1);
            comp.addComponent(b2);
            comp.writerIndex(comp.capacity());

            byte[] bb = new byte[4];
            comp.readBytes(bb);

            comp.discardReadBytes();

            comp.addComponent(b3);
            comp.writerIndex(comp.capacity());

            byte[] bb2 = new byte[5];
            comp.readBytes(bb2);

            comp.discardReadBytes();
            int xxx = 0;
            xxx++;
        } catch (Exception ee) {
            int iii = 10;
        }
    }
}
