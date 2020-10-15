package redis.clients.jedis;

import redis.clients.jedis.exceptions.JedisException;

@Deprecated
/**
 * This class is deprecated due to its error prone
 * and will be removed on next major release
 * @see https://github.com/xetorthio/jedis/pull/498
 */
public abstract class MyTransactionBlock extends MyTransaction {
    public MyTransactionBlock(MyClient client) {
        super(client);
    }

    public MyTransactionBlock() {
    }

    public abstract void execute() throws JedisException;

    public void setClient(MyClient client) {
        this.client = client;
    }
}
