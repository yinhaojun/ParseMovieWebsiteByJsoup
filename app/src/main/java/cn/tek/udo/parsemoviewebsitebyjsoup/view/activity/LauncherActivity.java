package cn.tek.udo.parsemoviewebsitebyjsoup.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.tek.udo.parsemoviewebsitebyjsoup.R;
import cn.tek.udo.parsemoviewebsitebyjsoup.bean.MovieInfo;

public class LauncherActivity extends Activity {

    private List<MovieInfo> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                parseWebsite();
//            }
//        }).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
            }
        }, 500);
    }


    private void parsePager(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();
            Elements thumbnails = doc.getElementsByClass("thumbnail");

            for (int i = 0; i < thumbnails.size(); i++) {
                MovieInfo info = new MovieInfo();
                Element data = thumbnails.get(i);
                Elements href = data.select("a[href]");
                Elements src = data.select("img[src]");
                if (href.size() == 1) {
                    Element e = href.get(0);
                    String detail = e.attr("href");
                    String title = e.attr("title");
                    if (src.size() == 1) {
                        Element e1 = src.get(0);
                        String img = e1.attr("src");
                        info.setPosterUrl(img);
                    }
                    info.setDetailUrl(detail);
                    info.setTitle(title);
                    movies.add(info);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseWebsite() {
        try {
            Document doc = Jsoup.connect("http://www.ashvsash.com/category/%E7%94%B5%E5%BD%B1")
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();
            Elements paginations = doc.getElementsByClass("pagination");
            Elements pages = paginations.select("a[href]");
            Element finalPager = pages.get(pages.size() - 1);
            String finalUrl = finalPager.attr("href");
            long pageLength = Long.valueOf(finalUrl.substring(finalUrl.lastIndexOf("/") + 1));
            String pagerBaseUrl = finalUrl.substring(0, finalUrl.lastIndexOf("/") + 1);
            for (int i = 1; i <= pageLength; i++) {
                String pagerUrl = pagerBaseUrl + i;
                parsePager(pagerUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("ll", "size:" + movies.size());
    }

}
