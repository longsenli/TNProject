package com.tnpy.common.Enum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:34
 */
public class ConfigParamEnum {
    public  static String serviceIP = "192.168.80.228;10.0.0.151";
    public  static String basicEquipParamDB = "tb_equipmentparamrecord";
    public  static Map  DryFilnCapacityMap = new HashMap<String,Integer>() {{
        put("7d57a44907154c5289ee46344de0ce41",94);
        put("fa3a57559107432599d0252b2bf67fcf",94);
        put("10011-1002-001", 81);
        put("10011-1002-002", 81);
        put("10011-1002-003", 40);
        put("10011-1002-004", 40);
        put("1001-2BGH-F1", 48);
        put("1002-2BGH-F2", 48);
        put("1003-2BGH-F3", 48);
        put("1004-2BGH-F4", 48);
        put("1005-2BGH-F5", 48);
        put("1006-2BGH-F6", 48);
        put("1007-2BGH-Z7", 72);
        put("1008-2BGH-Z8", 72);
        put("1009-2BGH-Z9", 72);
        put("1010-2BGH-Z10", 72);
        put("1011-2BGH-Z11", 72);
        put("1012-2BGH-Z12", 72);
        put("1013-2BGH-Z13", 72);
        put("1014-2BGH-Z14", 72);
        put("1003-1004-1001", 800);
        put("1003-1004-1002", 800);
        put("1003-1004-1003", 800);
        put("1003-1004-1004", 800);
        put("1003-1004-1005", 800);
        put("1003-1004-1006", 800);
        put("1003-1004-1007", 800);
        put("1003-1004-1008", 800);
        put("1003-1004-1009", 800);
    }};

    public  static Map  PlateWeighDBMap = new HashMap<String,String>() {{
        put("1001","tb_plateweighrecord_1b");
       // put("1002","tb_plateweighrecord_2b");
        put("1003","tb_plateweighrecord_3b");

    }};
    public static  enum BasicProcessEnum {
        TBProcessID("1003", 1), GHProcessID("1004", 2),CDProcessID("1009",3),JSProcessID("1008",4),
        ZHProcessID("1007",5), JZProcessID("1011", 6), ZLProcessID("1012", 7);
        // 成员变量
        private String name;
        private int index;
        // 构造方法
        private BasicProcessEnum(String name, int index) {
            this.name = name;
            this.index = index;
        }
        // 普通方法
        public static String getName(int index) {
            for (ConfigParamEnum.BasicProcessEnum c : ConfigParamEnum.BasicProcessEnum.values()) {
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

    public static  enum EquipmentTypeEnum {
        HCSC("3", "tb_equipmentparamrecord"), SZGZY("4", "tb_equipmentparamrecord"), ZNDB("10012", "tb_equipmentparamrecord_10012");
        // 成员变量
        private String typeID;
        private String tableName;
        // 构造方法
        private EquipmentTypeEnum(String typeID, String tableName) {
            this.typeID = typeID;
            this.tableName = tableName;
        }
        // 普通方法
        public static String getName(String typeID) {
            for (ConfigParamEnum.EquipmentTypeEnum c : ConfigParamEnum.EquipmentTypeEnum.values()) {
                if (c.getTypeID().equals(typeID) ) {
                    return c.tableName;
                }
            }
            return null;
        }
        // get set 方法
        public String getTypeID() {
            return typeID;
        }
        public void setTypeID(String typeID) {
            this.typeID = typeID;
        }
        public String getTableName() {
            return tableName;
        }
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }
    
    public static  enum EquipmentCapacity {
    	DRYKILNZY("3bjzZY",90),DRYKILNFY("3bjzFY",90);
        // 成员变量
        private String name;
        private Integer num;
        // 构造方法
        private EquipmentCapacity(String name, Integer num) {
            this.name = name;
            this.num = num;
        }
        // 普通方法
        public static String getName(String typeID) {
            for (ConfigParamEnum.EquipmentTypeEnum c : ConfigParamEnum.EquipmentTypeEnum.values()) {
                if (c.getTypeID().equals(typeID) ) {
                    return c.tableName;
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
        public Integer getNum() {
            return num;
        }
        public void setNum(Integer num) {
            this.num = num;
        }
    }
}
