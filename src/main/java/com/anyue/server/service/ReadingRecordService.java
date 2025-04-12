package com.anyue.server.service;

import com.anyue.common.dto.Result;
import com.anyue.server.dto.ReadingRecordDto;
import com.anyue.server.entity.ReadingRecord;

public interface ReadingRecordService {

    /**
     * 上传阅读记录
     * @param readingRecordDto
     * @return
     */
    Result uploadRecord(ReadingRecordDto readingRecordDto);

    /**
     * 获取书本的阅读记录
     * @param bookId
     * @return
     */
    Result getRecords(String bookId);
}
