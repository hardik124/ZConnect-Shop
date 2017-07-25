package zconnectcom.zutto.zconnectshophandle.ViewHolder;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Coupons.AddCoupon;
import zconnectcom.zutto.zconnectshophandle.Utils.statIncrement;
import zconnectcom.zutto.zconnectshophandle.models.Coupon;

public class CouponViewHolder extends RecyclerView.ViewHolder {
    ImageView mDel, mEdit;
    View mView;
    String shopName, shopKey;

    public CouponViewHolder(View itemView) {
        super(itemView);
        mView = itemView;


    }

    public void setData(final String shopName, final String shopKey) {
        this.shopKey = shopKey;
        this.shopName = shopName;
    }

    public void setEditButton(final Coupon Coupon) {
        mEdit = (ImageView) mView.findViewById(R.id.editcoupon);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(mView.getContext(), AddCoupon.class);
                edit.putExtra("ShopName", shopName);
                edit.putExtra("Coupon", Coupon);
                mView.getContext().startActivity(edit);
            }
        });
    }

    public void setDelButton(final String couponkey, final String name, final String recentKey) {

        mDel = (ImageView) mView.findViewById(R.id.del_coupon);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("home").child(recentKey).removeValue();
                FirebaseDatabase.getInstance().getReference("Shop/Offers").child(couponkey).removeValue();
                statIncrement statIncrement = new statIncrement("TotalOffers");
                statIncrement.change(false);

                FirebaseStorage.getInstance().getReference().child("Shops").child(shopKey + shopName).child("Coupons").child(name).delete();
            }
        });
    }
    public void setImage(String url)
    {
        (mView.findViewById(R.id.content)).setVisibility(View.INVISIBLE);
        (mView.findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        ImageView imageView = ((ImageView) mView.findViewById(R.id.offerImage));
        Picasso.with(mView.getContext()).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                (mView.findViewById(R.id.content)).setVisibility(View.VISIBLE);
                (mView.findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });
    }

    public void setTitle(String title)
    {
        EditText text = ((EditText) mView.findViewById(R.id.offerName));
        text.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
        text.setEnabled(false);
        text.setText(title);
    }

    public void setDesc(String desc)
    {
        EditText text = ((EditText) mView.findViewById(R.id.offerDesc));
        text.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
        text.setText(desc);
        text.setEnabled(false);
    }
}
