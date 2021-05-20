package work.darkforest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import work.darkforest.entity.redis.HotRank;
import work.darkforest.utils.constant.SystemConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        redisTemplate.opsForValue().set("name", "darkforest");
    }


    @Test
    void testZset(){
        // 添加一个zset
        // redisTemplate.opsForZSet().add(SystemConstant.HOT_RANK, "libai", 1);

        // 查询一个标签的分数
        // System.out.println(redisTemplate.opsForZSet().score(SystemConstant.HOT_RANK, "libai"));

        // 获取value以及score
        /*Set range = redisTemplate.opsForZSet().range(SystemConstant.HOT_RANK, 0, -1);
        List<HotRank> ranks = new ArrayList<>();
        for (Object value : range) {
            Double score = redisTemplate.opsForZSet().score(SystemConstant.HOT_RANK, value.toString());
            ranks.add(new HotRank(value.toString(), score));
        }
        ranks.forEach(res-> System.out.println(res));*/

        // 根据分数
/*        System.out.println(Double.POSITIVE_INFINITY);
        Set range = redisTemplate.opsForZSet().rangeByScore(SystemConstant.HOT_RANK, 2, Double.POSITIVE_INFINITY);
        List<HotRank> ranks = new ArrayList<>();
        for (Object value : range) {
            Double score = redisTemplate.opsForZSet().score(SystemConstant.HOT_RANK, value.toString());
            ranks.add(new HotRank(value.toString(), score));
        }
        ranks.forEach(res-> System.out.println(res));*/


        // 让分数自增，如果不存在就创建
        redisTemplate.opsForZSet().incrementScore(SystemConstant.HOT_RANK, "张飞", 1);
    }
}