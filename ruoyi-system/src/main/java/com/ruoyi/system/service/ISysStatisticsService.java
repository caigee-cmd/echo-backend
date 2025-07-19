package com.ruoyi.system.service;

import com.ruoyi.bussiness.domain.vo.SysDataVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统首页数据统计
 *
 * @author ruoyi
 */
@Service
public interface ISysStatisticsService {

    List<SysDataVO> getDataList(String parentId);
}
