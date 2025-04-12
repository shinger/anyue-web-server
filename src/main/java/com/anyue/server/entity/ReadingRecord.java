package com.anyue.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("reading_record")
public class ReadingRecord {
    private String userId;
    private String bookId;
    private Integer readingStatus;
    private String lastReadCfi;
    private Float readingProgress;
    private Integer readingDuration;
}
