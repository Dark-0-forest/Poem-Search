package work.darkforest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import work.darkforest.elaticsearch.dao.PoemRepository;
import work.darkforest.entity.po.Poem;
import work.darkforest.mapper.PoemMapper;
import java.util.List;

@Service
public class ScheduledService {
    @Autowired
    private PoemRepository poemRepository;
    @Autowired
    private PoemMapper poemMapper;

    // 每天的2点和14点定时触发
    @Scheduled(cron = "* 2,14 * * *")
    public void buildElasticsearch(){
        // 先删除索引中的所有文档
        poemRepository.deleteAll();
        // 再重新从数据库中读取所有记录，重新添加到es索引库中
        List<Poem> list = poemMapper.list();
        poemRepository.saveAll(list);
    }
}
