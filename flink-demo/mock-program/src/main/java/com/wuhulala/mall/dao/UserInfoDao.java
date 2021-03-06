package com.wuhulala.mall.dao;

import com.wuhulala.mall.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wuhulala<br>
 * @date 2019/10/23<br>
 * @since v1.0<br>
 */
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {
}
