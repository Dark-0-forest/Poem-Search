package work.darkforest.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import work.darkforest.entity.po.Poem;
import work.darkforest.mapper.PoemMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpiderUtil {

    static String pre = "https://so.gushiwen.cn/";

    public static List<Poem> parsePoems(String url) throws Exception {
        // 解析网页
        Document document = Jsoup.parse(new URL(url), 10000);
        Elements elements = document.getElementsByClass("typecont");
        List<Poem> poems = new ArrayList<>();
        for (Element element : elements) {
            // 找到对应的类型
            String type = element.getElementsByClass("bookMl").eq(0).text();
            // 找到所有的a标签
            Elements aTags = element.getElementsByTag("a");
            // 生成对应古诗的地址，传给parsePoem解析为Poem对象放入poems中
            aTags.forEach(aTag-> {
                try { poems.add(parsePoem(pre + aTag.attr("href"), type)); }
                catch (IOException e) { System.out.println(e.getMessage()); }
            });
        }
        return poems;
    }

    public static Poem parsePoem(String url, String type) throws IOException {
        Poem poem = new Poem()
                .setId(UUID.randomUUID().toString())
                .setPoemType(type)
                .setPoemSource("网络");
        Document document = Jsoup.parse(new URL(url), 10000);

        poem.setPoemName(document.getElementsByTag("h1").text().replaceAll(" ", ""));    // 获取诗名
        Elements source = document.getElementsByClass("source");
        String author = source.get(0).text();
        poem.setAuthor(author.substring(0, author.indexOf("〔")))    // 获取作者姓名
            .setAuthorDes(parseAuthorDes(pre + source.get(0).getElementsByTag("a").get(0).attr("href"))) // 获取作者简介
            .setPoemContent(document.getElementsByClass("contson").get(0).text());   // 获取诗歌内容
        return poem;
    }

    public static String parseAuthorDes(String url) throws IOException {
        Document document = Jsoup.parse(new URL(url), 10000);
        // 获取作者的简介信息
        String des = document.getElementsByClass("main3").get(0).getElementsByTag("p").get(0).text();
        int index = des.indexOf('►');
        if (index > 0) return des.substring(0, index);
        else return des;
    }

}
