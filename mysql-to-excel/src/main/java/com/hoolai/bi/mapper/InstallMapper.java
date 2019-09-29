package com.hoolai.bi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hoolai.bi.entiy.DailyStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Wrapper;

/**
 *
 *
 *@description: 
 *@author: Ksssss(chenlin@hoolai.com)
 *@time: 2019-09-10 17:06
 * 
 */

public interface InstallMapper extends BaseMapper<DailyStats> {

    @Select("select * from daily_stats ${ew.customSqlSegment}")
    DailyStats get(@Param(Constants.WRAPPER) Wrapper wrapper);
}
