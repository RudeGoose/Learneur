package edu.whu.learneur.dao;

import edu.whu.learneur.domain.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author Learneur
 * @since 2022-12-01
 */
@Mapper
public interface UsersDao extends BaseMapper<Users> {

}
