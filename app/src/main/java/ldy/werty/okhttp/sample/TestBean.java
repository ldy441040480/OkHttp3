package ldy.werty.okhttp.sample;

import java.util.List;

/**
 * Created by lidongyang on 2016/5/18.
 */
public class TestBean {

    /**
     * stat : 1
     * msg : succ
     * data : [{"pic_id":"4","picture":"/images/login_pic/d.jpg"},{"pic_id":"3","picture":"/images/login_pic/c.jpg"},{"pic_id":"2","picture":"/images/login_pic/b.jpg"},{"pic_id":"1","picture":"/images/login_pic/a.jpg"}]
     */

    private int stat;
    private String msg;
    /**
     * pic_id : 4
     * picture : /images/login_pic/d.jpg
     */

    private List<DataBean> data;

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String pic_id;
        private String picture;

        public String getPic_id() {
            return pic_id;
        }

        public void setPic_id(String pic_id) {
            this.pic_id = pic_id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}

