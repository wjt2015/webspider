package redis.clients.jedis;

public abstract class MyJedisMonitor {
    protected MyClient client;

    public void proceed(MyClient client) {
        this.client = client;
        this.client.setTimeoutInfinite();
        do {
            String command = client.getBulkReply();
            onCommand(command);
        } while (client.isConnected());
    }

    public abstract void onCommand(String command);
}