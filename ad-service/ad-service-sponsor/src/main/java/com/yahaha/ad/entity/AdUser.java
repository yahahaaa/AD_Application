package com.yahaha.ad.entity;

import com.yahaha.ad.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_user")
@SuppressWarnings("all")
public class AdUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    //标识为数据库字段
    @Basic
    //将java对象属性与数据库字段对应
    @Column(name = "username",nullable = false)
    private String username;

    @Basic
    @Column(name = "token",nullable = false)
    private String token;

    @Basic
    @Column(name = "user_status", nullable = false)
    private Integer userStatus;

    @Basic
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time",nullable = false)
    private Date updateTime;

    public AdUser(String username, String token){
        this.username = username;
        this.token = token;
        this.userStatus = CommonStatus.VALID.getCode();
        this.createTime = new Date();
        this.updateTime = this.createTime;
    }


}
