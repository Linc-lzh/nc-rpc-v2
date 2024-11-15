package nc.rpc.core.base.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import nc.rpc.core.base.kryocodec.KryoDecoder;
import nc.rpc.core.base.kryocodec.KryoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServerInit extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServerBusiHandler serverBusiHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /*Netty提供的日志打印Handler，可以展示发送接收出去的字节*/
        //ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        /*剥离接收到的消息的长度字段，拿到实际的消息报文的字节数组*/
        ch.pipeline().addLast("frameDecoder",
                new LengthFieldBasedFrameDecoder(65535,
                        0,2,0,
                        2));
        /*给发送出去的消息增加长度字段*/
        ch.pipeline().addLast("frameEncoder",
                new LengthFieldPrepender(2));
        /*反序列化，将字节数组转换为消息实体*/
        ch.pipeline().addLast(new KryoDecoder());
        /*序列化，将消息实体转换为字节数组准备进行网络传输*/
        ch.pipeline().addLast("MessageEncoder",
                new KryoEncoder());
        /*超时检测*/
        ch.pipeline().addLast("readTimeoutHandler",
                new ReadTimeoutHandler(50));
        /*登录应答*/
        ch.pipeline().addLast(new LoginAuthRespHandler());

        /*心跳应答*/
        ch.pipeline().addLast("HeartBeatHandler",
                new HeartBeatRespHandler());

        /*服务端业务处理*/
        ch.pipeline().addLast("ServerBusiHandler",
                serverBusiHandler);
    }
}
