package com.github.cryboy007.netty.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 *@ClassName TimeDecoder
 *@Author tao.he
 *@Since 2023/3/5 18:40
 */
public class TimeDecoder extends ByteToMessageDecoder {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
//		out.add(in.readBytes(4));
		out.add(new UnixTime(in.readUnsignedInt()));
	}
}
