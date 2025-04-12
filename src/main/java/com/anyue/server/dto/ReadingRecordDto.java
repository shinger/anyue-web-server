package com.anyue.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadingRecordDto {
    private String bookId;
    private String lastReadCfi;
    private Float readingProgress;
    private Integer readingDuration;
}
