package uni.mlgb.onlyapp.shit.service.alpha;

import com.jd.alpha.skill.client.constant.IntentTypeConstants;
import com.jd.alpha.skill.client.constant.RequestTypeConstants;
import com.jd.alpha.skill.client.entity.request.*;
import com.jd.alpha.skill.client.entity.response.SkillResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uni.mlgb.onlyapp.shit.Application;
import uni.mlgb.onlyapp.shit.service.model.ModelConsts;
import uni.mlgb.onlyapp.shit.service.model.SkillNameConsts;
import uni.mlgb.onlyapp.shit.service.model.SlotNameConsts;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author leo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SampleHandlerTest {

    @Autowired
    private SampleHandler handler;

    private SkillData skillData;

    @Value("${app.id}")
    private String APP_ID;

    @Before
    public void setUp() throws Exception {
        Map<String, SkillRequestSlot> slots = new HashMap<>();
        slots.put(SlotNameConsts.SPORT, SkillRequestSlot.builder().value("篮球").build());
        slots.put(SlotNameConsts.STAR, SkillRequestSlot.builder().value("凯里欧文").build());
        skillData = AlphaUtils.buildSimpleSkillData("1", APP_ID, RequestTypeConstants.INTENT_REQUEST,
                SkillNameConsts.STATIC_INFO, slots);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void handle() {
        SkillResponse resp = handler.handle(skillData);
        assertEquals(APP_ID, resp.getSkill());
        assertTrue(resp.getResponse().getOutput().getText().startsWith("凯里·欧文（Kyrie Irving）"));

        skillData = AlphaUtils.buildSimpleSkillData("1", APP_ID, RequestTypeConstants.INTENT_REQUEST,
                IntentTypeConstants.HELP_INTENT, null);
        resp = handler.handle(skillData);
        assertEquals(ModelConsts.HELP_MENU, resp.getResponse().getOutput().getText());

        skillData = AlphaUtils.buildSimpleSkillData("1", APP_ID, RequestTypeConstants.LAUNCH_REQUEST,
                null, null);
        resp = handler.handle(skillData);
        assertEquals(ModelConsts.HELLO_WORDS, resp.getResponse().getOutput().getText());
    }
}