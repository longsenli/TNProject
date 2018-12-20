package com.tnpy.common.Enum;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/19 14:41
 */
public class StatusEnum {
    public static  enum ResponseStatus {
        Success("成功", 1), Fail("失败", 2);
        // 成员变量
        private String name;
        private int index;
        // 构造方法
        private ResponseStatus(String name, int index) {
            this.name = name;
            this.index = index;
        }
        // 普通方法
        public static String getName(int index) {
            for (ResponseStatus c : ResponseStatus.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }
        // get set 方法
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
    }
}
