package com.anyue.server.mapper;

import com.anyue.common.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

    /**
     * 更新阅读人数+1
     * @param id 书本ID
     */
    @Update("update book set readership = readership + 1 where id = #{id}")
    void incrementReadership(@Param("id") String id);

}
