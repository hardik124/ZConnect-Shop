package zconnectcom.zutto.zconnectshophandle.ViewHolder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Coupons.AddCoupon;
import zconnectcom.zutto.zconnectshophandle.models.Coupon;

public class CouponViewHolder extends RecyclerView.ViewHolder {
    ImageView mDel, mEdit;
    View mView;
    String shopName;

    public CouponViewHolder(View itemView) {
        super(itemView);
        mView = itemView;


    }

    public void setData(final String shopName) {
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

    public void setDelButton(final String couponkey) {

        mDel = (ImageView) mView.findViewById(R.id.del_coupon);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Shop/Offers").child(couponkey).removeValue();
            }
        });
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

    public void setDesc(String desc)
    {
        EditText text = ((EditText) mView.findViewById(R.id.offerDesc));
        text.setText(desc);
        text.setEnabled(false);
    }
}
