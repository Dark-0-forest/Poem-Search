package work.darkforest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import work.darkforest.entity.redis.HotRank;
import work.darkforest.service.RedisService;
import work.darkforest.utils.constant.SystemConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void incrTag(String tag) {
        redisTemplate.opsForZSet().incrementScore(SystemConstant.HOT_RANK, tag, SystemConstant.HOT_RANG_INCR_SCORE);
    }

    @Override
    public List<HotRank> list() {
        // 查询所有分数大于5的信息，相当于热榜
        Set range = redisTemplate.opsForZSet().reverseRangeByScore(SystemConstant.HOT_RANK, SystemConstant.HOT_RANK_START_SCORE, Double.POSITIVE_INFINITY);
        List<HotRank> ranks = new ArrayList<>();
        for (Object value : range) {
            Double score = redisTemplate.opsForZSet().score(SystemConstant.HOT_RANK, value.toString());
            ranks.add(new HotRank(value.toString(), score));
        }
        return ranks;
    }
}
