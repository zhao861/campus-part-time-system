package com.rgzn.zcy.jobmanager.handler;

import com.rgzn.zcy.jobmanager.bean.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @RequestBody 参数校验失败异常
     * 使用 @Valid 时触发的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return buildValidationErrorResult(bindingResult);
    }

    /**
     * 处理 @ModelAttribute 参数校验失败异常
     * 使用 @Validated 时触发的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBindException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return buildValidationErrorResult(bindingResult);
    }

    /**
     * 处理 @RequestParam 和 @PathVariable 参数校验失败异常
     * 需要在类上使用 @Validated 注解
     */
    @ExceptionHandler(ConstraintViolationException.class)  // 直接使用类名
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations().stream()
                .map(violation ->
                        violation.getPropertyPath().toString() + ": " + violation.getMessage()
                )
                .collect(Collectors.joining("; "));

        return new Result(0, errorMsg, null);
    }

    /**
     * 构建校验错误信息
     */
    private Result buildValidationErrorResult(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder("参数校验失败: ");
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);
            errorMsg.append(fieldError.getField())
                    .append(" - ")
                    .append(fieldError.getDefaultMessage());

            if (i < fieldErrors.size() - 1) {
                errorMsg.append("; ");
            }
        }

        // 如果没有字段错误，显示全局错误
        if (fieldErrors.isEmpty() && bindingResult.hasErrors()) {
            errorMsg.append(bindingResult.getGlobalErrors().get(0).getDefaultMessage());
        }

        return new Result(0, errorMsg.toString(), null);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception ex) {
        ex.printStackTrace();
        return new Result(500, "服务器内部错误: " + ex.getMessage(), null);
    }
}