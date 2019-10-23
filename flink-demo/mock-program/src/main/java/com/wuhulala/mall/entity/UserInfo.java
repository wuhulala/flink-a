package com.wuhulala.mall.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * @author wuhulala<br>
 * @date 2019/10/23<br>
 * @since v1.0<br>
 */
@Table(name = "tb_user")
@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer birth;

    private String sex;

    private String address;

    private String email;
}
