package com.vihackerframework.core.exception;

/**
* @Description
* @Author: Ranger
* @Date: 2021/1/15 14:01
* @Email: wilton.icp@gmail.com
*/
public class ValidateCodeException extends Exception{

    private static final long serialVersionUID = 7514854456967620043L;

    public ValidateCodeException(String message){
        super(message);
    }
}
