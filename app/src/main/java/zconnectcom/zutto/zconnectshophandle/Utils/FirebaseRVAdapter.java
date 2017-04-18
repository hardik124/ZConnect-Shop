package zconnectcom.zutto.zconnectshophandle.Utils;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.ViewHolder.ImageViewHolder;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;


public class FirebaseRVAdapter {

    private String type, key;
    private DatabaseReference mMenu;

    public FirebaseRVAdapter(String type, DatabaseReference mMenu, String key, Context ctx) {
        this.type = type;
        this.mMenu = mMenu;
        this.key = key;
    }

    public FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder> showImage() {
//        ProgressDialog.Builder mProg = new ProgressDialog.Builder(context);
//        mProg.setTitle("Loading");
//        mProg.setCancelable(false);
//        mProg.show();
        return new FirebaseRecyclerAdapter<GalleryFormat, ImageViewHolder>(

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
    }
}
