package uni.mlgb.onlyapp.shit.controller;

import com.google.gson.Gson;
import com.jd.alpha.skill.client.entity.request.SkillData;
import com.jd.alpha.skill.client.entity.response.SkillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uni.mlgb.onlyapp.shit.service.alpha.SampleHandler;

/**
 * @author leo
 */
@RestController
public class SampleController {
    @Autowired
    private SampleHandler handler;

    /**
     * @param    requestBody    请求JSON字符串
     * @return SkillResponse
     */
    @PostMapping(value = "/sample", produces = {
            "application/json;charset=UTF-8"}, consumes = {"application/json"})
    public SkillResponse index(@RequestBody String requestBody) {
        // 将得到的JSON数据转换为SkillData对象,并交由Handler进行处理
        SkillData data = new Gson().fromJson(requestBody, SkillData.class);
        SkillResponse response = handler.handle(data);
        return response;
    }
}

