package zconnectcom.zutto.zconnectshophandle.models;

import java.io.Serializable;

public class Coupon implements Serializable {

    String image, name, desc, code, key;


    public Coupon(String image, String name, String desc, String code, String key) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.code = code;
        this.key = key;
    }

    public Coupon() {

    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }
}
