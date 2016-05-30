package cn.tek.udo.parsemoviewebsitebyjsoup.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.tek.udo.parsemoviewebsitebyjsoup.R;

public class ReliefActivity extends AppCompatActivity {

    TextView reliefTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relief);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("免责申明");

        reliefTv = (TextView) findViewById(R.id.tv_relief);
        reliefTv.setText("免责申明\n" +
                "\n" +
                "　　本应用是本人利用闲暇时间为了学习jsoup解析网页开发而来，并不是针对某个网站、个人或者集体，任何人不可利用该应用进行商业用途。如果该应用侵犯了您的权益，请与我及时联系，我第一时间进行删除。" +
                "\n" +
                "\n联系方式为:1339266784@qq.com。\n" +
                "\n" +
                "   任何其他的开发者如果对该应用感兴趣或者有好的job推荐也是可以的,很高兴和你们进行交流,和我共同开发,进行后续的升级。\n" +
                "\n" +
                "   如果有某电影的数据未被解析出来,请通过上面的qq进行联系,本人及时进行更新。\n" +
                "\n" +
                "　　访问者在接受本应用服务之前，请务必仔细阅读本条款并同意本声明。\n" +
                "\n" +
                "　　1、不将本应用以及与之相关的网络服务用作非法用途以及非正当用途;\n" +
                "\n" +
                "　　2、不干扰和扰乱本网站以及与之相关的网络服务;\n" +
                "\n" +
                "　　3、遵守与本网站以及与之相关的网络服务的协议、规定、程序和惯例等。\n" +
                "\n" +
                "　　4、应用郑重提醒访问者：请在转载、上载或者下载有关作品时务必尊重该作品的版权、著作权;如果您发现有您未署名的作品，请立即和我们联系，我们会在第一时间给予删除等相关处理。\n"
        );
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReliefActivity.this, MainActivity.class));
            }
        });
    }

}
