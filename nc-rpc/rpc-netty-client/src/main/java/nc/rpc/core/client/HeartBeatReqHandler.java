package nc.rpc.core.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import nc.rpc.core.base.vo.MessageType;
import nc.rpc.core.base.vo.RpcHeader;
import nc.rpc.core.base.vo.RpcMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(HeartBeatReqHandler.class);

    private volatile ScheduledFuture<?> heartBeat;

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        RpcMessage message = (RpcMessage) msg;
        // 握手或者说登录成功，主动发送心跳消息
        if (message.getMyHeader() != null
                && message.getMyHeader().getType() == MessageType.LOGIN_RESP
                .value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new HeartBeatTask(ctx), 0,
                    5000,
                    TimeUnit.MILLISECONDS);
            ReferenceCountUtil.release(msg);
        //如果是心跳应答
        } else if (message.getMyHeader() != null
                && message.getMyHeader().getType() == MessageType.HEARTBEAT_RESP
                .value()) {
//            LOG.info("Client receive server heart beat message : ---> ");
            ReferenceCountUtil.release(msg);
        //如果是其他报文，传播给后面的Handler
        } else
            ctx.fireChannelRead(msg);
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;
        //心跳计数，可用可不用，已经有超时处理机制
        private final AtomicInteger heartBeatCount;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
            heartBeatCount = new AtomicInteger(0);
        }

        @Override
        public void run() {
            RpcMessage heatBeat = buildHeatBeat();
//            LOG.info("Client send heart beat messsage to server : ---> "
//                            + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private RpcMessage buildHeatBeat() {
            RpcMessage message = new RpcMessage();
            RpcHeader myHeader = new RpcHeader();
            myHeader.setType(MessageType.HEARTBEAT_REQ.value());
            message.setMyHeader(myHeader);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
