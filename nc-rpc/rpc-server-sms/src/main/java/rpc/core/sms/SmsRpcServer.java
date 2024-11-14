package rpc.core.sms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpc.core.base.RpcServerFrame;
import rpc.remote.SendSms;

import javax.annotation.PostConstruct;
import java.util.Random;


@Service
public class SmsRpcServer {

    @Autowired
    private RpcServerFrame rpcServerFrame;

    @PostConstruct
    public void server() throws Throwable {
        Random r = new Random();
        int port = 8778+r.nextInt(100);
        rpcServerFrame.startService(SendSms.class.getName(),
                "127.0.0.1",port,SendSmsImpl.class);

    }

}
