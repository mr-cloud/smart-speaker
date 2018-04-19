package uni.mlgb.onlyapp.shit.service.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leo
 */
@Component
public class BaseSmart {
    private Map<String, String> nbaDb = new HashMap<>();
    private Map<String, List<String>> tweetsDb = new HashMap<>();

    public BaseSmart() {
        nbaDb.put("凯里欧文", "凯里·欧文（Kyrie Irving），1992年3月23日出生于澳大利亚墨尔本，拥有美国/澳大利亚双重国籍，职业篮球运动员，司职控球后卫，效力于NBA波士顿凯尔特人队。");
        nbaDb.put("勒布朗詹姆斯", "勒布朗·詹姆斯（LeBron James），1984年12月30日出生在美国俄亥俄州阿克伦，美国职业篮球运动员，司职小前锋，绰号“小皇帝”，效力于NBA克利夫兰骑士队。");
        tweetsDb.put("凯里欧文", Arrays.asList("凯里欧文因接受膝伤手术，将缺席整个2017至2018的NBA季后赛。"));
        tweetsDb.put("勒布朗詹姆斯", Arrays.asList("勒布朗詹姆斯的骑士队，在2017至2018赛季的NBA季后赛中，第一轮暂时处于0比1落后。"));
    }

    public String sayHello() {
        return "欢迎聊球！";
    }

    public String staticInfo(String sport, String star) {
        String info = nbaDb.get(star);
        if (info == null) {
            info = nbaDb.get("凯里欧文");
        }
        return info;
    }

    public String dynamicInfo(String star, int offset) {
        List<String> tweets = tweetsDb.get(star);
        if (tweets == null || tweets.size() == 0) {
            return tweetsDb.get("凯里欧文").get(0);
        }
        return tweets.get(0);
    }
}
