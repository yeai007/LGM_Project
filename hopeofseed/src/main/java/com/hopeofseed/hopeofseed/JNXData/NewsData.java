package com.hopeofseed.hopeofseed.JNXData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/5 15:22
 * 修改人：whisper
 * 修改时间：2016/8/5 15:22
 * 修改备注：
 */
public class NewsData extends RealmObject {

    /**
     * user_id : 1117
     * user_name : 15628801370
     * nickname : 用户01370
     * user_mobile :
     * user_email :
     * createtime : 2016-11-16 05:27:34
     * user_permation : 0
     * user_role : 0
     * user_field :
     * user_role_id : 0
     * fllowed_count : 0
     * been_fllowed_count : 0
     * id : 0000000153
     * title : 经历过咯
     * content : 理解理解
     * keyword : KEYWORD
     * marks : MARKS
     * newcreatetime : 2016-11-16 08:18:52
     * editid : 0
     * flag : 0
     * reviewid : 0
     * related_person : 0
     * createuser : 1117
     * assimgurl :
     * zambia_count : 0000000001
     * zambia_ids : 1
     * newclass : 3
     * newclassname : 农技经验
     * infoid : 50
     * fromid : 0
     * forwardCount : 0
     * commentCount : 1
     */

    private String user_id;
    private String user_name;
    private String nickname;
    private String user_mobile;
    private String user_email;
    private String createtime;
    private String user_permation;
    private String user_role;
    private String user_field;
    private String user_role_id;
    private String fllowed_count;
    private String been_fllowed_count;
    @PrimaryKey
    private int id;
    private String title;
    private String content;
    private String keyword;
    private String marks;
    private String newcreatetime;
    private String editid;
    private String flag;
    private String reviewid;
    private String related_person;
    private String createuser;
    private String assimgurl;
    private String newclass;
    private String newclassname;
    private String infoid;
    private String fromid;
    private String forwardCount;
    private String commentCount;

    /**
     * ForwardComment : 哦哦哦哦哦哦哦哦哦哦
     */

    private String ForwardComment;
    /**
     * zambiaCount : 1
     */

    private int zambiaCount;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUser_permation() {
        return user_permation;
    }

    public void setUser_permation(String user_permation) {
        this.user_permation = user_permation;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_field() {
        return user_field;
    }

    public void setUser_field(String user_field) {
        this.user_field = user_field;
    }

    public String getUser_role_id() {
        return user_role_id;
    }

    public void setUser_role_id(String user_role_id) {
        this.user_role_id = user_role_id;
    }

    public String getFllowed_count() {
        return fllowed_count;
    }

    public void setFllowed_count(String fllowed_count) {
        this.fllowed_count = fllowed_count;
    }

    public String getBeen_fllowed_count() {
        return been_fllowed_count;
    }

    public void setBeen_fllowed_count(String been_fllowed_count) {
        this.been_fllowed_count = been_fllowed_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getNewcreatetime() {
        return newcreatetime;
    }

    public void setNewcreatetime(String newcreatetime) {
        this.newcreatetime = newcreatetime;
    }

    public String getEditid() {
        return editid;
    }

    public void setEditid(String editid) {
        this.editid = editid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    public String getRelated_person() {
        return related_person;
    }

    public void setRelated_person(String related_person) {
        this.related_person = related_person;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getAssimgurl() {
        return assimgurl;
    }

    public void setAssimgurl(String assimgurl) {
        this.assimgurl = assimgurl;
    }


    public String getNewclass() {
        return newclass;
    }

    public void setNewclass(String newclass) {
        this.newclass = newclass;
    }

    public String getNewclassname() {
        return newclassname;
    }

    public void setNewclassname(String newclassname) {
        this.newclassname = newclassname;
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(String forwardCount) {
        this.forwardCount = forwardCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getForwardComment() {
        return ForwardComment;
    }

    public void setForwardComment(String ForwardComment) {
        this.ForwardComment = ForwardComment;
    }

    public int getZambiaCount() {
        return zambiaCount;
    }

    public void setZambiaCount(int zambiaCount) {
        this.zambiaCount = zambiaCount;
    }
}
