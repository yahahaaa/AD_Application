package com.yahaha.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitRequest {

    private Long planId;
    private String unitName;

    private Integer positionType;
    private Long budget;

    /**
     * 只做非空简单的校验，实际上还应该校验具体值范围
     * @return
     */
    public boolean createValidate(){
        return null != planId && !StringUtils.isEmpty(unitName)
                && positionType != null && budget != null;
    }
}
