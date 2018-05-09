package uni.mlgb.onlyapp.shit.service.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by leo on 11/28/17.
 */
public interface IDownloader {
    HtmlPage processUrl(String url);
}
