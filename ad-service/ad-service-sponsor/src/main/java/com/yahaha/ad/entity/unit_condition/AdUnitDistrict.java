package com.yahaha.ad.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 推广单元的省份城市限制维度
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ad_unit_district")
@SuppressWarnings("all")
public class AdUnitDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Basic
    @Column(name = "province", nullable = false)
    private String province;

    @Basic
    @Column(name = "city", nullable = false)
    private String city;

    public AdUnitDistrict(Long unitId, String province, String city) {
        this.unitId = unitId;
        this.province = province;
        this.city = city;
    }
}
