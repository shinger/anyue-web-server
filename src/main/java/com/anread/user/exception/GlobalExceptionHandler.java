package com.anread.user.exception;

import com.anread.common.dto.Result;
import com.anread.common.enums.StateEnum;
import com.anread.common.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;

/**
 * @description: 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("业务异常：", e.getErrorMsg());
        return Result.error(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("空指针异常：",e);
        return Result.error(StateEnum.BUSSINESS_ERROR);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常：",e);
        return Result.error(StateEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public Result handleIOException(HttpServletRequest req, IOException ex) {
        log.error("IO异常：", ex);
        return Result.error(StateEnum.INTERNAL_SERVER_ERROR);
    }
}
