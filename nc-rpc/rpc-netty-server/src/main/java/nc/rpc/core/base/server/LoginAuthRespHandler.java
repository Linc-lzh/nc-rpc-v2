package nc.rpc.core.base.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import nc.rpc.core.base.vo.MessageType;
import nc.rpc.core.base.vo.RpcHeader;
import nc.rpc.core.base.vo.RpcMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

	private final static Log LOG
            = LogFactory.getLog(LoginAuthRespHandler.class);

	//用以检查用户是否重复登录的缓存
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    //用户登录的白名单
    private String[] whitekList = { "127.0.0.1"};

    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
		RpcMessage message = (RpcMessage) msg;

		// 如果是握手请求消息，处理，其它消息透传
		if (message.getMyHeader() != null
			&& message.getMyHeader().getType() == MessageType.LOGIN_REQ
				.value()) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			RpcMessage loginResp = null;
			// 重复登陆，拒绝
			if (nodeCheck.containsKey(nodeIndex)) {
				loginResp = buildResponse((byte) -1);
			} else {
			    //检查用户是否在白名单中，在则允许登录，并写入缓存
				InetSocketAddress address = (InetSocketAddress) ctx.channel()
					.remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for (String WIP : whitekList) {
					if (WIP.equals(ip)) {
					isOK = true;
					break;
					}
				}
				loginResp = isOK ? buildResponse((byte) 0)
					: buildResponse((byte) -1);
				if (isOK)
					nodeCheck.put(nodeIndex, true);
			}
			LOG.info("The login response is : " + loginResp
				+ " body [" + loginResp.getBody() + "]");
			ctx.writeAndFlush(loginResp);
			ReferenceCountUtil.release(msg);
		}
		/*注释后，可演示消息不往下传递的情况*/
		else {
			ctx.fireChannelRead(msg);
		}
    }

    private RpcMessage buildResponse(byte result) {
		RpcMessage message = new RpcMessage();
		RpcHeader myHeader = new RpcHeader();
		myHeader.setType(MessageType.LOGIN_RESP.value());
		message.setMyHeader(myHeader);
		message.setBody(result);
		return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
		//cause.printStackTrace();
        // 删除缓存
		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
    }
}
