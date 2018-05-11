package uni.mlgb.onlyapp.shit.service.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uni.mlgb.onlyapp.shit.service.model.BaseSmart;
import uni.mlgb.onlyapp.shit.service.model.Player;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by zhangxin516 on 5/11/18
 */
@Service
public class NBADynamicInfoCrawler implements ICrawler {
    private static final Logger logger = LoggerFactory.getLogger(NBADynamicInfoCrawler.class);

    @Value("${crawler.nba-dynamic-info-crawler.url}")
    private String url;

    @Autowired
    private BaseSmart smart;

    private String offsetTitle = "";

    private List<String> regexs = Arrays.asList("[\\n\\r]", "&(\\w+);", "<.*?>");

    @Override
    @Scheduled(cron = "${crawler.nba-dynamic-info-crawler.schedule}")
    public int crawling() {
        logger.info("{} starts crawling...", NBADynamicInfoCrawler.class.getName());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            InputStream is = new URL(url).openStream();
            Document doc = db.parse(is);
            if (doc != null) {
                Map<String, Deque<String>> newTweets = new HashMap<>();
                NodeList items = doc.getElementsByTagName("item");
                ITEM_LOOP:
                for (int i = 0; i < items.getLength(); i++) {
                    Node item = items.item(i);
                    NodeList parts = item.getChildNodes();
                    String title = "";
                    String desc = "";
                    for (int j = 0; j < parts.getLength(); j++) {
                        Node part = parts.item(j);
                        if (part.getNodeType() == Node.ELEMENT_NODE) {
                            Element ele = (Element) part;
                            if ("title".equalsIgnoreCase(ele.getTagName())) {
                                title = ele.getTextContent();
                                if (this.offsetTitle.equalsIgnoreCase(title)) {
                                    logger.info("Catching milestone: {}", title);
                                    break ITEM_LOOP;
                                }
                                if (i == 0)
                                    offsetTitle = title;
                            } else if ("description".equalsIgnoreCase(ele.getTagName())) {
                                StringBuilder sb = new StringBuilder();
                                String paraContent = ele.getTextContent();
                                logger.debug("Content text in desc: {}", paraContent);
                                int startPara = 0;
                                int endPara = startPara;
                                String paraLeftTag = "<p>";
                                String paraRightTag = "</p>";
                                while (endPara < paraContent.length() - 1) {
                                    startPara = paraContent.indexOf(paraLeftTag, endPara + paraRightTag.length());
                                    if (startPara == -1)
                                        break;
                                    endPara = paraContent.indexOf(paraRightTag, startPara + paraLeftTag.length());
                                    if (endPara == -1)
                                        break;
                                    sb.append(paraContent.substring(startPara + paraLeftTag.length(), endPara));
                                }
                                desc = sb.toString();
                                logger.debug("Origin, desc: {}", desc);
                                desc = filterDesc(desc);
                                logger.debug("Removed special characters in HTML, desc: {}", desc);
                            } else
                                ;
                        }
                    }
                    for (Player player : smart.getPlayersDict()) {
                        for (String alias : player.getAliases()) {
                            if (title.contains(alias)) {
                                Deque<String> tweets = newTweets.get(player.getUname());
                                if (tweets == null) {
                                    tweets = new LinkedList<>();
                                }
                                tweets.push(desc);
                                newTweets.put(player.getUname(), tweets);
                                break;
                            }
                        }
                    }
                }
                Map<String, List<String>> rst = new HashMap<>();
                int numPlayer = newTweets.size();
                int numTweet = 0;
                for (Map.Entry<String, Deque<String>> ent : newTweets.entrySet()) {
                    numTweet += ent.getValue().size();
                    List<String> tweets = new ArrayList<>();
                    for (String tweet : ent.getValue()) {
                        tweets.add(tweet);
                    }
                    rst.put(ent.getKey(), tweets);
                }
                logger.info("Crawled {} tweets for {} players.", numTweet, numPlayer);
                is.close();
                return smart.addTweets(rst);
            }
        } catch (ParserConfigurationException e) {
            logger.error(e.toString());
        } catch (MalformedURLException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (SAXException e) {
            logger.error(e.toString());
        }
        return 0;
    }

    private String filterDesc(String desc) {
        for (String regex: regexs) {
            desc = desc.replaceAll(regex, "");
        }
        return desc;
    }
}
