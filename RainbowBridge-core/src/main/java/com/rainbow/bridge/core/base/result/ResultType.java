package com.rainbow.bridge.core.base.result;

public enum ResultType {
    SUCCESS(0, "success", "成功"),
    FAIL(1, "fail", "失败"),
    NO_TOKEN(2, "no token", "未携带token"),
    LOCK(3, "get lock fail", "请求过快"),
    PARAMS_FAIL(4, "", "参数非法"),
    NO_LOGIN(5, "no login", "没有登录"),
    ACCESS_FAIL(6, "no power,access fail", "没有登录"),
    ;

    ResultType(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    private int code;
    private String message;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
