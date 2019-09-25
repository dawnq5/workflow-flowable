package com.migu.se.core.workflow.constant;

/**
 * 流程状态
 */
public enum ProcessStatusEnum {

    //0:待处理，1：待处理 2:定时器取消

    UNDONE(0,"流程尚未结束"),DONE(1,"流程正常结束"),CANCEL(2,"流程被取消");
    private Integer key;
    private String desc;

    private ProcessStatusEnum(Integer key, String desc){
        this.key = key;
        this.desc = desc;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
