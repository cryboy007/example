package com.github.cryboy007.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *@ClassName Main
 *@Author tao.he
 *@Since 2023/3/5 11:34
 */
public class DiscardServer {

	private int port;
	public DiscardServer(int port) {
		this.port = port;
	}

	public void run() throws InterruptedException {
		//第一个经常被叫做‘boss’，用来接收进来的连接。第二个经常被叫做‘worker’，用来处理已经被接收的连接，一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
		EventLoopGroup bossGroup  = new NioEventLoopGroup();
		EventLoopGroup  workerGroup  = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
					//一个新的 Channel 如何接收进来的连接
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline().addLast(new DiscardServerHandler());
						}
					})
					//option() 是提供给NioServerSocketChannel
					// 用来接收进来的连接。childOption() 是提供给由父管道 ServerChannel 接收到的连接，在这个例子中也是 NioServerSocketChannel。
					.option(ChannelOption.SO_BACKLOG,128)
					.childOption(ChannelOption.SO_KEEPALIVE,true)
			;
			// 绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}
		finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new DiscardServer(port).run();
	}
}
