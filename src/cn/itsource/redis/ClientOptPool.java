package cn.itsource.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

public class ClientOptPool {
    JedisPool pool=null;

    public  ClientOptPool(){
        //公共部分：
        //需要对这个连接池进行配置:需要导入一个链接池的包
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);//最大空闲链接数
        poolConfig.setMaxWaitMillis(3 * 1000);// Milli:毫秒
        poolConfig.setTestOnBorrow(true);//借用的使用测试

        //获取一个连接池对象：默认的端口是6379
        // (GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password)
        //JedisPool pool=new JedisPool(poolConfig,"127.0.0.1");
        pool  = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000);
    }

    @Test
    public void testPool() {
        //需要对这个连接池进行配置:需要导入一个链接池的包
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);//最大空闲链接数
        poolConfig.setMaxWaitMillis(3 * 1000);// Milli:毫秒
        poolConfig.setTestOnBorrow(true);//借用的使用测试

        //获取一个连接池对象：默认的端口是6379
        // (GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password)
        //JedisPool pool=new JedisPool(poolConfig,"127.0.0.1");
        JedisPool pool = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000, "wbtest");

        // 获取一个客户端
        // Could not get a resource from the pool
        Jedis jedis = pool.getResource();
        jedis.set("name", "xxxxx");
        System.out.println(jedis.get("name"));
        //你不是应该还给连接池：还或者关闭：做了兼容的。
        jedis.close();
        // 连接池销毁
        pool.destroy();
    }

    @Test
    public void testPool2() {
        // 1:获取一个连接池

        // 2：从连接池上获取一个连接：

        // 3：调用api做事情

        // 4：资源的处理
        System.out.println(999);

    }

    @Test
    public void testPool3() {
        // 1:获取一个连接池
        // 2：从连接池上获取一个连接：
        Jedis jedis = pool.getResource();
        // 3：调用api做事情:java中的api的名字和客户端一样的。
        jedis.set("age", "17");
        // 4：资源的处理
        //你不是应该还给连接池：还或者关闭：做了兼容的。
        jedis.close();
        // 连接池销毁
        pool.destroy();

    }


}
