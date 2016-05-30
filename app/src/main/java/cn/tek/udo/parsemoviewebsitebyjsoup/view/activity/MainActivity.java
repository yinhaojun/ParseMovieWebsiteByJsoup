package cn.tek.udo.parsemoviewebsitebyjsoup.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.tek.udo.parsemoviewebsitebyjsoup.R;
import cn.tek.udo.parsemoviewebsitebyjsoup.bean.MovieInfo;
import cn.tek.udo.parsemoviewebsitebyjsoup.tools.NetWorkUtil;
import cn.tek.udo.parsemoviewebsitebyjsoup.view.adapter.MainAdapter;

public class MainActivity extends AppCompatActivity {

    int pager = 1;
    private List<MovieInfo> movies;
    private long pagerAccount;
    private String baseUrl;
    private MainAdapter mainAdapter;
    private final static int MSG_PARSE_WEBSITE = 1;
    private final static int MSG_PARSE_ERROR = 2;
    private final static int MSG_REFRESH = 3;
    private final static int MSG_LOADMORE = 4;

    @Bind(R.id.recyclerview)
    XRecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainAdapter = new MainAdapter(this, movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(mainAdapter);

        Toast.makeText(MainActivity.this, "正在获取数据,请稍后...", Toast.LENGTH_SHORT).show();

        refresh();
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtil.isNetWorkAvailable(MainActivity.this)) {
                    refresh();
                } else {
                    Toast.makeText(MainActivity.this, "网络异常,请检查网络链接", Toast.LENGTH_SHORT).show();
                    recyclerview.refreshComplete();
                }

            }

            @Override
            public void onLoadMore() {
                if (NetWorkUtil.isNetWorkAvailable(MainActivity.this)) {
                    loadMore();
                } else {
                    Toast.makeText(MainActivity.this, "网络异常,请检查网络链接", Toast.LENGTH_SHORT).show();
                    recyclerview.loadMoreComplete();
                }
            }
        });

    }

    private void refresh() {
        if (NetWorkUtil.isNetWorkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    pager = 1;
                    parseWebsite();
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "网络异常,请检查网络链接", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMore() {
        if (NetWorkUtil.isNetWorkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    parsePager(pager + 1, MSG_LOADMORE);
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "网络异常,请检查网络链接", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PARSE_WEBSITE:
                    pager = 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            parsePager(pager, MSG_REFRESH);
                        }
                    }).start();
                    break;
                case MSG_REFRESH:
                    movies = (List<MovieInfo>) msg.obj;
                    mainAdapter.setMovies(movies);
                    pager = 1;
                    recyclerview.refreshComplete();
                    break;
                case MSG_LOADMORE:
                    movies.addAll((List<MovieInfo>) msg.obj);
                    pager++;
                    recyclerview.loadMoreComplete();
                    break;
                case MSG_PARSE_ERROR:
                    recyclerview.refreshComplete();
                    recyclerview.loadMoreComplete();
                    Toast.makeText(MainActivity.this, "出错啦,刷新试试", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void parsePager(int pager, int type) {
        List<MovieInfo> movies = new ArrayList<>();
        Document doc = null;
        try {
            String url = baseUrl + pager;
            doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(8000)
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
            Message msg = new Message();
            msg.what = type;
            msg.obj = movies;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MSG_PARSE_ERROR);

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
            pagerAccount = Long.valueOf(finalUrl.substring(finalUrl.lastIndexOf("/") + 1));
            baseUrl = finalUrl.substring(0, finalUrl.lastIndexOf("/") + 1);
            handler.sendEmptyMessage(MSG_PARSE_WEBSITE);
//            for (int i = 1; i <= pageLength; i++) {
//                String pagerUrl = pagerBaseUrl + i;
//                parsePager(pagerUrl);
//            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MSG_PARSE_ERROR);

        }
    }


}
