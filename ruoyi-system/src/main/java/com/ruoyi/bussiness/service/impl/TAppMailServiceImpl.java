package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TAppMailMapper;
import com.ruoyi.bussiness.domain.TAppMail;
import com.ruoyi.bussiness.service.ITAppMailService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 1v1站内信Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-18
 */
@Service
public class TAppMailServiceImpl extends ServiceImpl<TAppMailMapper,TAppMail> implements ITAppMailService
{
    @Autowired
    private TAppMailMapper tAppMailMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private TAppUserMapper tAppUserMapper;

    /**
     * 查询1v1站内信
     * 
     * @param id 1v1站内信主键
     * @return 1v1站内信
     */
    @Override
    public TAppMail selectTAppMailById(Long id)
    {
        return tAppMailMapper.selectTAppMailById(id);
    }

    /**
     * 查询1v1站内信列表
     * 
     * @param tAppMail 1v1站内信
     * @return 1v1站内信
     */
    @Override
    public List<TAppMail> selectTAppMailList(TAppMail tAppMail)
    {
        return tAppMailMapper.selectTAppMailList(tAppMail);
    }

    /**
     * 新增1v1站内信
     * 
     * @param tAppMail 1v1站内信
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTAppMail(TAppMail tAppMail)
    {
        int i = 0;
        String keyCommon = CachePrefix.COMMONALITY_MAIL.getPrefix();
        tAppMail.setCreateTime(DateUtils.getNowDate());
        tAppMail.setDelFlag("0");
        tAppMail.setStatus(0);
        tAppMail.setOpertorId(SecurityUtils.getUsername());
            if (tAppMail.getType().equals("1")){
                String[] userIds = tAppMail.getUserIds().split(",");
                for (int j = 0; j < userIds.length; j++) {
                    tAppMail.setUserId(Long.parseLong(userIds[j]));
                    i = tAppMailMapper.insertTAppMail(tAppMail);
                    String key = CachePrefix.USER_MAIL.getPrefix() + tAppMail.getUserId();
                    List<TAppMail> list = redisCache.getCacheObject(key);
                    if (CollectionUtils.isEmpty(list)){
                        List<TAppMail> mailList = new ArrayList<>();
                        mailList.add(tAppMail);
                        redisCache.setCacheObject(key,mailList);
                    }else{
                        list.add(tAppMail);
                        redisCache.setCacheObject(key,list);
                    }
                }
            }else{
                i = tAppMailMapper.insertTAppMail(tAppMail);
                List<TAppMail> list = redisCache.getCacheObject(keyCommon);
                if (CollectionUtils.isEmpty(list)){
                    List<TAppMail> mailList = new ArrayList<>();
                    mailList.add(tAppMail);
                    redisCache.setCacheObject(keyCommon,mailList);
                }else{
                    list.add(tAppMail);
                    redisCache.setCacheObject(keyCommon,list);
                }
            }

        return i;
    }

    /**
     * 修改1v1站内信
     * 
     * @param tAppMail 1v1站内信
     * @return 结果
     */
    @Override
    public int updateTAppMail(TAppMail tAppMail)
    {
        tAppMail.setUpdateTime(DateUtils.getNowDate());
        return tAppMailMapper.updateTAppMail(tAppMail);
    }

    /**
     * 批量删除1v1站内信
     * 
     * @param ids 需要删除的1v1站内信主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTAppMailByIds(Long[] ids)
    {
        return tAppMailMapper.deleteTAppMailByIds(ids);
    }

    /**
     * 删除1v1站内信信息
     * 
     * @param id 1v1站内信主键
     * @return 结果
     */
    @Override
    public int deleteTAppMailById(Long id)
    {
        if(!Objects.isNull(id)){
            TAppMail tAppMail = tAppMailMapper.selectTAppMailById(id);
            if (tAppMail.getType().equals("1")){
                if (!Objects.isNull(tAppMail)){
                    String key = CachePrefix.USER_MAIL.getPrefix() + tAppMail.getUserId();
                    List<TAppMail> list = redisCache.getCacheObject(key);
                    List<TAppMail> copyList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(list)){
                        copyList.addAll(list);
                        copyList.stream().forEach(appMail -> {
                            if (appMail.getId().equals(tAppMail.getId())){
                                list.remove(appMail);
                            }
                        });
                    }
                    if (!CollectionUtils.isEmpty(list)){
                        redisCache.setCacheObject(key,list);
                    }else{
                        redisCache.deleteObject(key);
                    }
                }
            }else{
                String keyCommon = CachePrefix.COMMONALITY_MAIL.getPrefix();
                List<TAppMail> list = redisCache.getCacheObject(keyCommon);
                List<TAppMail> copyList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(list)){
                    copyList.addAll(list);
                    copyList.stream().forEach(appMail -> {
                        if (appMail.getId().equals(tAppMail.getId())){
                            list.remove(appMail);
                        }
                    });
                }
                if (!CollectionUtils.isEmpty(list)){
                    redisCache.setCacheObject(keyCommon,list);
                }else{
                    redisCache.deleteObject(keyCommon);
                }
            }

        }
        return tAppMailMapper.deleteTAppMailById(id);
    }

    @Override
    public List<TAppMail> listByUserId(TAppMail tAppMail) {
        String key = CachePrefix.USER_MAIL.getPrefix() + tAppMail.getUserId();
        String keyCommon = CachePrefix.COMMONALITY_MAIL.getPrefix();
        List<TAppMail> list = redisCache.getCacheObject(key);
        List<TAppMail> mailList = redisCache.getCacheObject(keyCommon);
//        if (CollectionUtils.isEmpty(list)){
//            list =tAppMailMapper.selectTAppMailList(tAppMail);
//        }
//        if (CollectionUtils.isEmpty(mailList)){
//            mailList =tAppMailMapper.selectList(new LambdaQueryWrapper<TAppMail>().eq(TAppMail::getType,"2"));
//        }
        list = CollectionUtils.isEmpty(list)?new ArrayList<>():list;
        mailList = CollectionUtils.isEmpty(mailList)?new ArrayList<>():mailList;
        list.addAll(mailList);
        return list;
    }

    @Transactional
    @Override
    public int updateMail(Long[] ids, TAppUser appUser) {
        int j = 0;
        String key = CachePrefix.USER_MAIL.getPrefix() + appUser.getUserId();
        List<TAppMail> list = redisCache.getCacheObject(key);
        list = CollectionUtils.isEmpty(list)?new ArrayList<>():list;
        List<TAppMail> listCopy = new ArrayList<>();
        listCopy.addAll(list);
        if (ids.length>0){
            for (int i = 0; i < ids.length; i++) {
                if (!CollectionUtils.isEmpty(list)){
                    for (TAppMail m:listCopy) {
                        if (m.getId().equals(ids[i])){
                            list.remove(m);
                        }
                    }
                    TAppMail tAppMail = new TAppMail();
                    tAppMail.setId(ids[i]);
                    tAppMail.setStatus(1);
                    j = tAppMailMapper.updateTAppMail(tAppMail);
                }
            }
        }
        if (CollectionUtils.isEmpty(list)){
            redisCache.deleteObject(key);
        }
        return j;
    }
}
