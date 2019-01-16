package cn.itsource.redis;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;

/**
 * 该类用来操作redis的key和各种类型value
 * 事务四大特征：
 *   原子性：要么都执行，要么都不执行
 *   一致性：回滚
 *   隔离性：事务间不能相互影响
 *   持续性：一旦开始就不能中止
 * @author Administrator
 *
 */
public class JedisOprsTest {

	//很多方法都要使用Pool,jedis，抽取一个成员变量
	JedisPool pool = null;
	Jedis jedis = null;

	//其实我们执行一个测试方法时候，会先创建该测试方法类的对象，所以可以在构造函数中初始化。
	public JedisOprsTest() {
		//1 创建连接池配置对象
		JedisPoolConfig config = new JedisPoolConfig();
		//2 进行配置
		//最小连接数
		config.setMaxIdle(2);
		config.setMaxTotal(5);//最大连接数
		config.setMaxWaitMillis(2*1000);//超时时间
		config.setTestOnBorrow(true);//获取连接时判断连接是否畅通
		//3 通过配置对象创建连接池
		pool = new JedisPool(config, "127.0.0.1",6379,2*1000,"wbtest");
		jedis = pool.getResource();
	}

	@Test
	public void testKeyOpr() throws Exception {
		//为了不让原有的数据影响测试，情况原来所有数据
		System.out.println("清空数据："+jedis.flushAll());
		// exists：存在
		System.out.println("判断名称为name的key是否存在："+jedis.exists("name"));
		System.out.println("判断名称为age的key是否存在："+jedis.exists("age"));
		System.out.println("判断名称为sex的key是否存在："+jedis.exists("sex"));

		System.out.println("给名称为name的key的设置一个值："+jedis.set("name", "zs"));
		System.out.println("给名称为age的key的设置一个值："+jedis.set("age", "18"));
		System.out.println("给名称为sex的key的设置一个值："+jedis.set("sex", "nan"));

		System.out.println("再次判断名称为name的key是否存在："+jedis.exists("name"));
		System.out.println("再次判断名称为age的key是否存在："+jedis.exists("age"));
		System.out.println("再次判断名称为sex的key是否存在："+jedis.exists("sex"));

		System.out.println("获取名称为name的key的值："+jedis.get("name"));
		System.out.println("获取名称为age的key的值："+jedis.get("age"));
		System.out.println("获取名称为sex的key的值："+jedis.get("sex"));

		System.out.println("删除名称为sex的key的值："+jedis.del("sex"));
		System.out.println("删除后再次获取名称为sex的key的值："+jedis.get("sex"));

		//获取所有
		Set<String> keys = jedis.keys("*");
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = jedis.get(key);
			System.out.println(key+"---->"+value);

		}
		//设置值：
		System.out.println("我自己的=============================");
		jedis.set("wbtest", "6699");
		System.out.println(jedis.ttl("wbtest"));
		jedis.expire("wbtest", 5);
		System.out.println(jedis.get("wbtest"));
		Thread.sleep(2000);
		System.out.println(jedis.ttl("wbtest"));
		Thread.sleep(4000);
		System.out.println("已经过期："+jedis.get("wbtest"));
		//释放连接，摧毁连接池
		jedis.close();
		pool.destroy();
	}

	//基本操作-crud
	@Test
	public void testStrOpr1() throws Exception {
		//为了不让原有的数据影响测试，情况原来所有数据
		System.out.println("清空数据："+jedis.flushAll());
		System.out.println("===========创建==============");
		jedis.set("name", "zs");
		jedis.set("age", "18");
		jedis.set("sex", "nan");
		System.out.println("===========创建==============");
		System.out.println("===========查==============");
		jedis.get("name");
		System.out.println(jedis.get("age"));
		jedis.get("sex");
		System.out.println("===========查==============");
		System.out.println("===========改==============");
		jedis.set("age", "19");
		System.out.println(jedis.get("age"));;
		System.out.println("===========改==============");
		System.out.println("===========删除==============");
		jedis.del("age");
		System.out.println(jedis.get("age"));;
		System.out.println("===========删除==============");

		//释放连接，摧毁连接池
//		jedis.mset(keysvalues)
//		jedis.mget(keys)
//		jedis.del(keys)

//		jedis.incr(key)
//		jedis.incrBy(key, integer)
//		//设置值得时候并且给它一个过期时间
//		jedis.setex(key, seconds, value)
		jedis.close();
		pool.destroy();
	}
	/**
	 * List操作：
	 *    lpush 设置列表值
	 *    lrange 获取list值，end为-1
	 *    lrem 删除
	 *    lset 修改
	 *
	 *    sort排序
	 *       数字，不用加sortingParams
	 *       字母需要加，并且是指assii值排序
	 *
	 * @throws Exception
	 */
	@Test
	public void listOperation() throws Exception
	{
		System.out.println("清空数据库："+jedis.flushDB());
		System.out.println("===============添加========================");
		System.out.println("添加语言到langues List中。");
		jedis.lpush("langues", "java");
		jedis.lpush("langues", "mysql");
		jedis.lpush("langues", "javascript");
		jedis.lpush("langues", "oracle");
		jedis.lpush("langues", "javascript");
		jedis.lpush("langues", "redis");
		jedis.lpush("langues", "javascript");
		jedis.lpush("nums", "3");
		jedis.lpush("nums", "1");
		jedis.lpush("nums", "5");
		jedis.lpush("nums", "4");

		System.out.println("获取langues list的值："+jedis.lrange("langues", 0, -1));
		System.out.println("===============删除========================");
		System.out.println("从langues list中删除一个JavaScript"+jedis.lrem("langues", 2, "javascript"));
		System.out.println("获取langues list的值："+jedis.lrange("langues", 0, -1));
		System.out.println("===============修改========================");
		System.out.println(jedis.lset("langues", 3, "VBScript"));
		System.out.println("获取langues list的值："+jedis.lrange("langues", 0, -1));
		System.out.println("===============查询========================");
		System.out.println("获取langues list长度"+jedis.llen("langues"));

		System.out.println("对list nums进行排序后的值：" +jedis.sort("nums"));
		System.out.println("nums原始值："+jedis.lrange("nums", 0, -1));


		SortingParams sortingParameters = new SortingParams();
		//使用assii码进行排序
		sortingParameters.alpha();
		System.out.println("对list langues进行排序后的值：" +jedis.sort("langues",sortingParameters));


		//排序
		//释放连接
		jedis.close();
		//摧毁连接池
		pool.destroy();
	}
	/**
	 * sadd 添加成员
	 * srem 删除
	 * smember 查看所有成员
	 * sinter 交集
	 * sinterstore 交集保存到新的Set
	 * sunion  并集
	 * sunionstore 并集保存到新的Set
	 * sdiff 差集（我有你没有）
	 * sdiffstore 差集保存到新的Set
	 * @throws Exception
	 */
	@Test
	public void setOperation() throws Exception
	{
		System.out.println("清空数据库："+jedis.flushDB());
		System.out.println("===============添加========================");
		jedis.sadd("keySet", "key1" ,"key2", "key3");
		System.out.println("获取所以keySet Set值："+jedis.smembers("keySet"));
		System.out.println("===============删除========================");
		System.out.println("删除key1 key2："+jedis.srem("keySet", "key1", "key2"));
		System.out.println("获取所以keySet Set值："+jedis.smembers("keySet"));
		System.out.println("===============修改========================");
		System.out.println("修改key1 key2 key3："+jedis.sadd("keySet", "key1--->edit", "key2--->edit", "key3--->edit"));
		System.out.println("获取所以keySet Set值："+jedis.smembers("keySet"));
		System.out.println("===============查询========================");

		System.out.println("分别添加me xiaoli 的朋友：");
		jedis.sadd("me", "zs" ,"ls", "ww");
		jedis.sadd("xiaoli", "zs" ,"ww", "zl");

		//交集
		System.out.println("交集为："+jedis.sinter("me", "xiaoli"));
		System.out.println("交集为："+jedis.sinterstore("newGroup1", "me", "xiaoli"));
		System.out.println("交集保存为："+jedis.smembers("newGroup1"));

		//并集
		System.out.println("并集为："+jedis.sunion("me", "xiaoli"));
		System.out.println("并集为："+jedis.sunionstore("newGroup2", "me", "xiaoli"));
		System.out.println("并集保存为："+jedis.smembers("newGroup2"));
		//差集 我有你没有
		System.out.println("差集为："+jedis.sdiff("me", "xiaoli"));
		System.out.println("差集为："+jedis.sdiffstore("newGroup3", "me", "xiaoli"));
		System.out.println("差集保存为："+jedis.smembers("newGroup3"));
		//释放连接
		jedis.close();
		//摧毁连接池
		pool.destroy();
	}

	/**
	 * Hash操作
	 *   hset ： 设置或者修改
	 *   hgetAll 获取所有
	 *   hdel 删除
	 *   hincrBy 自增
	 *   hkeys 查询所有的key
	 *   hvalues 查询说要的value
	 *
	 *   Map<String,Map<String,Oject>>
	 * @throws Exception
	 */
	@Test
	public void hashOperation() throws Exception
	{
		System.out.println("清空数据库："+jedis.flushDB());
		System.out.println("===============添加========================");
		jedis.hset("user", "name", "zs");
		jedis.hset("user", "age", "18");
		jedis.hset("user", "tel", "135xxxx");
		System.out.println(jedis.hgetAll("user"));
		System.out.println("===============删除========================");
		System.out.println(jedis.hdel("user", "age", "tel"));
		System.out.println(jedis.hgetAll("user"));
		System.out.println("===============修改========================");
		jedis.hset("user", "name", "zs");
		jedis.hset("user", "age", "20");
		jedis.hset("user", "tel", "123xxx");
		System.out.println(jedis.hgetAll("user"));
		jedis.hincrBy("user", "age", 2L);
		System.out.println(jedis.hgetAll("user"));
		System.out.println("===============查询========================");
		System.out.println("查询所有的key："+jedis.hkeys("user"));
		System.out.println("查询所有的value："+jedis.hvals("user"));
		System.out.println("查询hash的长度："+jedis.hlen("user"));
		//释放连接
		jedis.close();
		//摧毁连接池
		pool.destroy();
	}

	@Test
	public void testBaseOpr() throws Exception {
		//为了不让原有的数据影响测试，情况原来所有数据
		System.out.println("清空数据："+jedis.flushAll());

		//测试。。。。。

		//释放连接，摧毁连接池
		jedis.close();
		pool.destroy();
	}
}
