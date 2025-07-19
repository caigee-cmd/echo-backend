package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TAppuserLoginLog;
import com.ruoyi.bussiness.mapper.TAppuserLoginLogMapper;
import com.ruoyi.bussiness.service.ITAppuserLoginLogService;
import com.ruoyi.common.utils.ip.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 系统访问记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
@Service
public class TAppuserLoginLogServiceImpl extends ServiceImpl<TAppuserLoginLogMapper,TAppuserLoginLog> implements ITAppuserLoginLogService
{
    @Resource
    private TAppuserLoginLogMapper tAppuserLoginLogMapper;

    /**
     * 查询系统访问记录
     * 
     * @param id 系统访问记录主键
     * @return 系统访问记录
     */
    @Override
    public TAppuserLoginLog selectTAppuserLoginLogById(Long id)
    {
        return tAppuserLoginLogMapper.selectTAppuserLoginLogById(id);
    }

    /**
     * 查询系统访问记录列表
     * 
     * @param tAppuserLoginLog 系统访问记录
     * @return 系统访问记录
     */
    @Override
    public List<TAppuserLoginLog> selectTAppuserLoginLogList(TAppuserLoginLog tAppuserLoginLog)
    {
        return tAppuserLoginLogMapper.selectTAppuserLoginLogList(tAppuserLoginLog);
    }

    /**
     * 新增系统访问记录
     * 
     * @param tAppuserLoginLog 系统访问记录
     * @return 结果
     */
    @Override
    public int insertTAppuserLoginLog(TAppuserLoginLog tAppuserLoginLog)
    {
        return tAppuserLoginLogMapper.insertTAppuserLoginLog(tAppuserLoginLog);
    }

    /**
     * 修改系统访问记录
     * 
     * @param tAppuserLoginLog 系统访问记录
     * @return 结果
     */
    @Override
    public int updateTAppuserLoginLog(TAppuserLoginLog tAppuserLoginLog)
    {
        return tAppuserLoginLogMapper.updateTAppuserLoginLog(tAppuserLoginLog);
    }

    /**
     * 批量删除系统访问记录
     * 
     * @param ids 需要删除的系统访问记录主键
     * @return 结果
     */
    @Override
    public int deleteTAppuserLoginLogByIds(Long[] ids)
    {
        return tAppuserLoginLogMapper.deleteTAppuserLoginLogByIds(ids);
    }

    /**
     * 删除系统访问记录信息
     * 
     * @param id 系统访问记录主键
     * @return 结果
     */
    @Override
    public int deleteTAppuserLoginLogById(Long id)
    {
        return tAppuserLoginLogMapper.deleteTAppuserLoginLogById(id);
    }

    @Override
    public void insertAppActionLog(TAppUser user, String msg, int status, HttpServletRequest request) {
        TAppuserLoginLog log = new TAppuserLoginLog();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        log.setUserId(user.getUserId());
        log.setUsername(user.getAddress());
        String ip = IpUtils.getIpAddr(request);
        log.setIpaddr(ip);
        try {
            log.setLoginLocation(IpUtils.getCityInfo(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.setBrowser(browser);
        log.setOs(os);
        log.setMsg(msg);
        log.setStatus(status);
        log.setLoginTime(new Date());
        tAppuserLoginLogMapper.insert(log);
    }
}
