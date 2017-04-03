package zconnectcom.zutto.zconnectshophandle.models;

/**
 * Created by f390 on 3/4/17.
 */

public class coupon {

    String image , name , desc ,code ,key;




    public coupon(String image, String name, String desc, String code, String key) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.code = code;
        this.key = key;
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
