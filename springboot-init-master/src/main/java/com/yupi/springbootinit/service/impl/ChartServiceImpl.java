package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.mapper.ChartMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2025-01-21 11:09:37
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    @Override
    public void createDynamicChartTable(Long chartId, String headerRow) {
        // 将表头按逗号分割成字段
        String[] columns = headerRow.split(",");
        StringBuilder columnsDef = new StringBuilder();
        for (String col : columns) {
            col = col.trim();
            if (StringUtils.isNotBlank(col)) {
                // 用反引号包裹列名，并设置默认类型为 VARCHAR(255)
                columnsDef.append("`").append(col).append("` VARCHAR(255) NULL, ");
            }
        }

        // 移除最后多余的逗号和空格
        if (columnsDef.length() > 0) {
            columnsDef.setLength(columnsDef.length() - 2);
        }
        String dynamicTableName = "chart_" + chartId;
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + dynamicTableName + " (" + columnsDef + ");";
        // 使用 MybatisPlus 提供的 SqlRunner 执行 SQL 语句
        SqlRunner.db().update(createTableSql);
    }
}




