package com.wuhulala.mall.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * @author wuhulala<br>
 * @date 2019/10/24<br>
 * @since v1.0<br>
 */
@Table(name = "tb_goods")
@Entity
@Data
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private String type;

    private String address;

}
