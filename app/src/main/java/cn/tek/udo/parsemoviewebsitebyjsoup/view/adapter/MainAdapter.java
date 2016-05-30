package cn.tek.udo.parsemoviewebsitebyjsoup.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.tek.udo.parsemoviewebsitebyjsoup.R;
import cn.tek.udo.parsemoviewebsitebyjsoup.bean.MovieInfo;
import cn.tek.udo.parsemoviewebsitebyjsoup.tools.NetWorkUtil;
import cn.tek.udo.parsemoviewebsitebyjsoup.view.activity.MovieDetailActivity;

/**
 * Created by yinhaojun on 16/5/26.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private List<MovieInfo> movies;

    public MainAdapter(Context context, List<MovieInfo> movieInfos) {
        this.context = context;
        this.movies = movieInfos;
    }

    public void setMovies(List<MovieInfo> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainAdapter.ViewHolder holder = new MainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie_info, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position) {
        MovieInfo movieInfo = movies.get(position);
        holder.tvMovieName.setText(movieInfo.getTitle());
        Glide.with(context).load(movieInfo.getPosterUrl()).error(R.mipmap.movie_default_bg).placeholder(R.mipmap.movie_default_bg).into(holder.ivMoviePoster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetWorkUtil.isNetWorkAvailable(v.getContext()))
                {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", movies.get(position).getDetailUrl());
                    bundle.putString("title", movies.get(position).getTitle());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else
                {
                    Snackbar.make(holder.itemView, "网络不可用,请检查网络连接", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_movie_poster)
        ImageView ivMoviePoster;
        @Bind(R.id.tv_movie_name)
        TextView tvMovieName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
