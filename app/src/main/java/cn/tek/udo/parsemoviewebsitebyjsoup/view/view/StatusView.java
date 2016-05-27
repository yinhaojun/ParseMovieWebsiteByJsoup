package cn.tek.udo.parsemoviewebsitebyjsoup.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.tek.udo.parsemoviewebsitebyjsoup.R;

/**
 * Created by yinhaojun on 16/5/27.
 */
public class StatusView extends RelativeLayout {
    @Bind(R.id.iv_status)
    ImageView ivStatus;
    @Bind(R.id.tv_status)
    TextView tvStatus;

    public StatusView(Context context) {
        super(context);
        initViews();
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_status, this);
        ButterKnife.bind(this, view);
    }

    public void setImageResource(int resId) {
        ivStatus.setImageResource(resId);
    }

    public void setText(String text) {
        tvStatus.setText(text);
    }

    public void setText(int textId) {
        tvStatus.setText(getResources().getString(textId));
    }

    public void setStatus(int resId, String text) {
        setImageResource(resId);
        setText(text);
    }

    public void setStatus(int resId, int text) {
        setImageResource(resId);
        setText(text);
    }
}
