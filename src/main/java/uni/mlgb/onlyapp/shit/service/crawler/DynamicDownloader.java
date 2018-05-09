package uni.mlgb.onlyapp.shit.service.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by leo on 11/28/17.
 */
@Component
public class DynamicDownloader implements IDownloader {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDownloader.class);

    @Override
    public HtmlPage processUrl(String url) {
        try {
            HtmlPage page = getHtmlPage(url);
            logger.debug("page title: {}", page.getTitleText());
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HtmlPage getHtmlPage(String url) throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);
        webClient.getOptions().setCssEnabled(false);
        // Endure js exception.
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage page = webClient.getPage(url);
        logger.debug("Crawling {} ...", url);
        TimeUnit.SECONDS.sleep(3);  //web请求数据需要时间，必须让主线程休眠片刻
        webClient.close();
        return page;
    }
}
