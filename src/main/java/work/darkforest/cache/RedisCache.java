package work.darkforest.cache;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import work.darkforest.config.ApplicationContextConfig;

import java.util.concurrent.TimeUnit;

public class RedisCache implements Cache {
    // 因为这个类并不是由spring去实例化创建的，所以我们无法自动注入redisTemplate

    private final String id;    // id表示操作的类
    Logger logger = LoggerFactory.getLogger(RedisCache.class);

    public RedisCache(String id){
        this.id = id;
    }

    // 无法直接获取redisTemplate，所以用封装的工厂工具类间接获得
    private RedisTemplate getRedisTemplate(){
        return (RedisTemplate) ApplicationContextConfig.getBean("redisTemplate");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    // 放入缓存
    public void putObject(Object o, Object o1) {
        String key = DigestUtils.md5DigestAsHex(o.toString().getBytes());
        getRedisTemplate().opsForHash().put(id, key, o1);
        if (id.equals("work.darkforest.mapper.PoemMapper")) getRedisTemplate().expire(key, 12, TimeUnit.HOURS);
        logger.info("redis缓存 PUT " + id + ":" + key);
    }

    @Override
    // 从缓存中读取
    public Object getObject(Object o) {
        String key = DigestUtils.md5DigestAsHex(o.toString().getBytes());
        logger.info("redis缓存 GET " + id + ":" + key);
        return getRedisTemplate().opsForHash().get(id, key);
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    // 增删改时清除缓存
    public void clear() {
        logger.info("redis清除缓存 " + id);
        getRedisTemplate().delete(id);
    }

    @Override
    public int getSize() {
        return getRedisTemplate().opsForHash().size(id).intValue();
    }
}
