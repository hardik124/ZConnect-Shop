package zconnectcom.zutto.zconnectshophandle.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import zconnectcom.zutto.zconnectshophandle.R;

/**
 * Created by f390 on 5/4/17.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {
    String imageurl;
    Button mDel;
    View mView;
    String ShopKey;
    String Type;

    public ImageViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setData(String shopKey, String type) {
        ShopKey = shopKey;
        Type = type;
    }

    public void setDelButton(final String key) {

        mDel = (Button) mView.findViewById(R.id.btnDel);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Shop/Shops").child(ShopKey).child(Type).child(key).removeValue();
            }
        });
    }

    public void setImage(String url) {
        Picasso.with(mView.getContext()).load(url).into((ImageView) mView.findViewById(R.id.imgDisplay));
    }

}
