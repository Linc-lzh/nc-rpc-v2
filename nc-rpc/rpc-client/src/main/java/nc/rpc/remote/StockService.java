package nc.rpc.remote;

public interface StockService {
    /*增加库存*/
    void addStock(String goodsId, int addAmout);
    /*扣减库存*/
    void deduceStock(String goodsId, int deduceAmout);
}
