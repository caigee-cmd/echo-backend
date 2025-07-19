package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TAppMail;
import com.ruoyi.bussiness.domain.TAppUser;

/**
 * 1v1站内信Service接口
 * 
 * @author ruoyi
 * @date 2023-07-18
 */
public interface ITAppMailService extends IService<TAppMail>
{
    /**
     * 查询1v1站内信
     * 
     * @param id 1v1站内信主键
     * @return 1v1站内信
     */
    public TAppMail selectTAppMailById(Long id);

    /**
     * 查询1v1站内信列表
     * 
     * @param tAppMail 1v1站内信
     * @return 1v1站内信集合
     */
    public List<TAppMail> selectTAppMailList(TAppMail tAppMail);

    /**
     * 新增1v1站内信
     * 
     * @param tAppMail 1v1站内信
     * @return 结果
     */
    public int insertTAppMail(TAppMail tAppMail);

    /**
     * 修改1v1站内信
     * 
     * @param tAppMail 1v1站内信
     * @return 结果
     */
    public int updateTAppMail(TAppMail tAppMail);

    /**
     * 批量删除1v1站内信
     * 
     * @param ids 需要删除的1v1站内信主键集合
     * @return 结果
     */
    public int deleteTAppMailByIds(Long[] ids);

    /**
     * 删除1v1站内信信息
     * 
     * @param id 1v1站内信主键
     * @return 结果
     */
    public int deleteTAppMailById(Long id);

    List<TAppMail> listByUserId(TAppMail tAppMail);

    int updateMail(Long[] ids, TAppUser appUser);
}
