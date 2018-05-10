package uni.mlgb.onlyapp.shit.service.crawler;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangxin516 on 5/9/18
 */
public class DataCollector {
    private static IDownloader iDownloader = new DynamicDownloader();

    public static int collectNBAPlayer(String url, BufferedWriter bfw) throws IOException {
        int numPlayers = 0;
        HtmlPage htmlPage = iDownloader.processUrl(url);
        DomNodeList<DomElement> tables = htmlPage.getElementsByTagName("table");
        for (DomElement table : tables) {
            if (table.hasAttribute("class") && "qiuyuandt01".equalsIgnoreCase(table.getAttribute("class"))) {
                DomNodeList<HtmlElement> anchors = table.getElementsByTagName("a");
                for (HtmlElement anchor : anchors) {
                    if (anchor.hasAttribute("href") && anchor.getAttribute("href").contains("player")) {
                        String player = anchor.getTextContent();
                        System.out.println("Player signed: " + player);
                        bfw.write(player);
                        bfw.newLine();
                        bfw.flush();
                        numPlayers += 1;
                    }
                }
            }
        }
        bfw.close();
        return numPlayers;
    }

    /**
     * FIXME reconstruct: collectInfoFromWebPageContent
     * @param urlPrefix
     * @param bfw
     * @param bfr
     * @return
     */
    public static int collectPlayerStaticInfo(String urlPrefix, BufferedWriter bfw, BufferedReader bfr) throws IOException {
        int numPlayersFetched = 0;
        int numPlayers = 0;
        String playerCard;
        while ((playerCard = bfr.readLine()) != null) {
            numPlayers += 1;
            StringBuilder namePattern = new StringBuilder();
            String[] names = playerCard.split(",");
            if (names.length >= 2) {
                String[] firstLast = names[1].split("-");
                for (String part: firstLast) {
                    namePattern.append(part).append("·");
                }
                String url = urlPrefix + namePattern.substring(0, namePattern.length() - 1);
                HtmlPage htmlPage = iDownloader.processUrl(url);
                if (htmlPage == null) {
                    System.out.println("[WARN] cannot fetch content, url=" + url);
                    continue;
                }
                else
                    System.out.println("[INFO] fetching content, url=" + url);
                DomNodeList<DomElement> divs = htmlPage.getElementsByTagName("div");
                for (DomElement div : divs) {
                    if (div.hasAttribute("class") && "lemma-summary".equalsIgnoreCase(div.getAttribute("class"))) {
                        StringBuilder sb = new StringBuilder();
                        for(DomElement para: div.getChildElements()) {
                            if(para.hasAttribute("class") && "para".equalsIgnoreCase(para.getAttribute("class"))) {
                                String desc = para.getTextContent().trim();
                                desc = desc.replaceAll("\\n", "");
                                desc = desc.replaceAll("\\r", "");
                                sb.append(desc);
                            }
                        }
                        bfw.write(playerCard + "|" + sb.toString());
                        bfw.newLine();
                        bfw.flush();
                        numPlayersFetched += 1;
                    }
                }
            }

        }
        System.out.println("#players=" + numPlayers + ", #fetched=" + numPlayersFetched);
        bfr.close();
        bfw.close();
        return numPlayersFetched;
    }

    public static int generatePlayerSlotValues(BufferedReader bfr, BufferedWriter bfw) throws IOException {
        int cnt = 0;
        int oriCnt = 0;
        String sentence;
        Set<String> existed = new HashSet<>();
        while ((sentence = bfr.readLine()) != null) {
            oriCnt += 1;
            String[] names = sentence.split(",");
            if (names.length >= 2) {
                String slotVal = names[1].replaceAll("[ -\\.·]", "");
                if (existed.contains(slotVal)) {
                    continue;
                }
                existed.add(slotVal);
                List<String> aliases = Arrays.asList(names[0].replaceAll("[ -\\.·]", ""));
                StringBuilder sb = new StringBuilder();
                for (String alias: aliases) {
                    if(existed.contains(alias)) {
                        continue;
                    }
                    existed.add(alias);
                    sb.append(alias).append("/");
                }
                bfw.write(slotVal + "," + (sb.length() == 0? "": sb.substring(0, sb.length() - 1)));
                bfw.newLine();
                cnt += 1;
            }
        }
        System.out.println("Origin number: " + oriCnt + ", Add number: " + cnt);
        return cnt;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Args cannot be empty!");
        } else if ("nbaPlayer".equalsIgnoreCase(args[0])) {
            String url = "http://nba.sports.163.com/player/";
            String home = System.getProperty("user.home");
            String filename = home + "/" + "datahouse/logs/onlyshit/" + "nba-players.txt";
            try {
                BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(filename)));
                System.out.println("Data will stored in " + filename);
                int cnt = collectNBAPlayer(url, bfw);
                System.out.println(cnt + " players names info collected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ("static".equalsIgnoreCase(args[0])) {
            String urlPrefix = "https://baike.baidu.com/item/";
            String home = System.getProperty("user.home");
            String filenamePlayer = home + "/" + "datahouse/logs/onlyshit/" + "nba-players.txt";
            String filenameStatic = home + "/" + "datahouse/logs/onlyshit/" + "nba-players-static.txt";
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(new File(filenamePlayer)));
                BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(filenameStatic)));
                int cnt = collectPlayerStaticInfo(urlPrefix, bfw, bfr);
                System.out.println(cnt + " players static info collected.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ("slotPlayer".equalsIgnoreCase(args[0])) {
            String home = System.getProperty("user.home");
            String filenamePlayer = home + "/" + "datahouse/logs/onlyshit/" + "nba-players.txt";
            String workDir = System.getProperty("user.dir");
            String filenamePlayerSlot = workDir + "/" + "src/main/resources/datahouse/" + "nba-players-slot.csv";
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(new File(filenamePlayer)));
                BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(filenamePlayerSlot)));
                int cnt = generatePlayerSlotValues(bfr, bfw);
                System.out.println(cnt + " players' slots added.");
                bfr.close();
                bfw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Your args cannot be supported yet!");
        }
    }
}
