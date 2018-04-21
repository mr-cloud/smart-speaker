package uni.mlgb.onlyapp.shit.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jd.alpha.skill.client.constant.RequestTypeConstants;
import com.jd.alpha.skill.client.entity.request.SkillData;
import com.jd.alpha.skill.client.entity.request.SkillRequestSlot;
import com.jd.alpha.skill.client.entity.response.SkillResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uni.mlgb.onlyapp.shit.service.alpha.AlphaUtils;
import uni.mlgb.onlyapp.shit.service.alpha.SampleHandler;
import uni.mlgb.onlyapp.shit.service.model.SkillNameConsts;
import uni.mlgb.onlyapp.shit.service.model.SlotNameConsts;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author leo
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
public class SampleControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(SampleControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    private SkillResponse skillResponse;
    private SkillData skillData;
    @MockBean
    private SampleHandler handler;

    private Gson gson = new GsonBuilder().serializeNulls().create();

    @Value("${app.id}")
    private String APP_ID;

    @Before
    public void setUp() throws Exception {
        skillResponse = AlphaUtils.buildSimpleResponse(APP_ID,
                false,
                "凯里欧文是NBA全明星控卫。");
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
    public void index() throws Exception {
        String requestBody = new Gson().toJson(skillData);
        logger.info("Request Json String: {}", requestBody);
        given(handler.handle(any())).willReturn(skillResponse);
        String skillResponseStr = gson.toJson(skillResponse);
        logger.info("Response Json String: {}", skillResponseStr);
        mockMvc.perform(post("/sample").content(requestBody).accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().json(skillResponseStr, true));
    }
}