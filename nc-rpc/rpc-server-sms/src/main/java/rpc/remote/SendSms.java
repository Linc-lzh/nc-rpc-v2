package rpc.remote;


import rpc.remote.vo.UserInfo;

public interface SendSms {
    /*发送短信*/
    boolean sendMail(UserInfo user);

}
