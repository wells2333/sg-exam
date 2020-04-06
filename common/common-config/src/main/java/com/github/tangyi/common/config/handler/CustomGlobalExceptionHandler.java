package com.github.tangyi.common.config.handler;

import com.github.tangyi.common.core.constant.ApiMsg;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.JsonMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author tangyi
 * @date 2019/05/25 15:36
 */
@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    /**
     * 处理参数校验异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationBodyException(MethodArgumentNotValidException ex,
                                                          HttpHeaders headers,
                                                          HttpStatus status) {
        // 获取所有异常信息
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ResponseBean<List<String>> responseBean = new ResponseBean<>(errors, ApiMsg.KEY_SERVICE, ApiMsg.ERROR);
        return new ResponseEntity<>(responseBean, headers, status);
    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息 
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ResponseBean<String>> parameterTypeException(HttpMessageConversionException exception) {
        ResponseBean<String> responseBean = new ResponseBean<>(exception.getMessage(), ApiMsg.KEY_PARAM_VALIDATE, ApiMsg.ERROR);
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    /**
     * 处理CommonException
     *
     * @param e e
     * @return ResponseEntity
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ResponseBean<String>> handleCommonException(Exception e) {
        ResponseBean<String> responseBean = new ResponseBean<>(e.getMessage(), ApiMsg.KEY_SERVICE, ApiMsg.ERROR);
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    /**
     * 捕获@Validate校验抛出的异常
     *
     * @param e e
     * @return ResponseEntity
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> validExceptionHandler(BindException e) {
        Exception ex = parseBindingResult(e.getBindingResult());
        ResponseBean<String> responseBean = new ResponseBean<>(ex.getMessage(), ApiMsg.KEY_PARAM_VALIDATE, ApiMsg.ERROR);
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBean<String>> handleException(Exception e) {
        ResponseBean<String> responseBean = new ResponseBean<>(e.getMessage(), ApiMsg.KEY_ERROR, ApiMsg.ERROR);
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    /**
     * 提取Validator产生的异常错误
     *
     * @param bindingResult bindingResult
     * @return Exception
     */
    private Exception parseBindingResult(BindingResult bindingResult) {
        Map<String, String> errorMsgs = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMsgs.put(error.getField(), error.getDefaultMessage());
        }
        if (errorMsgs.isEmpty()) {
            return new CommonException(ApiMsg.KEY_PARAM_VALIDATE + "");
        } else {
            return new CommonException(JsonMapper.toJsonString(errorMsgs));
        }
    }
}