package nc.rpc.core.base.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import nc.rpc.core.base.vo.MessageType;
import nc.rpc.core.base.vo.RpcHeader;
import nc.rpc.core.base.vo.RpcMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	private static final Log LOG
			= LogFactory.getLog(HeartBeatRespHandler.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
		RpcMessage message = (RpcMessage) msg;
		// 返回心跳应答消息
		if (message.getMyHeader() != null
			&& message.getMyHeader().getType() == MessageType.HEARTBEAT_REQ
				.value()) {
//			LOG.info("Receive client heart beat message : ---> "+ message);
			RpcMessage heartBeat = buildHeatBeat();
//			LOG.info("Send heart beat response message to ---> client");
			ctx.writeAndFlush(heartBeat);
			ReferenceCountUtil.release(msg);
		} else
			ctx.fireChannelRead(msg);
    }

    private RpcMessage buildHeatBeat() {
		RpcMessage message = new RpcMessage();
		RpcHeader myHeader = new RpcHeader();
		myHeader.setType(MessageType.HEARTBEAT_RESP.value());
		message.setMyHeader(myHeader);
		return message;
    }

}
