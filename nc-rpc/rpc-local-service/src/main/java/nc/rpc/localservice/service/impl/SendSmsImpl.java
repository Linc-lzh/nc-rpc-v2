package nc.rpc.localservice.service.impl;

import nc.rpc.localservice.service.SendSms;
import nc.rpc.localservice.vo.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class SendSmsImpl implements SendSms {
    @Override
    public boolean sendMail(UserInfo user) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发送短信息给："+user.getName()+"到【"+user.getPhone()+"】");
        return true;
    }
}
