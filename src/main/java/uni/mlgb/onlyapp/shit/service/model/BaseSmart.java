package uni.mlgb.onlyapp.shit.service.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leo
 */
@Component
public class BaseSmart {
    private Map<String, String> nbaDb = new HashMap<>();

    public BaseSmart() {
        nbaDb.put("凯里欧文", "凯里·欧文（Kyrie Irving），1992年3月23日出生于澳大利亚墨尔本，拥有美国/澳大利亚双重国籍，职业篮球运动员，司职控球后卫，效力于NBA波士顿凯尔特人队。");
        nbaDb.put("勒布朗詹姆斯", "勒布朗·詹姆斯（LeBron James），1984年12月30日出生在美国俄亥俄州阿克伦，美国职业篮球运动员，司职小前锋，绰号“小皇帝”，效力于NBA克利夫兰骑士队。");
    }

    public String sayHello() {
        return "欢迎聊球！我可以告诉你运动员的基本信息和他们的动态呢！";
    }

    public String staticInfo(String sport, String star) {
        String info = nbaDb.get(star);
        if (info == null) {
            info = ModelConsts.CONSCIOUSNESS_WORDS + nbaDb.get("凯里欧文");
        }
        return info;
    }
}
