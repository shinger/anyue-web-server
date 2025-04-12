package com.anyue.server.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadingRecordVO {
    private Integer readingStatus;
    private String lastReadCfi;
    private Float readingProgress;
    private Integer readingDuration;
}
