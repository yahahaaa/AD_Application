package com.yahaha.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 用来接口前端用户填写的user属性，接收POST请求实体对象
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    //只需要用户填写user，token和status都不需要用户填写
    private String username;

    public boolean validate(){
        return !StringUtils.isEmpty(username);
    }

}
