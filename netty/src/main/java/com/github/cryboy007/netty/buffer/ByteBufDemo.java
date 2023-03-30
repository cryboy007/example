package com.github.cryboy007.netty.buffer;

import io.netty.buffer.*;
import io.netty.util.ByteProcessor;

/**
 * @ClassName ByteBufDemo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/3/9 10:18
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        //池类
        ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer();


//        heapBuf();
//        directBuf();
        //复合缓冲区
//        compositeBuf();
        ByteBuf buffer = Unpooled.buffer(5);
        buffer.writeBytes("test1".getBytes());
//        System.out.println("hexDump" + ByteBufUtil.hexDump(buffer));
        printBuffer(buffer.array(),buffer.readerIndex() + buffer.arrayOffset(),buffer.readableBytes());
        //通过索引访问时不会推进 readerIndex （读索引）和 writerIndex（写索引），
        // 我们可以通过 ByteBuf 的 readerIndex(index) 或 writerIndex(index) 来分别推进读索引或写索引
        //clear() 比 discardReadBytes() 更低成本，因为他只是重置了索引，而没有内存拷贝。
        System.out.println(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println((char) buffer.getByte(i));
        }
        //用来报告输入值是否是一个正在寻求的值
        int index = buffer.forEachByte(new ByteProcessor.IndexOfProcessor((byte) 's'));
        System.out.println(index);
        //ByteBuf 一定符合：0 <= readerIndex <= writerIndex <= capacity。
    }

    private static void compositeBuf() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf header = Unpooled.directBuffer(10);
        header.writeBytes("is header".getBytes());
        ByteBuf body = Unpooled.buffer(10);
        body.writeBytes("is body".getBytes());
        messageBuf.addComponents(header,body);

        // CompositeByteBuf 不允许访问其内部可能存在的支持数组，也不允许直接访问数据，这一点类似于直接缓冲区模式
//        messageBuf.writeBytes("this is compositeByteBuf".getBytes());
//        int length = messageBuf.readableBytes();
//        byte[] array = new byte[length];
//        messageBuf.getBytes(0,array);
//        printBuffer(array,0,length);
//        System.out.println(new String(array));

//        messageBuf.removeComponent(0); // 移除头
        // messageBuf.hasArray() 总是返回 false，因为它可能既包含堆缓冲区，也包含直接缓冲区
        for (int i = 0; i < messageBuf.numComponents(); i++) {
            if (messageBuf.component(i).hasArray())
                System.out.println(new String(messageBuf.component(i).array()));

            System.out.println(messageBuf.component(i).toString());
        }
    }

    private static void directBuf() {
        //直接缓冲区的缺点是在内存空间的分配和释放上比堆缓冲区更复杂，另外一个缺点是如果要将数据传递给遗留代码处理，因为数据不是在堆上，你可能不得不作出一个副本
        ByteBuf directBuf = Unpooled.directBuffer(10);
        directBuf.writeBytes("ni hao ma ? ".getBytes());
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array  = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            printBuffer(array,0,length);
        }
    }

    //堆缓存区
    private static void heapBuf() {
        ByteBuf heapBuf = Unpooled.buffer(10);
        heapBuf.writeBytes("ni hao ma ? ".getBytes());
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            printBuffer(array,offset,length);
        }
    }

    /**
     * 打印出Buffer的信息
     *
     * @param buffer
     */
    private static void printBuffer(byte[] array, int offset, int len) {
        System.out.println("array：" + array);
        System.out.println("array->String：" + new String(array));
        System.out.println("offset：" + offset);
        System.out.println("len：" + len);
    }
}
