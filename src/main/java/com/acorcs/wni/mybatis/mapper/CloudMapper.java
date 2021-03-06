package com.acorcs.wni.mybatis.mapper;

import com.acorcs.wni.entity.Cloud;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by 邓超 on 2016/12/9.
 */
@CacheConfig(cacheNames = "wni_cloud")
public interface CloudMapper extends WniEntityMapper<Cloud> {
    @Insert("INSERT INTO wni_cloud" +
            " (header, notice_id,cloud_distribution, cloud_type,altitudes, airframe_icing,extended_degree, geographic)" +
            "VALUES (#{header}, #{noticeId},#{cloudDistribution}, #{cloudType},#{altitudes}, " +
            "#{airframeIcing},#{extendedDegree}, GeomFromText(#{geographic},4326))")
    @SelectKey(keyProperty = "id",resultType = Long.class,before = false,statement = "SELECT LAST_INSERT_ID() AS id")
    Long save(Cloud cloud);


    @Cacheable(key = "#noticeId")
    @Select("SELECT id,notice_id,header,cloud_distribution,cloud_type,altitudes,airframe_icing,extended_degree,AsBinary(geographic) as geographic FROM wni_cloud WHERE notice_id=#{noticeId}")
    @Results({
        @Result(property = "notice",column = "notice_id",one = @One(select = "com.acorcs.wni.mybatis.mapper.NoticeMapper.getNotice")),
        @Result(property = "noticeId",column = "notice_id")
    })
    List<Cloud> findByNoticeId(Long noticeId);
}
