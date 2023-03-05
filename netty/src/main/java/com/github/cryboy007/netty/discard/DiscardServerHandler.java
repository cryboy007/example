package com.github.cryboy007.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 *@ClassName DiscardServerHandler
 *@Author tao.he
 *@Since 2023/3/5 11:12
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		try {
//			while (in.isReadable()) {
//				System.out.println((char) in.readByte());
//				System.out.flush();
//			}
			System.out.println(in.toString(CharsetUtil.UTF_8));
		}
		finally {
			//in.release();
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
