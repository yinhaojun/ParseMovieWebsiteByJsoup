package cn.tek.udo.parsemoviewebsitebyjsoup.view.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tek.udo.parsemoviewebsitebyjsoup.R;
import cn.tek.udo.parsemoviewebsitebyjsoup.bean.MovieDetailInfo;
import cn.tek.udo.parsemoviewebsitebyjsoup.tools.NetWorkUtil;
import cn.tek.udo.parsemoviewebsitebyjsoup.view.view.StatusView;

public class MovieDetailActivity extends AppCompatActivity {

    @Bind(R.id.iv_movie_poster)
    ImageView ivMoviePoster;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_director)
    TextView tvDirector;
    @Bind(R.id.tv_editor)
    TextView tvEditor;
    @Bind(R.id.tv_main_role)
    TextView tvMainRole;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_language)
    TextView tvLanguage;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_intro)
    TextView tvIntro;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private MovieDetailInfo info;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        final String url = bundle.getString("url", "");
        String title = bundle.getString("title", "");

        info = new MovieDetailInfo();
        info.setTitle(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("详情");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData(url);

        snackbar = Snackbar.make(ivMoviePoster, "向上滑动查看详情", Snackbar.LENGTH_LONG).setAction("Get it", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void getData(final String url) {
        if (NetWorkUtil.isNetWorkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    parseMovieDetailInfo(url);
                }
            }).start();
        } else {
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updateViews();
                    break;
            }
        }
    };

    private void updateViews() {
        Glide.with(this).load(info.getPosterUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(ivMoviePoster);
        tvDirector.setText(info.getDirector());
        tvArea.setText(info.getArea());
        tvEditor.setText(info.getEditor());
        tvIntro.setText(info.getIntro());
        tvLanguage.setText(info.getLanguage());
        tvMainRole.setText(info.getMainActors());
        tvName.setText(info.getTitle());
        tvType.setText(info.getType());

    }

    @OnClick(R.id.fab)
    public void onClick() {
    }

    private void parseMovieDetailInfo(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(8000)
                    .post();
            Element element = doc.getElementById("post_content");
            parseDetail(element);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseDetail(Element element) {
        //解析图片
        Elements imgs = element.select("img[src]");
        if (imgs.size() >= 1) {
            String poster = imgs.get(0).attr("src");
            info.setPosterUrl(poster);
        }

        Elements details = element.getElementsByTag("p");
        Log.i("ll", details.toString());


        if (details.size() == 3) {
            String intro = details.get(2).childNode(0).childNode(0).toString();
            info.setIntro(intro);//设置介绍
        }
        List<Node> nodes;
        for (int j = 0; j < details.size(); j++) {
            Element element1 = details.get(j);
            if (element1.toString().contains("类型") ) {
                nodes = details.get(1).childNodes();
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    int size = node.childNodeSize();
                    if (size == 1) {
                        String type = node.childNode(0).toString();
                        if (TextUtils.equals(type, "类型:")) {
                            String temp = nodes.get(i + 1).toString();
                            temp = temp.replaceAll("&nbsp;", "");
                            info.setType(temp);
                        } else if (TextUtils.equals(type, "制片国家/地区:")) {
                            String temp = nodes.get(i + 1).toString();
                            temp = temp.replaceAll("&nbsp;", "");
                            info.setArea(temp);
                        }
                        if (TextUtils.equals(type, "语言:")) {
                            String temp = nodes.get(i + 1).toString();
                            temp = temp.replaceAll("&nbsp;", "");
                            info.setLanguage(temp);
                        } else if (TextUtils.equals(type, "上映日期:")) {
                            String temp = nodes.get(i + 1).toString();
                            temp = temp.replaceAll("&nbsp;", "");
                            info.setDate(temp);
                        }

                    }
                }
            } else {
                Log.i("pls", "adsfadsf");
            }
        }

        Elements pls = element.getElementsByClass("pl");
        Elements attrs = element.getElementsByClass("attrs");
        for (int i = 0; i < pls.size(); i++) {
            Log.i("pls", pls.get(i).data());
            String type = pls.get(i).ownText();
            if (TextUtils.equals(type, "导演")) {
                Element element1 = attrs.get(i);
                Elements directors = element1.select("a[href]");
                String director = "";
                if (directors.size() == 0) {
                    director = attrs.get(i).ownText();
                } else {
                    for (Element e : directors) {
                        director = director + e.text();
                    }
                }
//                String attr = attrs.get(i).ownText();
//                Log.i("attr", attr + hello);
                info.setDirector(director);
            } else if (TextUtils.equals(type, "编剧")) {
                Element element1 = attrs.get(i);
                Elements editors = element1.select("a[href]");
                String editor = "";
                if (editors.size() == 0) {
                    editor = attrs.get(i).ownText();
                } else {
                    for (Element e : editors) {
                        editor = editor + e.text();
                    }
                }
                info.setEditor(editor);
            } else if (TextUtils.equals(type, "主演")) {
                Element element1 = attrs.get(i);
                Elements mains = element1.select("a[href]");
                String main = "";
                if (mains.size() == 0) {
                    main = attrs.get(i).ownText();
                } else {
                    for (Element e : mains) {
                        main = main + e.text();
                    }
                }
                info.setMainActors(main);
            } else {

            }
        }
        Log.i("ll-type", info.toString());
        handler.sendEmptyMessage(1);
    }
}
