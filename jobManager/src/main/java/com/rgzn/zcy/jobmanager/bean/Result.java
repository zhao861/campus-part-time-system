package com.rgzn.zcy.jobmanager.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private int code;
    private String message;
    private Object data;


    public static Result success(Object objects) {
        return new Result(200, "success", objects);
    }

    public static Result failure(Object objects) {
        return new Result(0, "failure", objects);
    }
    public static Result success() {
        return new Result(200, "success", null);
    }

    public static Result failure() {
        return new Result(0, "failure", null);
    }


}
