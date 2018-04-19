package uni.mlgb.onlyapp.shit.service.alpha;

import com.jd.alpha.skill.client.constant.RequestTypeConstants;
import com.jd.alpha.skill.client.entity.request.*;
import com.jd.alpha.skill.client.entity.response.SkillResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uni.mlgb.onlyapp.shit.Application;
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

    @Before
    public void setUp() throws Exception {
        Map<String, SkillRequestSlot> slots = new HashMap<>();
        slots.put(SlotNameConsts.SPORT, SkillRequestSlot.builder().value("篮球").build());
        slots.put(SlotNameConsts.STAR, SkillRequestSlot.builder().value("凯里欧文").build());
        skillData.builder()
                .session(SkillSession.builder().sessionId("1")
                        .application(SkillSessionApplicationInfo.builder().applicationId("1")
                                .build())
                        .contexts(new HashMap<>())
                        .isNew(true)
                        .build())
                .request(SkillRequest.builder().type(RequestTypeConstants.INTENT_REQUEST)
                        .intent(SkillRequestIntent.builder().name(SkillNameConsts.STATIC_INFO)
                                .slots(slots).build())
                        .build())
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void handle() {
        SkillResponse resp = handler.handle(skillData);
        assertEquals("1", resp.getSkill());
        assertTrue(resp.getResponse().getOutput().getText().startsWith("凯里·欧文（Kyrie Irving）"));
    }
}