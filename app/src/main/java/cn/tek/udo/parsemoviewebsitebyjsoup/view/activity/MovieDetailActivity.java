package cn.tek.udo.parsemoviewebsitebyjsoup.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.thefinestartist.finestwebview.FinestWebView;

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
        collapsingToolbarLayout.setTitle("电影详情");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData(url);

//        Toast.makeText(MovieDetailActivity.this, "向上滑动查看详情", Toast.LENGTH_SHORT).show();

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
        Glide.with(getApplicationContext()).load(info.getPosterUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(ivMoviePoster);
        tvDirector.setText(info.getDirector());
        tvArea.setText(info.getArea());
        tvEditor.setText(info.getEditor());
        tvIntro.setText(info.getIntro());
        tvLanguage.setText(info.getLanguage());
        tvMainRole.setText(info.getMainActors());
        tvName.setText(info.getTitle());
        tvType.setText(info.getType());
        for (int i = 0; i < info.getJumpInfos().size(); i++) {
            Toast.makeText(this, "url: " + info.getJumpInfos().get(i).getJumpUrl() + "\n"
                    + "pwd:" + info.getJumpInfos().get(i).getPwd(), Toast.LENGTH_SHORT).show();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = new String[info.getJumpInfos().size()];
                for (int i = 0; i < info.getJumpInfos().size(); i++) {
                    items[i] = "URL: \t" + info.getJumpInfos().get(i).getJumpUrl() + "\n" + info.getJumpInfos().get(i).getPwd();
                }
                new MaterialDialog.Builder(v.getContext())
                        .theme(Theme.LIGHT)
                        .title("请选择")
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();

//                                //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
//                                Intent intent = new Intent();
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                //设置intent的Action属性
//                                intent.setAction(Intent.ACTION_VIEW);
//                                //获取文件file的MIME类型
////                                String type = getMIMEType(file);
//                                //设置intent的data和Type属性。
//                                intent.setDataAndType(Uri.parse(info.getJumpInfos().get(which).getJumpUrl()), "text/html");
//                                //跳转
//                                startActivity(intent);
                                String agent = "MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
                                new FinestWebView.Builder(MovieDetailActivity.this).webViewUserAgentString(agent).webViewJavaScriptCanOpenWindowsAutomatically(true).webViewJavaScriptEnabled(true).show(info.getJumpInfos().get(which).getJumpUrl());
                            }
                        })
                        .show();

//                new MaterialDialog.Builder(v.getContext())
//                        .title("请选择")
//                        .items(items)
//                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                            @Override
//                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                /**
//                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
//                                 * returning false here won't allow the newly selected radio button to actually be selected.
//                                 **/
//                                return true;
//                            }
//                        })
////                        .positiveText(R.string.choose)
//                        .show();
            }
        });


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
        parsePlayUrl(element);
        Log.i("ll", details.toString());
        Element linkReport = element.getElementById("link-report");

        if (details.size() == 3) {
            if (details.get(2).childNodeSize() > 1) {
                Node node = details.get(2).childNode(0);
                if (node.childNodeSize() > 1) {
                    String intro = node.childNode(0).toString();
                    info.setIntro(intro);//设置介绍
                }
            }
        }

        if (linkReport != null) {
            Elements reports = linkReport.getElementsByTag("span");
            if (reports.size() >= 1) {
                String intro = reports.get(0).ownText();
                info.setIntro(intro);
            }
        }


        List<Node> nodes;
        for (int j = 0; j < details.size(); j++) {
            Element element1 = details.get(j);
            if (element1.toString().contains("类型")) {
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
                        if (TextUtils.equals(e.text(), "")) {
                            continue;
                        } else {
                            director = director + e.text() + "\\";
                        }

                    }
                    director = director.substring(0, director.length() - 1);
                }
                info.setDirector(director);
            } else if (TextUtils.equals(type, "编剧")) {
                Element element1 = attrs.get(i);
                Elements editors = element1.select("a[href]");
                String editor = "";
                if (editors.size() == 0) {
                    editor = attrs.get(i).ownText();
                } else {
                    for (Element e : editors) {
                        if (TextUtils.equals(e.text(), "")) {
                            continue;
                        } else {
                            editor = editor + e.text() + "\\";
                        }
                    }
                    editor = editor.substring(0, editor.length() - 1);
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
                        if (TextUtils.equals(e.text(), "")) {
                            continue;
                        } else {
                            main = main + e.text() + "\\";
                        }
                    }
                    main = main.substring(0, main.length() - 1);
                }
                info.setMainActors(main);
            } else {

            }
        }
        Log.i("ll-type", info.toString());
        handler.sendEmptyMessage(1);
    }


    private void parsePlayUrl(Element element) {
        Elements playInfos = element.getElementsByTag("h2");
        Log.i("pls", "adsfadsf" + playInfos.toString());
        for (int i = 0; i < playInfos.size(); i++) {
            Element e = playInfos.get(i);
            Elements spans = e.select("span");
            for (Element span : spans) {
                Elements elements = span.select("a[href]");
                String url = "";
                for (int j = 0; j < elements.size(); j++) {
                    url = elements.get(j).attr("href");
                    Log.i("ll-type", url);
                }
                String text = span.text();
                String ll = span.ownText();
                MovieDetailInfo.JumpInfo jumpInfo = new MovieDetailInfo.JumpInfo(url, ll);
                info.addJumpInfo(jumpInfo);
            }
        }
    }
}
