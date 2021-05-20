package work.darkforest.service;

import work.darkforest.entity.redis.HotRank;

import java.util.List;

public interface RedisService {

    // 让一个标签自增1，如果此标签不存在就自动创建
    void incrTag(String tag);

    // 返回redis中的热榜搜索
    List<HotRank> list();
}