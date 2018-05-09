package uni.mlgb.onlyapp.shit.service.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import uni.mlgb.onlyapp.shit.Application;

import static org.junit.Assert.*;

/**
 * Created by zhangxin516 on 5/9/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BaseSmartTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private BaseSmart baseSmart;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getNbaDbSize() {
        assertTrue(this.baseSmart.getNbaDbSize() >= 2);
    }
}