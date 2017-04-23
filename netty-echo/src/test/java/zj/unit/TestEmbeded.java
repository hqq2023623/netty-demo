package zj.unit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Created by lzj on 2017/4/23.
 */
public class TestEmbeded {


    /**
     *
     */
    @Test
    public void test01() throws Exception {
        ByteBuf b = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            b.writeByte(i);
        }
        ByteBuf input = b.copy();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengFrameDecoder(3));
        //写入9个字节
        assertTrue(channel.writeInbound(input));
        assertTrue(channel.finish());

        //读取数据
        assertEquals(b.readBytes(3), channel.readInbound());
        assertEquals(b.readBytes(3), channel.readInbound());
        assertEquals(b.readBytes(3), channel.readInbound());
        assertNull(channel.readInbound());

    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new
                FixedLengFrameDecoder(3));
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));
        assertTrue(channel.finish());

        assertEquals(buf.readBytes(3), channel.readInbound());
        assertEquals(buf.readBytes(3), channel.readInbound());
        assertEquals(buf.readBytes(3), channel.readInbound());
        assertNull(channel.readInbound());
    }

    @Test
    public void testEncoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        EmbeddedChannel channel = new EmbeddedChannel(
                new AbsIntegerEncoder());
        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());

        // read bytes
        for (int i = 1; i < 10; i++) {
            //调用 queue.poll,会删掉消息
            assertTrue(i == (Integer) channel.readOutbound());
        }
        Queue<Object> msgs = channel.outboundMessages();
        int i = 1;
        for (Object msg : msgs) {
            assertEquals(i++, msg);
        }
    }


}
