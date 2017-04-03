package zconnectcom.zutto.zconnectshophandle.ViewHolder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import zconnectcom.zutto.zconnectshophandle.R;

public class CouponViewHolder extends RecyclerView.ViewHolder {
    ImageView mDel , mEdit;
    View mView;
    String couponkey;
    String shopName;

    public CouponViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        mDel = (ImageView) mView.findViewById(R.id.del_coupon);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add intent
            }
        });
        mEdit = (ImageView) mView.findViewById(R.id.editcoupon);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Shops/Coupons").child(shopName).child(couponkey).removeValue();
            }
        });
    }

    public void setData(final String couponkey , final String shopName)
    {
        this.couponkey = couponkey;
        this.shopName = shopName;
    }

    public void setImage(String url)
    {

        Picasso.with(mView.getContext()).load(url).into((ImageView) mView.findViewById(R.id.offerImage));
    }

    public void setTitle(String title)
    {
        EditText text = ((EditText) mView.findViewById(R.id.offerName));
        text.setEnabled(false);
        text.setText(title);
    }

    public void setCode(String code)
    {
        EditText text =((EditText) mView.findViewById(R.id.offerCode));
        text.setText(code);
        text.setEnabled(false);
    }

    public void setDesc(String desc)
    {
        EditText text = ((EditText) mView.findViewById(R.id.offerDesc));
        text.setText(desc);
        text.setEnabled(false);
    }
}
