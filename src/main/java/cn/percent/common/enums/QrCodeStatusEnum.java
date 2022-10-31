package cn.percent.common.enums;

/**
 * @author: zhangpengju
 * Date: 2021/12/16
 * Time: 11:13
 * Description:
 */
public enum QrCodeStatusEnum {

    NOT_SCAN(0,"未被扫描"),
    SCANNED (1,"被扫描"),
    VERIFIED (2,"确认完后"),
    EXPIRED (3,"过期"),
    FINISH (4,"完成");


    private Integer value;

    private String desc;

    QrCodeStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * **根据value值获取枚举对象**
     * @param value
     */
    public static QrCodeStatusEnum getEnum(Integer value){
        QrCodeStatusEnum[] arr=values();
        int len = arr.length;

        for(int i = 0; i < len; ++i) {
            QrCodeStatusEnum status = arr[i];
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        return null;
    }
}
