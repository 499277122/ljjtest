package cn.ljj.test.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtils {

	private static Logger log = Logger.getLogger(RedisUtils.class);

	private JedisPool jedisPool;

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/****************************** 基础操作 ******************************/

	/**
	 * key对应的Value加1 原子性操作
	 * 
	 * @param key
	 */
	public Long incr(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.incr(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	/**
	 * key对应的Value减1 原子性操作
	 * 
	 * @param key
	 */
	public Long decr(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.decr(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	/**
	 * key对应的Value加 increment 原子性操作
	 * 
	 * @param key
	 */
	public Long incrBy(String key, long increment) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.incrBy(key, increment);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	/**
	 * key对应的Value减decrement 原子性操作
	 * 
	 * @param key
	 */
	public Long decrBy(String key, long decrement) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.decrBy(key, decrement);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	public Boolean setnx(String key, String value, Long expire) {
		Jedis jedis = null;
		boolean ret = false;
		try {
			jedis = jedisPool.getResource();
			Long s = jedis.setnx(key, value);
			if (null != s && s.longValue() == 1L) {
				ret = true;
			}
			if (null != expire) {
				jedis.expire(key, Long.valueOf(expire / 1000).intValue());
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

	public String getset(String key, String value, Long expire) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.getSet(key, value);
			if (null != expire) {
				jedis.expire(key, Long.valueOf(expire / 1000).intValue());
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

	/**
	 * 设置过期时间
	 * 
	 * @param key
	 * @param expire
	 */
	public void setExpire(String key, long expire) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.expire(key, Long.valueOf(expire / 1000).intValue());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 对key查询（支持模糊查询）
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> keys(String key) {
		Jedis jedis = null;
		Set<String> value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.keys(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 前缀删除
	 * 
	 * @author yi.wang
	 * @param prefixKey 前缀
	 * @date 2017年4月14日
	 */
	public void delPrefix(String prefixKey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> set = jedis.keys(prefixKey + "*");
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String keyStr = it.next();
				jedis.del(keyStr);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/****************************** String操作 ******************************/

	/**
	 * String类型设值
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * String类型设值
	 * 
	 * @param key
	 * @param value
	 * @param expire 有效期 单位是ms
	 */
	public void set(String key, String value, long expire) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, Long.valueOf(expire / 1000).intValue());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 删除值
	 * 
	 * @param key
	 */
	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 判断是否存在key
	 * 
	 * @param key
	 * @return
	 */
	public Boolean exists(String key) {
		Jedis jedis = null;
		Boolean ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.exists(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

	/**
	 * 得到值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 得到值
	 * 
	 * @param key
	 * @param callback
	 * @param expiry 有效期
	 * @return
	 */
	public String get(String key, Callable<String> callback, long expiry) {
		String value = get(key);
		if (null == value) {
			try {
				value = callback.call();
				if (null != value) {
					set(key, value, expiry);
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return value;
	}

	/**
	 * 得到值
	 * 
	 * @param key
	 * @param callback
	 * @return
	 */
	public String get(String key, Callable<String> callback) {
		String value = get(key);
		if (null == value) {
			try {
				value = callback.call();
				if (null != value) {
					set(key, value);
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return value;
	}

	/****************************** Hash操作 ******************************/

	/**
	 * Hash添加值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}

	}

	/**
	 * Hash添加值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @param expire 有效期 单位是ms
	 */
	public void hset(String key, String field, String value, long expire) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
			jedis.expire(key, Long.valueOf(expire / 1000).intValue());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取Hash的所有keys
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> hkeys = jedis.hkeys(key);
			return hkeys;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	/**
	 * 删除Hash中的一个或多个
	 * 
	 * @param key
	 * @param fields
	 */
	public void hdel(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hdel(key, fields);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 判断是否存在key field
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(String key, String field) {
		Jedis jedis = null;
		Boolean ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.hexists(key, field);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

	/**
	 * 返回Hash中key中给定域field的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		if (StringUtils.isBlank(field)) {
			return null;
		}
		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 返回Hash中所有的字段和值
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		Map<String, String> value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hgetAll(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 返回Hash中key中给定域field的值，并回调重新设置值
	 * 
	 * @param key
	 * @param field
	 * @param callback
	 * @param expiry 有效期
	 * @return
	 */
	public String hget(String key, String field, Callable<String> callback, long expiry) {
		String value = hget(key, field);
		if (null == value) {
			try {
				value = callback.call();
				if (null != value) {
					hset(key, field, value, expiry);
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return value;
	}

	/**
	 * 返回Hash中key中给定域field的值，并回调重新设置值
	 * 
	 * @param key
	 * @param field
	 * @param callback
	 * @return
	 */
	public String hget(String key, String field, Callable<String> callback) {
		String value = hget(key, field);
		if (null == value) {
			try {
				value = callback.call();
				if (null != value) {
					hset(key, field, value);
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return value;
	}

	/****************************** list操作 ******************************/

	/**
	 * 从list的头部添加字符串元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long lpush(String key, String... values) {
		Jedis jedis = null;
		Long value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.lpush(key, values);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 从list的头部删除元素，并返回删除元素
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.lpop(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 从list的尾部添加字符串元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long rpush(String key, String... values) {
		Jedis jedis = null;
		Long value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.rpush(key, values);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 从list的尾部删除元素，并返回删除元素
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.rpop(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 从list的尾部删除元素，并返回删除元素
	 * 
	 * @param key
	 * @return
	 */
	public String brpop(String key) {
		Jedis jedis = null;
		List<String> list = null;
		try {
			jedis = jedisPool.getResource();
			list = jedis.brpop(0, key);

			if (list == null || list.isEmpty()) {
				return null;
			} else {
				return list.get(1);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);

		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	/**
	 * 通过索引获取list中的元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public String lindex(String key, long index) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.lindex(key, index);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 返回list的长度
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long llen(String key) {
		Jedis jedis = null;
		Long value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.llen(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 返回list中指定区间内的元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public List<String> lrange(String key, Integer start, Integer end) {
		Jedis jedis = null;
		List<String> list = new ArrayList<String>();
		try {
			jedis = jedisPool.getResource();
			list = jedis.lrange(key, start, end);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return list;
	}

	/****************************** 无序set操作 ******************************/

	/**
	 * set类型设值
	 * 
	 * @param key
	 * @param value
	 */
	public void sadd(String key, String... values) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.sadd(key, values);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * set类型设值
	 * 
	 * @param key
	 * @param value
	 * @param expire 有效期 单位是ms
	 */
	public void sadd(long expire, String key, String... values) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.sadd(key, values);
			jedis.expire(key, Long.valueOf(expire / 1000).intValue());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 返回zset的长度
	 * 
	 * @param key
	 * @return
	 */
	public Long slen(String key) {
		Jedis jedis = null;
		Long value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.scard(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 返回set中的所有的成员
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> sgetAll(String key) {
		Jedis jedis = null;
		Set<String> value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.smembers(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 随机从set中返回一个成员
	 * 
	 * @param key
	 */
	public String sgetRandom(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.srandmember(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 删除set指定的成员
	 * 
	 * @param key
	 */
	public void sdel(String key, String... values) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.srem(key, values);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * set是否存在
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean sexists(String key, String value) {
		Jedis jedis = null;
		Boolean ret = false;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.sismember(key, value);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

	/****************************** 有序zset操作 ******************************/

	/**
	 * zset类型设值
	 * 
	 * @param key
	 * @param score 分数值用于排序
	 * @param value
	 */
	public void zadd(String key, Double score, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.zadd(key, score, value);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * zset类型设值
	 * 
	 * @param key
	 * @param score 分数值用于排序
	 * @param value
	 * @param expire 有效期 单位是ms
	 */
	public void zadd(String key, Double score, String value, long expire) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.zadd(key, score, value);
			jedis.expire(key, Long.valueOf(expire / 1000).intValue());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 返回zset的长度
	 * 
	 * @param key
	 * @return
	 */
	public Long zlen(String key) {
		Jedis jedis = null;
		Long value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.zcard(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 返回zset中的所有的成员
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> zgetAll(String key) {
		Jedis jedis = null;
		Set<String> value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.zrange(key, 0L, -1L);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 删除zset指定的成员
	 * 
	 * @param key
	 */
	public void zdel(String key, String... values) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.zrem(key, values);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * zset是否存在
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean zexists(String key, String value) {
		Jedis jedis = null;
		Boolean ret = false;
		try {
			jedis = jedisPool.getResource();
			Long zrank = jedis.zrank(key, value);
			if (null != zrank) {
				ret = true;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

}
