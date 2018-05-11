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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin516 on 5/11/18
 */
@Service
public class NBADynamicInfoCrawler implements ICrawler{
    private static final Logger logger = LoggerFactory.getLogger(NBADynamicInfoCrawler.class);

    @Value("${crawler.nba-dynamic-info-crawler.url}")
    private String url;

    @Autowired
    private BaseSmart smart;

    @Override
    @Scheduled(cron = "${crawler.nba-dynamic-info-crawler.schedule}")
    public void crawling() {
        logger.info("{} starts crawling...", NBADynamicInfoCrawler.class.getName());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(url).openStream());
            if(doc != null) {
                NodeList items = doc.getElementsByTagName("item");
                for(int i = 0; i < items.getLength(); i++) {
                    Node item = items.item(i);
                    NodeList parts = item.getChildNodes();
                    for (int j = 0; j < parts.getLength(); j++) {
                        Node part = parts.item(j);
                        if (part.getNodeType() == Node.ELEMENT_NODE) {
                            Element ele = (Element)part;
                            String title = "";
                            String desc = "";
                            if ("title".equalsIgnoreCase(ele.getTagName())) {
                                title = ele.getTextContent();
                            }
                            else if ("description".equalsIgnoreCase(ele.getTagName())) {
                                StringBuilder sb = new StringBuilder();
                                NodeList paras = ele.getElementsByTagName("p");
                                for (int k = 0; k < paras.getLength(); k++) {
                                    Element para = (Element)paras.item(k);
                                    sb.append(para.getTextContent());
                                }
                                desc = sb.toString();
                                logger.debug("Origin, desc: {}", desc);
                                desc = desc.replaceAll("[\\n\\r]", "");
                                desc = desc.replaceAll("^&\\w+;$", "");
                                logger.debug("Removed special characters in HTML, desc: {}", desc);
                            }
                            else
                                ;
                            Map<String, List<String>> newTweets = new HashMap<>();
                            for(Player player: smart.getPlayersDict()) {
                                for (String alias: player.getAliases()) {
                                    if (title.contains(alias)) {
                                        List<String> tweets = newTweets.get(player.getUname());
                                        if (tweets == null) {
                                            tweets = new ArrayList<>();
                                        }
                                        tweets.add(desc);
                                        newTweets.put(player.getUname(), tweets);
                                        break;
                                    }
                                }
                            }
                            smart.addTweets(newTweets);
                        }
                    }
                }
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
    }
}
