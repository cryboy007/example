package com.github.cryboy007.netty.time;

import com.github.cryboy007.netty.pojo.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *@ClassName TimeClient
 *@Author tao.he
 *@Since 2023/3/5 18:11
 */
public class TimeClient {
	public static void main(String[] args) throws InterruptedException {
		String host = args[0];
		int port = Integer.parseInt(args[1]);

		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE,true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new TimeDecoder());
							ch.pipeline().addLast(new TimeClientHandler());
						}
					});
			// 启动客户端
			ChannelFuture f = b.connect(host, port).sync(); // (5)
			// 等待连接关闭
			f.channel().closeFuture().sync();
		}
		finally {
			workGroup.shutdownGracefully();
		}

	}
}
