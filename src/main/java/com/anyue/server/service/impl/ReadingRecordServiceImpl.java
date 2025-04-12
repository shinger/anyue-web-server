package com.anyue.server.service.impl;

import com.anyue.common.dto.Result;
import com.anyue.server.dto.ReadingRecordDto;
import com.anyue.server.entity.ReadingRecord;
import com.anyue.server.mapper.ReadingRecordMapper;
import com.anyue.server.service.ReadingRecordService;
import com.anyue.server.utils.ThreadLocalUtil;
import com.anyue.server.vo.ReadingRecordVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadingRecordServiceImpl implements ReadingRecordService {

    @Autowired
    private ReadingRecordMapper readingRecordMapper;

    @Override
    public Result uploadRecord(ReadingRecordDto readingRecordDto) {
        String userId = ThreadLocalUtil.getCurrentUser();
        // 查找是否第一次阅读
        ReadingRecord readingRecord = readingRecordMapper.selectOne(new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, readingRecordDto.getBookId()));

        ReadingRecord readingRecordNew = new ReadingRecord();
        BeanUtils.copyProperties(readingRecordDto, readingRecordNew);
        readingRecordNew.setReadingProgress(Float.valueOf(readingRecordDto.getReadingProgress()));
        readingRecordNew.setUserId(userId);

        if (readingRecord == null) {
            // 第一次阅读，直接插入
            readingRecordMapper.insert(readingRecordNew);
            return Result.success();
        }

        // 第二次阅读
        // 计算新时长
        float newDuration = readingRecordDto.getReadingDuration().floatValue()
                + readingRecord.getReadingDuration().floatValue();

        // 时长 > 20 分钟则更新阅读状态为在读
        if (newDuration > 20) {
            readingRecordNew.setReadingStatus(1);
        } else {
            // 否则设置为不标记
            readingRecordNew.setReadingStatus(0);
        }

        // 更新
        readingRecordMapper.update(readingRecordNew, new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, readingRecordDto.getBookId()));

        return Result.success();
    }

    @Override
    public Result getRecords(String bookId) {
        String userId = ThreadLocalUtil.getCurrentUser();
        ReadingRecord readingRecord = readingRecordMapper.selectOne(new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, bookId));

        // 如果没有记录，直接返回0
        if (readingRecord == null) {
            return Result.success().data(0);
        }
        ReadingRecordVO readingRecordVO = new ReadingRecordVO();
        BeanUtils.copyProperties(readingRecord, readingRecordVO);
        return Result.success().data(readingRecordVO);
    }
}
