package uni.mlgb.onlyapp.shit.service.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

/**
 * @author leo
 */
@Component
public class BaseSmart {
    private Map<String, String> nbaDb = new HashMap<>();
    private Map<String, Deque<String>> tweetsDb = new HashMap<>();
    private Set<Player> playersDict = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(BaseSmart.class);

    @Autowired
    private ResourceLoader resourceLoader;

    public BaseSmart() {
        nbaDb.put("凯里欧文", "凯里·欧文（Kyrie Irving），1992年3月23日出生于澳大利亚墨尔本，拥有美国/澳大利亚双重国籍，职业篮球运动员，司职控球后卫，效力于NBA波士顿凯尔特人队。");
        nbaDb.put("勒布朗詹姆斯", "勒布朗·詹姆斯（LeBron James），1984年12月30日出生在美国俄亥俄州阿克伦，美国职业篮球运动员，司职小前锋，绰号“小皇帝”，效力于NBA克利夫兰骑士队。");
        tweetsDb.put("凯里欧文", new LinkedList<>(Arrays.asList("凯里欧文因接受膝伤手术，将缺席整个2017至2018的NBA季后赛。")));
        tweetsDb.put("勒布朗詹姆斯", new LinkedList<>(Arrays.asList("勒布朗詹姆斯的骑士队，在2017至2018赛季的NBA季后赛中，第一轮暂时处于1比1平局，期待勒布朗的一如既往的天神下凡表演时刻。")));
    }

    @PostConstruct
    public void initialize() {
        logger.info("{} players signed.", initDB());
    }

    /**
     * Initialize players database from local file
     *
     * @return number of players in database
     */
    private int initDB() {
        Resource resource = resourceLoader.getResource(ModelConsts.STATIC_INFO_PATH);
        if (!resource.exists()) {
            logger.warn("{} not found!", ModelConsts.STATIC_INFO_PATH);
            return -1;
        }
        try {
            InputStream inputStream = resource.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
            String playerInfo;
            while ((playerInfo = bfr.readLine()) != null) {
                int sepIndex = playerInfo.indexOf("|");
                if (sepIndex != -1) {
                    String[] names = playerInfo.substring(0, sepIndex).split(",");
                    if (names.length >= 2) {
                        String chineseName = names[1].replaceAll("[ -\\.·]", "");
                        this.nbaDb.put(chineseName, playerInfo.substring(sepIndex + 1));
                        Player player = new Player(chineseName);
                        Set<String> aliases = player.getAliases();
                        if (aliases == null) {
                            aliases = new HashSet<>();
                            player.setAliases(aliases);
                        }
                        aliases.addAll(Arrays.asList(names[1].split("[ -\\.·]")));
                        playersDict.add(player);
                    }
                }
            }
            return this.nbaDb.size();
        } catch (FileNotFoundException e) {
            logger.warn("{} not found!", ModelConsts.STATIC_INFO_PATH);
            return -1;
        } catch (IOException e) {
            logger.warn(e.toString());
            return -1;
        }
    }

    public String sayHello() {
        return ModelConsts.HELLO_WORDS;
    }

    public String staticInfo(String sport, String star) {
        String info = nbaDb.get(star);
        if (info == null) {
            star = "凯里欧文";
            info = ModelConsts.CONSCIOUSNESS_WORDS + nbaDb.get(star);
        }
        return info + ModelConsts.INTRODUCTION_WORDS_DYNAMIC_INFO.replaceAll(ModelConsts.PLACE_HOLDER_STAR,
                star);
    }

    public synchronized String dynamicInfo(String star, int offset) {
        Deque<String> tweets = tweetsDb.get(star);
        if (tweets == null) {
            return ModelConsts.CONSCIOUSNESS_WORDS + tweetsDb.get("凯里欧文").peek();
        } else if (tweets.size() == 0) {
            return ModelConsts.SILENCE_WORDS;
        } else
            return tweets.peek();
    }

    public int getNbaDbSize() {
        return nbaDb.size();
    }

    public int getTweetsDbSize() {
        return tweetsDb.size();
    }

    public Map<String, String> getNbaDb() {
        return nbaDb;
    }

    public Set<Player> getPlayersDict() {
        return playersDict;
    }

    /**
     * tweets in order chronologically
     *
     * @param newTweets
     * @return
     */
    public synchronized int addTweets(Map<String, List<String>> newTweets) {
        int numUpdated = 0;
        for (Map.Entry<String, List<String>> ent : newTweets.entrySet()) {
            Deque<String> tweets = tweetsDb.get(ent.getKey());
            if (tweets == null) {
                tweets = new LinkedList<>();
                tweetsDb.put(ent.getKey(), tweets);
            }
            for (String tweet : ent.getValue()) {
                tweets.addFirst(tweet);
                if (tweets.size() > ModelConsts.TWEETS_CAPACITY) {
                    tweets.removeLast();
                }
            }
            numUpdated += 1;
        }
        return numUpdated;
    }
}
