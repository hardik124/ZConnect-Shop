package zconnectcom.zutto.zconnectshophandle.Utils;

import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.ViewHolder.ImageViewHolder;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;


public class FirebaseRVAdapter {

    private String type, key, ShopName;
    private DatabaseReference mMenu;

    public FirebaseRVAdapter(String type, DatabaseReference mMenu, String key, String shopName) {
        this.type = type;
        this.mMenu = mMenu;
        this.key = key;
        this.ShopName = shopName;
    }

    public FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder> showImage() {


        return new FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder>(

                GalleryFormat.class,
                R.layout.layout_fullscreen_image,
                ImageViewHolder.class,
                mMenu
        ) {
            @Override
            protected void populateViewHolder(ImageViewHolder viewHolder, GalleryFormat model, int position) {
                Toast.makeText(viewHolder.itemView.getContext(), "ShopName" + (ShopName == null), Toast.LENGTH_SHORT).show();
                Toast.makeText(viewHolder.itemView.getContext(), "ShopKey" + (key == null), Toast.LENGTH_SHORT).show();

                viewHolder.setData(key, type, ShopName);
                viewHolder.setDelButton(model.getKey(), model.getName());
                viewHolder.setImage(model.getImage());
            }


        };
    }
}
