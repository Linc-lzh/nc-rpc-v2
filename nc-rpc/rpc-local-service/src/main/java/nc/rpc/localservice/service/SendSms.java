package nc.rpc.localservice.service;

import nc.rpc.localservice.vo.UserInfo;

public interface SendSms {
    boolean sendMail(UserInfo user);
}
