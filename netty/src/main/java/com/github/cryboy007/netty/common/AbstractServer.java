package com.github.cryboy007.netty.common;

import java.util.List;
import java.util.Optional;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *@ClassName AbstractServer
 *@Author tao.he
 *@Since 2023/3/5 12:16
 */
public abstract class AbstractServer {
	private int port;
	private List<ChannelInboundHandlerAdapter> channelInboundHandlerAdapterList;
	private List<Class<? extends ChannelInboundHandlerAdapter>> prototypeList ;

	public  AbstractServer(int port,List<Class<? extends ChannelInboundHandlerAdapter>> prototypeList ,List<ChannelInboundHandlerAdapter> channelInboundHandlerAdapterList) {
		this.port = port;
		this.channelInboundHandlerAdapterList = channelInboundHandlerAdapterList;
		this.prototypeList = prototypeList;
	}

	public void run(String[] args) throws InterruptedException {
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
							Optional.ofNullable(prototypeList).ifPresent(LambdaUtil.wrapConsumer(arr -> {
								arr.stream().map(LambdaUtil.wrapFunction(Class::newInstance)).forEach(channel.pipeline()::addLast);
							}));
							Optional.ofNullable(channelInboundHandlerAdapterList).ifPresent(arr -> {
								arr.forEach(channel.pipeline()::addLast);
							});
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
}
