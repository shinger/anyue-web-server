package com.anread.user.mapper;

import com.anread.common.entity.UserAnnotation;
import com.anread.common.vo.UserAnnotationListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserAnnotationMapper extends BaseMapper<UserAnnotation> {
    /**
     * 获取用户在书籍目录下的标注
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @return 用户在书籍目录下的标注
     */
     @Select("SELECT toc_parent_index, toc_child_index, JSON_ARRAYAGG(anno_json) AS userAnnotationList " +
             "FROM ( " +
             "SELECT " +
             "toc_parent_index, " +
             "toc_child_index, " +
             "JSON_OBJECT('id', id, 'type', type, 'lineColor', line_color, 'lineContent', line_content, 'ideaContent', idea_content, 'epubCfiRange', epub_cfi_range) AS anno_json " +
             "FROM user_annotation " +
             "WHERE user_id = #{userId} AND book_id = #{bookId} " +
             "ORDER BY toc_parent_index, toc_child_index, epub_cfi_range " +
             ") AS sorted_anno " +
             "GROUP BY toc_parent_index, toc_child_index " +
             "ORDER BY toc_parent_index, toc_child_index ")
    List<UserAnnotationListVO> getUserAnnotationsInToc(@Param("userId") String userId, @Param("bookId") String bookId);
}
