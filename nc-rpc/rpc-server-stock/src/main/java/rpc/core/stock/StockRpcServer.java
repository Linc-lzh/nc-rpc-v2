package rpc.core.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpc.core.base.RpcServerFrame;
import rpc.remote.StockService;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
public class StockRpcServer {

    @Autowired
    private RpcServerFrame rpcServerFrame;

    @PostConstruct
    public void server() throws Throwable {
        Random r = new Random();
        int port = r.nextInt(100)+7778;
        rpcServerFrame.startService(StockService.class.getName(),
                "127.0.0.1",port,StockServiceImpl.class);

    }

}
