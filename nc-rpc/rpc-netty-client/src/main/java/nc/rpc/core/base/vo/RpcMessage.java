package nc.rpc.core.base.vo;


public final class RpcMessage {

    private RpcHeader myHeader;

    private Object body;

    public final RpcHeader getMyHeader() {
    	return myHeader;
    }

    public final void setMyHeader(RpcHeader myHeader) {
    	this.myHeader = myHeader;
    }

    public final Object getBody() {
    	return body;
    }

    public final void setBody(Object body) {
    	this.body = body;
    }

    @Override
    public String toString() {
    	return "RpcMessage [RpcHeader=" + myHeader + "][body="+body+"]";
    }
}
