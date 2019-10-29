package com.wuhulala.mall.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wuhulala<br>
 * @date 2019/10/29<br>
 * @since v1.0<br>
 */
@Data
public class Event implements Serializable {

    private String eventId;

    private Long userId;

    private String userName;

    private String goodsName;

    private Long goodsId;

    private Date date;


}
