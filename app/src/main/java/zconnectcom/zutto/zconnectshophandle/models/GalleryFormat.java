package zconnectcom.zutto.zconnectshophandle.models;

public class GalleryFormat {
    String ImageName;
    String ImageUrl;
    String key;

    public GalleryFormat(String ImageName, String ImageUrl, String key) {
        this.ImageName = ImageName;
        this.ImageUrl = ImageUrl;
        this.key = key;
    }

    public GalleryFormat() {

    }

    public String getKey() {
        return key;
    }

    public String getImage() {
        return ImageUrl;
    }

    public void setImage(String imageurl) {
        this.ImageUrl = imageurl;
    }

    public String getTitle() {
        return ImageName;
    }

    public void setTitle(String name) {
        this.ImageName = name;
    }
}
