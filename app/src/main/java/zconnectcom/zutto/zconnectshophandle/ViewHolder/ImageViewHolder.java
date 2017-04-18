package zconnectcom.zutto.zconnectshophandle.ViewHolder;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import zconnectcom.zutto.zconnectshophandle.R;


public class ImageViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    Button mDel;
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
                try {
                    FirebaseDatabase.getInstance().getReference("Shop/Shops").child(ShopKey).child(Type).child(key).removeValue();
                } catch (Exception e) {
                    Toast.makeText(mView.getContext(), "Cannot delete image", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void setImage(final String url) {
        final ProgressDialog mProg = new ProgressDialog(mView.getContext());
        mProg.setMessage("Loading");
        // mProg.show();
        final ImageView mImage = (ImageView) mView.findViewById(R.id.imgDisplay);
        mImage.setVisibility(View.INVISIBLE);
        Picasso.with(mView.getContext()).load(url).into(mImage, new Callback() {
            @Override
            public void onSuccess() {
                mDel.setVisibility(View.VISIBLE);
                mImage.setVisibility(View.VISIBLE);
                mView.findViewById(R.id.imgDisplay).setVisibility(View.VISIBLE);
                mProg.dismiss();
            }

            @Override
            public void onError() {
                
            }
        });
    }

}
