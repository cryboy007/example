package com.github.cryboy007.netty.time;

import com.github.cryboy007.netty.pojo.UnixTime;
import io.netty.channel.*;

/**
 *@ClassName TimeServerHandler
 *@Author tao.he
 *@Since 2023/3/5 12:10
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	//会在连接被建立并且准备进行通信时被调用。因此让我们在这个方法里完成一个代表当前时间的 32 位整数消息的构建工作。
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		final ByteBuf time = ctx.alloc().buffer(4);
//		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//		// 出站操作返回ChannelFuture
//		final ChannelFuture f = ctx.writeAndFlush(time);
		//改用pojo
		ChannelFuture f = ctx.writeAndFlush(new UnixTime());
		// 增加监听器
		f.addListener(new ChannelFutureListener() {
			// 操作完成，关闭管道
			@Override
			public void operationComplete(ChannelFuture future) {
				ctx.close();
			}
		});
		//f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
