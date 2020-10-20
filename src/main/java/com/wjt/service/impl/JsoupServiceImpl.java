package com.wjt.service.impl;

import com.wjt.service.JsoupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JsoupServiceImpl implements JsoupService {
    @Override
    public void saveUniversityDetail(final String url) {
        Connection conn = Jsoup.connect(url);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("", "");
        headerMap.put("", "");

        try {
            Document doc = conn.get();

            doc.charset(Charset.forName("utf8"));

            String title = doc.title();
            Element body = doc.body();
            

            Elements elements = body.getElementsByClass("in_T");

            //log.info("title={};body={};elements.size={};", title, body,elements.size());

            for (Element e:elements){
                log.info("---------");
                //log.info("te={};",e);
                Element tbody = e.tagName("tbody");

                Elements elementsByITx2 = tbody.getElementsByClass("IT_x2");
                if(CollectionUtils.isNotEmpty(elementsByITx2)){
                    Element element = elementsByITx2.get(0);

                    String ownText = element.ownText();
                    if(ownText.equalsIgnoreCase("男")){
                        continue;
                    }
                    //log.info("ownText={};",ownText);
                }

                String wholeText = tbody.wholeText();
                Elements elementsByATag = tbody.getElementsByTag("a");

                if(CollectionUtils.isNotEmpty(elementsByATag)){
                    String href = elementsByATag.get(0).attr("href");
                    log.info("wholeText={};href={};",wholeText,href);
                }
            }
        } catch (Exception e) {
            log.error("get doc error!url={};", url, e);
        }
    }
}
