package nc.rpc.core.base;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import nc.rpc.core.base.server.ServerInit;
import nc.rpc.core.base.vo.NettyConstant;
import nc.rpc.core.sms.SendSmsImpl;
import nc.rpc.remote.SendSms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class RpcServerFrame implements Runnable{

    @Autowired
    private RegisterService registerService;
    @Autowired
    private ServerInit serverInit;

	private static final Log LOG = LogFactory.getLog(RpcServerFrame.class);
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void bind() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .childHandler(serverInit);

        // 绑定端口，同步等待成功
        b.bind(NettyConstant.REMOTE_PORT).sync();
        LOG.info("网络服务已准备好，可以进行业务操作了....... : "
            + (NettyConstant.REMOTE_IP + " : "
                + NettyConstant.REMOTE_PORT));
    }

    @PostConstruct
    public void startNet() throws Exception {
        registerService.regService(SendSms.class.getName(), SendSmsImpl.class);
        new Thread(this).start();
    }

    @PreDestroy
    public void stopNet() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }

    @Override
    public void run() {
        try {
            bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
