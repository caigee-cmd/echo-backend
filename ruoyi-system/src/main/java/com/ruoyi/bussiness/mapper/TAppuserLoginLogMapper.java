package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.bussiness.domain.TAppuserLoginLog;

/**
 * 系统访问记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
public interface TAppuserLoginLogMapper extends BaseMapper<TAppuserLoginLog>
{
    /**
     * 查询系统访问记录
     * 
     * @param id 系统访问记录主键
     * @return 系统访问记录
     */
    public TAppuserLoginLog selectTAppuserLoginLogById(Long id);

    /**
     * 查询系统访问记录列表
     * 
     * @param tAppuserLoginLog 系统访问记录
     * @return 系统访问记录集合
     */
    public List<TAppuserLoginLog> selectTAppuserLoginLogList(TAppuserLoginLog tAppuserLoginLog);

    /**
     * 新增系统访问记录
     * 
     * @param tAppuserLoginLog 系统访问记录
     * @return 结果
     */
    public int insertTAppuserLoginLog(TAppuserLoginLog tAppuserLoginLog);

    /**
     * 修改系统访问记录
     * 
     * @param tAppuserLoginLog 系统访问记录
     * @return 结果
     */
    public int updateTAppuserLoginLog(TAppuserLoginLog tAppuserLoginLog);

    /**
     * 删除系统访问记录
     * 
     * @param id 系统访问记录主键
     * @return 结果
     */
    public int deleteTAppuserLoginLogById(Long id);

    /**
     * 批量删除系统访问记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppuserLoginLogByIds(Long[] ids);
}
