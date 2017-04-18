package zconnectcom.zutto.zconnectshophandle.models;

public class GalleryFormat {
    String imageurl;
    String key;

    public GalleryFormat(String ImageName, String ImageUrl, String key) {
        this.imageurl = ImageUrl;
        this.key = key;
    }

    public GalleryFormat() {

    }

    public String getKey() {
        return key;
    }

    public String getImage() {
        return imageurl;
    }

    public void setImage(String imageurl) {
        this.imageurl = imageurl;
    }

}
