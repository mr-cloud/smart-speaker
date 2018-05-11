package uni.mlgb.onlyapp.shit.service.crawler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uni.mlgb.onlyapp.shit.service.model.BaseSmart;

import static org.junit.Assert.*;

/**
 * Created by zhangxin516 on 5/11/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class NBADynamicInfoCrawlerTest {
    private Logger logger = LoggerFactory.getLogger(NBADynamicInfoCrawler.class);

    @Autowired
    private NBADynamicInfoCrawler crawler;


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void crawling() {
        assertTrue(crawler.crawling() > 0) ;
    }
}