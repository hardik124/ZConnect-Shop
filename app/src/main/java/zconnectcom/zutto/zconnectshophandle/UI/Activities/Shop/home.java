package zconnectcom.zutto.zconnectshophandle.UI.Activities.Shop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.ViewHolder.CouponViewHolder;
import zconnectcom.zutto.zconnectshophandle.models.coupon;

public class home extends BaseActivity {

    RecyclerView mCouponList;
    DatabaseReference mCoupons;
    Bundle extras ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showBackButton();
        extras = getIntent().getExtras();
        setActionBarTitle(extras.getString("ShopName"));
        showCoupons();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    void initRV() {
        mCoupons = FirebaseDatabase.getInstance().getReference("Shops/Coupons").child(extras.getString("ShopKey"));
        mCouponList = (RecyclerView) findViewById(R.id.couponList);
        mCouponList.setHasFixedSize(true);
        LinearLayoutManager productLinearLayout = new LinearLayoutManager(getApplicationContext());
        productLinearLayout.setReverseLayout(true);
        productLinearLayout.setStackFromEnd(true);
        mCouponList.setLayoutManager(productLinearLayout);

    }

    void showCoupons() {
        initRV();
        FirebaseRecyclerAdapter<coupon, CouponViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<coupon, CouponViewHolder>(

                coupon.class,
                R.layout.row_coupons,
                CouponViewHolder.class,
                mCoupons
        ) {
            @Override
            protected void populateViewHolder(CouponViewHolder viewHolder, coupon model, int position) {

                viewHolder.setData(model.getKey(),extras.getString("ShopName"));
                viewHolder.setCode(model.getCode());
                viewHolder.setImage(model.getImage());
                viewHolder.setTitle(model.getName());
                viewHolder.setDesc(model.getDesc());
            }
        };mCouponList.setAdapter(firebaseRecyclerAdapter);
    }
}
