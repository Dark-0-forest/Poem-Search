package work.darkforest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.darkforest.entity.po.Poem;
import work.darkforest.mapper.PoemMapper;
import work.darkforest.utils.SpiderUtil;
import java.util.List;

@SpringBootTest
public class InitData {
    @Autowired
    private PoemMapper poemMapper;

    private String[] urls = new String[]{"https://so.gushiwen.cn/gushi/tangshi.aspx", "https://so.gushiwen.cn/gushi/sanbai.aspx",
            "https://so.gushiwen.cn/gushi/songsan.aspx", "https://so.gushiwen.cn/gushi/songci.aspx"};

    @Test
    void initData() throws Exception {
        List<Poem> poems = SpiderUtil.parsePoems(urls[3]);
        poems.forEach(poem->poemMapper.add(poem));
    }
}
