package com.wuhulala.mall.dao;

import com.wuhulala.mall.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wuhulala<br>
 * @date 2019/10/24<br>
 * @since v1.0<br>
 */
public interface GoodsDao extends JpaRepository<Goods, Long> {


}
