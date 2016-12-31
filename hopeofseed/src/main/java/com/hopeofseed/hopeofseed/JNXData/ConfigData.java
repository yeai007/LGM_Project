package com.hopeofseed.hopeofseed.JNXData;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/31 10:35
 * 修改人：whisper
 * 修改时间：2016/12/31 10:35
 * 修改备注：
 */
public class ConfigData {

    /**
     * Id : 0000000001
     * ConfigName : AppArea
     * ConfigValue : 地址数据库
     * ConfigVersionCode : 1.0
     * ConfigVersion : 1
     * ConfigFlag : 1
     */

    private String Id;
    private String ConfigName;
    private String ConfigValue;
    private String ConfigVersionCode;
    private String ConfigVersion;
    private String ConfigFlag;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getConfigName() {
        return ConfigName;
    }

    public void setConfigName(String ConfigName) {
        this.ConfigName = ConfigName;
    }

    public String getConfigValue() {
        return ConfigValue;
    }

    public void setConfigValue(String ConfigValue) {
        this.ConfigValue = ConfigValue;
    }

    public String getConfigVersionCode() {
        return ConfigVersionCode;
    }

    public void setConfigVersionCode(String ConfigVersionCode) {
        this.ConfigVersionCode = ConfigVersionCode;
    }

    public String getConfigVersion() {
        return ConfigVersion;
    }

    public void setConfigVersion(String ConfigVersion) {
        this.ConfigVersion = ConfigVersion;
    }

    public String getConfigFlag() {
        return ConfigFlag;
    }

    public void setConfigFlag(String ConfigFlag) {
        this.ConfigFlag = ConfigFlag;
    }
}
