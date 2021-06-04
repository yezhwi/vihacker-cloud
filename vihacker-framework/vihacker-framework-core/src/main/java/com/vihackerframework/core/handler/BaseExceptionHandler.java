package com.vihackerframework.core.handler;

import com.vihackerframework.core.api.ResultCode;
import com.vihackerframework.core.api.ViHackerResult;
import com.vihackerframework.core.exception.ValidateCodeException;
import com.vihackerframework.core.exception.ViHackerAuthException;
import com.vihackerframework.core.exception.ViHackerException;
import com.vihackerframework.core.exception.ViHackerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.auth.message.AuthException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

/**
* 全局统用异常处理器
* 所谓的全局异常处理指的是全局处理Controller层抛出来的异常。因为全局异常处理器在各个服务系统里都能用到
* 对于通用的异常类型捕获可以在BaseExceptionHandler中定义
* @Description
* @Author: Ranger
* @Date: 2021/1/15 13:57
* @Email: wilton.icp@gmail.com
*/
@Slf4j
public class BaseExceptionHandler {


    @ExceptionHandler(value = ViHackerException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult handleBaseException(ViHackerException e) {
        log.error("系统异常", e);
        return ViHackerResult.failed(e.getMessage());
    }

    /**
     * 处理自定义的业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ViHackerRuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult bizExceptionHandler(ViHackerRuntimeException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ViHackerResult.failed(e.getMessage());
    }

    /**
     * 处理空指针的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult exceptionHandler(NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return ViHackerResult.failed(ResultCode.BODY_NOT_MATCH);
    }

    /**
     * 校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + "!";
        }
        log.error("参数异常:",e);
        return ViHackerResult.failed(errorMesssage);
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return ViHackerResult
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult handleBindException(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return ViHackerResult.failed(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return ViHackerResult
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return ViHackerResult.failed(message.toString());
    }

    @ExceptionHandler(value = ViHackerAuthException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult handleWiltonException(ViHackerException e) {
        log.error("系统错误", e);
        return ViHackerResult.failed(e.getMessage());
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    public ViHackerResult handleException(Exception e) {
//        log.error("系统内部异常，异常信息", e);
//        return ViHackerResult.failed("系统内部异常");
//    }

    @ExceptionHandler(value = ValidateCodeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult handleValidateCodeException(ValidateCodeException e) {
        log.error("系统错误", e);
        return ViHackerResult.failed(e.getMessage());
    }



    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ViHackerResult handleAccessDeniedException(){
        return ViHackerResult.failed(ResultCode.FORBIDDEN);
    }
}
