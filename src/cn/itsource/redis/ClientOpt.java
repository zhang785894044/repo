package cn.itsource.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class ClientOpt {

    @Test
    public void testClient() {
        // 创建客户端：你知道客户端的ip，端口呢？  构造函数；或者set方法
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("wbtest");
        //使用客户端调用api:java代码里的api都和命令行的api一样
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);


        jedis.set("name", "zxx");
        System.out.println(jedis.get("name"));
        jedis.close();
    }

}
