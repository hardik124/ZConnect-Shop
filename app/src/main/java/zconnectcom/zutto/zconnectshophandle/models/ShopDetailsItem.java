package zconnectcom.zutto.zconnectshophandle.models;

public class ShopDetailsItem {
    String name;
    String number;
    String imageurl;
    String lat;
    String details;
    String lon;
    String menuurl;
    String cat;
    String shopid, address, code, couponTitle;

    public ShopDetailsItem(String name, String code, String shopid, String number, String imageurl, String lat, String details, String lon, String menuurl, String cat, String address) {
        this.name = name;
        this.number = number;
        this.imageurl = imageurl;
        this.lat = lat;
        this.details = details;
        this.lon = lon;
        this.menuurl = menuurl;
        this.cat = cat;
        this.shopid = shopid;
        this.address = address;
        this.code = code;
        this.couponTitle = couponTitle;
    }


    public ShopDetailsItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMenuurl() {
        return menuurl;
    }

    public void setMenuurl(String menuurl) {
        this.menuurl = menuurl;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }
}

