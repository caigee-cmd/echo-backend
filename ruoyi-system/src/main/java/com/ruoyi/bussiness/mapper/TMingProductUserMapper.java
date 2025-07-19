package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TMingProductUser;

/**
 * 用户购买质押限制Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-11
 */
public interface TMingProductUserMapper extends BaseMapper<TMingProductUser>
{
    /**
     * 查询用户购买质押限制
     * 
     * @param id 用户购买质押限制主键
     * @return 用户购买质押限制
     */
    public TMingProductUser selectTMingProductUserById(Long id);

    /**
     * 查询用户购买质押限制列表
     * 
     * @param tMingProductUser 用户购买质押限制
     * @return 用户购买质押限制集合
     */
    public List<TMingProductUser> selectTMingProductUserList(TMingProductUser tMingProductUser);

    /**
     * 新增用户购买质押限制
     * 
     * @param tMingProductUser 用户购买质押限制
     * @return 结果
     */
    public int insertTMingProductUser(TMingProductUser tMingProductUser);

    /**
     * 修改用户购买质押限制
     * 
     * @param tMingProductUser 用户购买质押限制
     * @return 结果
     */
    public int updateTMingProductUser(TMingProductUser tMingProductUser);

    /**
     * 删除用户购买质押限制
     * 
     * @param id 用户购买质押限制主键
     * @return 结果
     */
    public int deleteTMingProductUserById(Long id);

    /**
     * 批量删除用户购买质押限制
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMingProductUserByIds(Long[] ids);
}
