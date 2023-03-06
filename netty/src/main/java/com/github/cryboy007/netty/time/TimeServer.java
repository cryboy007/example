package com.github.cryboy007.netty.time;

import java.util.Collections;

import com.github.cryboy007.netty.common.AbstractServer;
import com.github.cryboy007.netty.pojo.TimeDecoder;

/**
 *@ClassName TimeServer
 *@Author tao.he
 *@Since 2023/3/5 12:18
 */
public class TimeServer extends AbstractServer {
	public TimeServer(int port) {
		super(port, Collections.singletonList(TimeDecoder.class), Collections.singletonList(new TimeServerHandler()));
	}

	public static void main(String[] args) throws Exception {
		new TimeServer(8080).run(args);
	}
}
