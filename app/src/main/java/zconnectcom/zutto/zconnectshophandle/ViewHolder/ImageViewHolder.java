package zconnectcom.zutto.zconnectshophandle.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import zconnectcom.zutto.zconnectshophandle.R;


public class ImageViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    Button mDel;
    String ShopKey, ShopName;
    String Type;

    public ImageViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setData(String shopKey, String type, String shopName) {
        ShopKey = shopKey;
        ShopName = shopName;
        Type = type;
    }

    public void setDelButton(final String key, final String name) {

        mDel = (Button) mView.findViewById(R.id.btnDel);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shop").child(Type).child(ShopKey).child(key);
                    reference.removeValue();
                    StorageReference reference1 = FirebaseStorage.getInstance().getReference().child("Shops").child(ShopKey + ShopName).child(Type).child(name);
                    reference1.delete();

                } catch (Exception e) {
                    Toast.makeText(mView.getContext(), "Cannot delete image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }
            }
        });

    }

    public void setImage(final String url) {
        // mProg.show();
        final ImageView mImage = (ImageView) mView.findViewById(R.id.imgDisplay);
        (mView.findViewById(R.id.card)).setVisibility(View.INVISIBLE);
        (mView.findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);

        Picasso.with(mView.getContext()).load(url).into(mImage, new Callback() {
            @Override
            public void onSuccess() {
                (mView.findViewById(R.id.card)).setVisibility(View.VISIBLE);
                (mView.findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError() {
                
            }
        });
    }

}
