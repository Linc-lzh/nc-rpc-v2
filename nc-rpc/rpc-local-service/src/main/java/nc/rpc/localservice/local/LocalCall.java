package nc.rpc.localservice.local;

import nc.rpc.localservice.service.NormalBusi;
import nc.rpc.localservice.service.SendSms;
import nc.rpc.localservice.service.StockService;
import nc.rpc.localservice.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalCall {

    @Autowired
    private NormalBusi normalBusi;
    @Autowired
    private StockService stockService;
    @Autowired
    private SendSms sendSms;

    public void processOrder() {

        long start = System.currentTimeMillis();
        normalBusi.business();

        /*扣减库存*/
        stockService.addStock("A001",1000);
        stockService.deduceStock("B002",50);

        /*发送邮件*/
        UserInfo userInfo = new UserInfo("Mark","Mark@xiangxue.com");
        System.out.println("Send mail: "+ sendSms.sendMail(userInfo));

        System.out.println("共耗时："+(System.currentTimeMillis()-start)+"ms");
    }
}
