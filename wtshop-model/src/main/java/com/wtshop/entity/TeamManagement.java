package com.wtshop.entity;

/**
 * 团队管理
 */
public class TeamManagement {
    /*
    头像
     */
    private String   avatar;
    /*
        邀请码
         */
    private String   share_code;
    /*
        总人数
         */
    private String   memeber_num;    /*
       订单数
         */
    private String   order_num;
    /*
        总金额
         */
    private String   price_num;
    /*
        注册时间
         */
    private String   create_date;
    /*
        微信号
         */
    private String   we_chat_number;
    /*
        手机号
         */
    private String   phone;
    /*
       昵称
         */
    private String   nickname;

    public TeamManagement(String avatar, String share_code, String memeber_num, String order_num, String price_num, String create_date, String we_chat_number, String phone, String nickname) {
        this.avatar = avatar;
        this.share_code = share_code;
        this.memeber_num = memeber_num;
        this.order_num = order_num;
        this.price_num = price_num;
        this.create_date = create_date;
        this.we_chat_number = we_chat_number;
        this.phone = phone;
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public String getMemeber_num() {
        return memeber_num;
    }

    public void setMemeber_num(String memeber_num) {
        this.memeber_num = memeber_num;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getPrice_num() {
        return price_num;
    }

    public void setPrice_num(String price_num) {
        this.price_num = price_num;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getWe_chat_number() {
        return we_chat_number;
    }

    public void setWe_chat_number(String we_chat_number) {
        this.we_chat_number = we_chat_number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
