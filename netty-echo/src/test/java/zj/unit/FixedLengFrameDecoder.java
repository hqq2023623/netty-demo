package zj.unit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by lzj on 2017/4/24.
 */
public class FixedLengFrameDecoder extends ByteToMessageDecoder {

    private int length;

    public FixedLengFrameDecoder(int length) {
        this.length = length;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf b;

        while (in.readableBytes() >= 3) {
            b = in.readBytes(length);
            out.add(b);
        }


    }
}
