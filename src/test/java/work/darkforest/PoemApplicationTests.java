package work.darkforest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.darkforest.entity.po.Poem;
import work.darkforest.mapper.PoemMapper;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class PoemApplicationTests {
    @Autowired
    private PoemMapper poemMapper;

    @Test
    void contextLoads() {
        List<Poem> poems = poemMapper.list();
        poems.forEach(res-> System.out.println(res));
    }

    @Test
    void testadd(){
        Poem poem = new Poem(UUID.randomUUID().toString(), "行宫", "元稹", "唐诗","网络", "寥落古行宫，宫花寂寞红。白头宫女在，闲坐说玄宗.",
                "元稹（779年－831年，或唐代宗大历十四年至文宗大和五年），字微之，别字威明，唐洛阳人（今河南洛阳）。父元宽，母郑氏。为北魏宗室鲜卑族拓跋部后裔，是什翼犍之十四世孙。早年和白居易共同提倡“新乐府”。世人常把他和白居易并称“元白”。");
        poemMapper.add(poem);
    }
    @Test
    void update(){
        Poem poem = new Poem(UUID.randomUUID().toString(), "行宫", "元稹", "唐诗","网络", "寥落古行宫，宫花寂寞红。白头宫女在，闲坐说玄宗.",
                "元稹（779年－831年，或唐代宗大历十四年至文宗大和五年），字微之，别字威明，唐洛阳人（今河南洛阳）。父元宽，母郑氏。为北魏宗室鲜卑族拓跋部后裔，是什翼犍之十四世孙。早年和白居易共同提倡“新乐府”。世人常把他和白居易并称“元白”。");
        poemMapper.update(poem);
    }

    @Test
    void testDel(){
        poemMapper.delById("b6b555f5-0ca8-4b76-9378-ad8cfd880b89");
    }

    @Test
    void count(){
        System.out.println(poemMapper.count());
    }
}
