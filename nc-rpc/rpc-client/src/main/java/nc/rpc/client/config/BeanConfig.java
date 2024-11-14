package nc.rpc.client.config;

import nc.rpc.client.core.RpcClientFrame;
import nc.rpc.remote.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Autowired
    private RpcClientFrame rpcClientFrame;

    @Bean
    public SendSms getSmsService() throws Exception{
        return rpcClientFrame.getRemoteProxyObject(SendSms.class);
    }
}
