package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 江睿达
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2025-01-21 11:09:37
*/
public interface ChartService extends IService<Chart> {

    /**
     * 根据图表 ID 动态创建图表数据表，表名规则：chart_{chartId}
     *
     * @param chartId 图表ID
     */
    void createDynamicChartTable(Long chartId, String headerRow);
}
