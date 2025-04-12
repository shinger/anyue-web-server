package com.anyue.server.controller;

import com.anyue.common.dto.Result;
import com.anyue.server.dto.ReadingRecordDto;
import com.anyue.server.entity.ReadingRecord;
import com.anyue.server.service.ReadingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reading/record")
public class ReadingRecordController {

    @Autowired
    private ReadingRecordService readingRecordService;

    /**
     * 上传阅读记录
     * @param readingRecordDto
     * @return
     */
    @PostMapping("/upload")
    private Result uploadRecords(@RequestBody ReadingRecordDto readingRecordDto) {
        return readingRecordService.uploadRecord(readingRecordDto);
    }

    @GetMapping("/{bookId}")
    private Result getRecords(@PathVariable("bookId") String bookId) {
        return readingRecordService.getRecords(bookId);
    }

}
