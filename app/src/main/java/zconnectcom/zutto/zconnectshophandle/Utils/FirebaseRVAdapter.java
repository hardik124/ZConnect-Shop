package zconnectcom.zutto.zconnectshophandle.Utils;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.ViewHolder.ImageViewHolder;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;

/**
 * Created by f390 on 5/4/17.
 */

public class FirebaseRVAdapter {

    String type;
    DatabaseReference mMenu;
    String key;

    public FirebaseRVAdapter(String type, DatabaseReference mMenu, String key) {
        this.type = type;
        this.mMenu = mMenu;
        this.key = key;
    }

    public FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder> showImage() {
        FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder>(

                GalleryFormat.class,
                R.layout.layout_fullscreen_image,
                ImageViewHolder.class,
                mMenu
        ) {
            @Override
            protected void populateViewHolder(ImageViewHolder viewHolder, GalleryFormat model, int position) {
                viewHolder.setData(key, type);
                viewHolder.setDelButton(model.getKey());
                viewHolder.setImage(model.getImage());
            }
        };
        return firebaseRecyclerAdapter;
    }
}
