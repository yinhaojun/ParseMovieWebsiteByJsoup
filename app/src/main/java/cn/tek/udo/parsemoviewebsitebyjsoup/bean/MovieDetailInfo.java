package cn.tek.udo.parsemoviewebsitebyjsoup.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinhaojun on 16/5/27.
 */
public class MovieDetailInfo {

    private String posterUrl;
    private String title;
    private String director;
    private String editor;
    private String mainActors;
    private String type;
    private String area;
    private String language;
    private String date;
    private String intro;
    private List<JumpInfo> jumpInfos = new ArrayList<>();

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getMainActors() {
        return mainActors;
    }

    public void setMainActors(String mainActors) {
        this.mainActors = mainActors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void addJumpInfo(JumpInfo jumpInfo) {
        this.jumpInfos.add(jumpInfo);
    }

    public List<JumpInfo> getJumpInfos() {
        return jumpInfos;
    }

    public static class JumpInfo {
        private String jumpUrl;
        private String pwd;

        public JumpInfo(String jumpUrl, String pwd) {
            this.jumpUrl = jumpUrl;
            this.pwd = pwd;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }


}
