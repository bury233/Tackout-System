package com.tackout.system.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})//拦截RestController和controller   @RestControllerAdvice
@ResponseBody//返回JSON
@Slf4j
public class AllExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionhandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());

        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            String username = split[2];
            return R.error(username+"已存在");
        }

        return R.error("错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> Customexceptionhandler(CustomException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }
}
