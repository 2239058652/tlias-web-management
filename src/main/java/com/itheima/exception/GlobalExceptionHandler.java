package com.itheima.exception;


import com.itheima.pojo.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统异常，请稍后再试");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据重复", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String substring = message.substring(i);
        String[] arr = substring.split(" ");
        return Result.error(arr[2] + "已存在");
    }

    @ExceptionHandler(BindException.class)
    public Result handleEmpValidateException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "参数校验失败";
        return Result.error(message);
    }
}
