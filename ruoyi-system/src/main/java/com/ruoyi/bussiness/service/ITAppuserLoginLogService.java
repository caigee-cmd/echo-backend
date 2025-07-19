package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TAppuserLoginLog;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统访问记录Service接口
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
public interface ITAppuserLoginLogService extends IService<TAppuserLoginLog>
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
     * 批量删除系统访问记录
     * 
     * @param ids 需要删除的系统访问记录主键集合
     * @return 结果
     */
    public int deleteTAppuserLoginLogByIds(Long[] ids);

    /**
     * 删除系统访问记录信息
     * 
     * @param id 系统访问记录主键
     * @return 结果
     */
    public int deleteTAppuserLoginLogById(Long id);

    void insertAppActionLog(TAppUser one, String 退出登录成功, int i, HttpServletRequest request);
}
