package nc.rpc.localservice.service;

public interface StockService {
    /*增加库存*/
    void addStock(String goodsId, int addAmout);
    /*扣减库存*/
    void deduceStock(String goodsId, int deduceAmout);
}
