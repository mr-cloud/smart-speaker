package uni.mlgb.onlyapp.shit.service.alpha;

import com.jd.alpha.skill.client.constant.ResponseOutputTypeConstants;
import com.jd.alpha.skill.client.entity.response.SkillResponse;
import com.jd.alpha.skill.client.entity.response.SkillResponseDetails;
import com.jd.alpha.skill.client.entity.response.SkillResponseOutput;

/**
 * @author leo
 */
public class AlphaUtils {
    /**
     * 构建简单技能响应
     *
     * @param    skillApplicationId    技能ID
     * @param    endSession                                    是否结束会话
     * @param    msg                                                                响应消息
     * @return SkillResponse    技能响应消息
     */
    public static SkillResponse buildSimpleResponse(String skillApplicationId, boolean endSession, String msg) {
        return SkillResponse.builder()
                .skill(skillApplicationId)
                .shouldEndSession(endSession)

                .response(SkillResponseDetails.builder()

                        .output(SkillResponseOutput.builder()
                                .text(msg)

                                .type(ResponseOutputTypeConstants.PLAIN_TEXT)
                                .build())
                        .build())
                .build();
    }

}
