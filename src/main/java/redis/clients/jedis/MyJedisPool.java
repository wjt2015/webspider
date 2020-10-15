package redis.clients.jedis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.commons.pool2.impl.MyGenericObjectPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.JedisURIHelper;
import redis.clients.util.MyPool;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
@Slf4j
public class MyJedisPool extends MyPool<MyJedis> {

    public MyJedisPool() {
        this(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host) {
        this(poolConfig, host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, null,
                Protocol.DEFAULT_DATABASE, null);
    }

    public MyJedisPool(String host, int port) {
        this(new GenericObjectPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, null,
                Protocol.DEFAULT_DATABASE, null);
    }

    public MyJedisPool(final String host) {
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            String h = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int database = JedisURIHelper.getDBIndex(uri);
            boolean ssl = uri.getScheme().equals("rediss");
            this.internalPool = new MyGenericObjectPool<>(new MyJedisFactory(h, port,
                    Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, password, database, null,
                    ssl, null, null, null), new GenericObjectPoolConfig());
        } else {
            this.internalPool = new MyGenericObjectPool<>(new MyJedisFactory(host,
                    Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, null,
                    Protocol.DEFAULT_DATABASE, null, false, null, null, null), new GenericObjectPoolConfig());
        }
    }

    public MyJedisPool(final String host, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            String h = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int database = JedisURIHelper.getDBIndex(uri);
            boolean ssl = uri.getScheme().equals("rediss");
            this.internalPool = new MyGenericObjectPool<MyJedis>(new MyJedisFactory(h, port,
                    Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, password, database, null, ssl,
                    sslSocketFactory, sslParameters, hostnameVerifier),
                    new GenericObjectPoolConfig());
        } else {
            this.internalPool = new MyGenericObjectPool<MyJedis>(new MyJedisFactory(host,
                    Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, null,
                    Protocol.DEFAULT_DATABASE, null, false, null, null, null), new GenericObjectPoolConfig());
        }
    }

    public MyJedisPool(final URI uri) {
        this(new GenericObjectPoolConfig(), uri, Protocol.DEFAULT_TIMEOUT);
    }

    public MyJedisPool(final URI uri, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(new GenericObjectPoolConfig(), uri, Protocol.DEFAULT_TIMEOUT, sslSocketFactory,
                sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final URI uri, final int timeout) {
        this(new GenericObjectPoolConfig(), uri, timeout);
    }

    public MyJedisPool(final URI uri, final int timeout, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(new GenericObjectPoolConfig(), uri, timeout, sslSocketFactory, sslParameters,
                hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password) {
        this(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE, null);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final boolean ssl) {
        this(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE, null, ssl);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final boolean ssl,
                     final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                     final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE, null, ssl,
                sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port,
                     final boolean ssl) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null,
                ssl);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port,
                     final boolean ssl, final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                     final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null,
                ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port,
                     final int timeout) {
        this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE, null);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port,
                     final int timeout, final boolean ssl) {
        this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE, null, ssl);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port,
                     final int timeout, final boolean ssl, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE, null, ssl,
                sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final int database) {
        this(poolConfig, host, port, timeout, password, database, null);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final int database, final boolean ssl) {
        this(poolConfig, host, port, timeout, password, database, null, ssl);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final int database, final boolean ssl,
                     final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                     final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, password, database, null, ssl, sslSocketFactory,
                sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final int database, final String clientName) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, false,
                null, null, null);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final int database, final String clientName,
                     final boolean ssl) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl,
                null, null, null);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     int timeout, final String password, final int database, final String clientName,
                     final boolean ssl, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl,
                sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                     final int connectionTimeout, final int soTimeout, final String password, final int database,
                     final String clientName, final boolean ssl, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        super(poolConfig, new MyJedisFactory(host, port, connectionTimeout, soTimeout, password,
                database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri) {
        this(poolConfig, uri, Protocol.DEFAULT_TIMEOUT);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,
                     final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                     final HostnameVerifier hostnameVerifier) {
        this(poolConfig, uri, Protocol.DEFAULT_TIMEOUT, sslSocketFactory, sslParameters,
                hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int timeout) {
        this(poolConfig, uri, timeout, timeout);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int timeout,
                     final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                     final HostnameVerifier hostnameVerifier) {
        this(poolConfig, uri, timeout, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,
                     final int connectionTimeout, final int soTimeout) {
        super(poolConfig, new MyJedisFactory(uri, connectionTimeout, soTimeout, null, false,
                null, null, null));
    }

    public MyJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,
                     final int connectionTimeout, final int soTimeout, final SSLSocketFactory sslSocketFactory,
                     final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        super(poolConfig, new MyJedisFactory(uri, connectionTimeout, soTimeout, null,
                (uri.getScheme() !=null && uri.getScheme().equals("rediss")), sslSocketFactory,
                sslParameters, hostnameVerifier));
    }

    @Override
    public MyJedis getResource() {
        MyJedis jedis = super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }

    /**
     * @deprecated starting from MyJedis 3.0 this method will not be exposed. Resource cleanup should be
     *             done using @see {@link redis.clients.jedis.Jedis#close()}
     */
    @Override
    @Deprecated
    public void returnBrokenResource(final MyJedis resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    /**
     * @deprecated starting from MyJedis 3.0 this method will not be exposed. Resource cleanup should be
     *             done using @see {@link redis.clients.jedis.Jedis#close()}
     */
    @Override
    @Deprecated
    public void returnResource(final MyJedis resource) {
        if (resource != null) {
            try {
                resource.resetState();
                returnResourceObject(resource);
            } catch (Exception e) {
                returnBrokenResource(resource);
                throw new JedisException("Could not return the resource to the pool", e);
            }
        }
    }
}
