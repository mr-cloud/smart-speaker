package uni.mlgb.onlyapp.shit.service.alpha;

import com.jd.alpha.skill.client.RequestHandler;
import com.jd.alpha.skill.client.entity.request.SkillData;
import com.jd.alpha.skill.client.entity.request.SkillRequestSlot;
import com.jd.alpha.skill.client.entity.response.SkillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uni.mlgb.onlyapp.shit.service.model.BaseSmart;
import uni.mlgb.onlyapp.shit.service.model.SkillNameConsts;
import uni.mlgb.onlyapp.shit.service.model.SlotNameConsts;

import java.util.Map;

/**
 * @author leo
 */
@Component
public class SampleHandler extends RequestHandler {
    @Autowired
    private BaseSmart smart;

    /**
     * 请求合法性校验
     *
     * @param skillData Skill请求数据
     * @return boolean
     */
    @Override
    public boolean validate(SkillData skillData) {
        String appId = skillData.getSession().getApplication().getApplicationId();
        // TODO validate with appId.
        return true;
    }

    /**
     * Session开始时的响应
     *
     * @param skillData Skill请求数据
     */
    @Override
    public void onSessionStarted(SkillData skillData) {
        // Maintain states for each guest.
        // TODO
    }

    /**
     * 技能启动时的响应(一般返回欢迎语句)
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onLaunchRequest(SkillData skillData) {
        String hello = smart.sayHello();
        return AlphaUtils.buildSimpleResponse(skillData.getSession().getApplication().getApplicationId(),
                false,
                hello);
    }

    /**
     * 自定义意图时的响应(用户在Alpha平台自行定义的意图)
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onIntentRequest(SkillData skillData) {
        String intentName = skillData.getRequest().getIntent().getName();
        Map<String, SkillRequestSlot> slots = skillData.getRequest().getIntent().getSlots();
        String info = null;
        switch (intentName) {
            case SkillNameConsts.STATIC_INFO: {
                info = smart.staticInfo(slots.get(SlotNameConsts.SPORT).getValue(),
                        slots.get(SlotNameConsts.STAR).getValue());
                break;
            }
            case SkillNameConsts.DYNAMIC_INFO:
                int offsetForSpecUser = 0;
                info = smart.dynamicInfo(slots.get(SlotNameConsts.STAR).getValue(), offsetForSpecUser);
                break;
            default:
                ;
        }
        return AlphaUtils.buildSimpleResponse(skillData.getSession().getApplication().getApplicationId(),
                false,
                info);
    }

    /**
     * Session超时退出
     *
     * @param skillData Skill请求数据
     */
    @Override
    public void onSessionEndedRequest(SkillData skillData) {
        // Shutdown user's session state
        // TODO
    }

    /**
     * 取消意图时的响应
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onCancelIntent(SkillData skillData) {
        return null;
    }

    /**
     * 帮助意图时的响应
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onHelpIntent(SkillData skillData) {
        return null;
    }

    /**
     * 下一个意图时的响应
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onNextIntent(SkillData skillData) {
        return null;
    }

    /**
     * 重复播报意图时的响应(暂未支持)
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onRepeatIntent(SkillData skillData) {
        return null;
    }

    /**
     * 其他内置意图时的响应
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse onOtherBuildInIntent(SkillData skillData) {
        return null;
    }

    /**
     * 默认响应
     *
     * @param skillData Skill请求数据
     * @return SkillResponse
     */
    @Override
    public SkillResponse defaultResponse(SkillData skillData) {
        return null;
    }
}
