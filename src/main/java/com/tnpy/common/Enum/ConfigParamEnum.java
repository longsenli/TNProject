package com.tnpy.common.Enum;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:34
 */
public class ConfigParamEnum {
    public static  enum BasicParamEnum {
        TBProcessID("1003", 1), GHProcessID("1004", 2);
        // 成员变量
        private String name;
        private int index;
        // 构造方法
        private BasicParamEnum(String name, int index) {
            this.name = name;
            this.index = index;
        }
        // 普通方法
        public static String getName(int index) {
            for (ConfigParamEnum.BasicParamEnum c : ConfigParamEnum.BasicParamEnum.values()) {
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
