package com.hopeofseed.hopeofseed.Data;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/20 9:58
 * 修改人：whisper
 * 修改时间：2016/10/20 9:58
 * 修改备注：
 */
public class SearchBean {
    private int iconId;
    private String title;
    private String content;
    private String comments;

    public SearchBean(int iconId, String title, String content, String comments) {
        this.iconId = iconId;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
