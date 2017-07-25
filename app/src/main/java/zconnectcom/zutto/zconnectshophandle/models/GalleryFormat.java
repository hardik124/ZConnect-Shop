package zconnectcom.zutto.zconnectshophandle.models;

public class GalleryFormat {
    String imageurl;
    String key;
    String name;

    public GalleryFormat(String ImageName, String ImageUrl, String key) {
        this.imageurl = ImageUrl;
        this.key = key;
        this.name = ImageName;
    }

    public GalleryFormat() {

    }

    public String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return imageurl;
    }

    public void setImage(String imageurl) {
        this.imageurl = imageurl;
    }

}
