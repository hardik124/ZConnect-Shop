package zconnectcom.zutto.zconnectshophandle.UI.Activities.Coupons;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.AboutUs;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Gallery.ShopGallery;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Menu.ShopMenu;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.ShopDetails.ShopDetails;
import zconnectcom.zutto.zconnectshophandle.ViewHolder.CouponViewHolder;
import zconnectcom.zutto.zconnectshophandle.models.Coupon;

public class home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView mCouponList;
    DatabaseReference mCoupons;
    Bundle extras ;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        extras = getIntent().getExtras();
        //getToolbar().setTitle(extras.getString("ShopName"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle(extras.getString("ShopName"));
        setActionBarTitle(extras.getString("ShopName"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postCoupon = new Intent(home.this, AddCoupon.class);
                postCoupon.putExtra("ShopKey", extras.getString("ShopKey"));
                postCoupon.putExtra("ShopName", extras.getString("ShopName"));
                startActivity(postCoupon);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setDrawer();
        showCoupons();

    }

    void initRV() {
        mCoupons = FirebaseDatabase.getInstance().getReference("Shops/Coupons").child(extras.getString("ShopName"));
        mCouponList = (RecyclerView) findViewById(R.id.couponList);
        mCouponList.setHasFixedSize(true);
        mCoupons.keepSynced(true);
        LinearLayoutManager productLinearLayout = new LinearLayoutManager(getApplicationContext());
        productLinearLayout.setReverseLayout(true);
        productLinearLayout.setStackFromEnd(true);
        mCouponList.setLayoutManager(productLinearLayout);

    }

    void showCoupons() {
        initRV();
        FirebaseRecyclerAdapter<Coupon, CouponViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Coupon, CouponViewHolder>(

                Coupon.class,
                R.layout.row_coupons,
                CouponViewHolder.class,
                mCoupons
        ) {
            @Override
            protected void populateViewHolder(CouponViewHolder viewHolder, Coupon model, int position) {

                viewHolder.setData(extras.getString("ShopName"));
                viewHolder.setDelButton(model.getKey());
                viewHolder.setEditButton(model);
                viewHolder.setCode(model.getCode());
                viewHolder.setImage(model.getImage());
                viewHolder.setTitle(model.getName());
                viewHolder.setDesc(model.getDesc());
            }
        };mCouponList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.nav_bar, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Menu) {
            Intent menu = new Intent(this, ShopMenu.class);
            menu.putExtra("ShopKey", extras.getString("ShopKey"));
            menu.putExtra("ShopName", extras.getString("ShopName"));
            startActivity(menu);
        } else if (id == R.id.Coupons) {

//            Intent menu = new Intent(this, home.class);
//            menu.putExtra("ShopKey",extras.getString("Shopkey"));
//            menu.putExtra("ShopName",extras.getString("ShopName"));
//            startActivity(menu);

        } else if (id == R.id.signOut) {
            finish();

        } else if (id == R.id.about) {
            Intent menu = new Intent(this, AboutUs.class);
//            menu.putExtra("ShopKey",extras.getString("Shopkey"));
//            menu.putExtra("ShopName",extras.getString("ShopName"));
            startActivity(menu);

        } else if (id == R.id.profile) {
            Intent menu = new Intent(this, ShopDetails.class);
            menu.putExtra("ShopKey", extras.getString("ShopKey"));
            menu.putExtra("ShopName", extras.getString("ShopName"));
            startActivity(menu);
        } else if (id == R.id.Gallery) {
            Intent menu = new Intent(this, ShopGallery.class);
            menu.putExtra("ShopKey", extras.getString("ShopKey"));
            menu.putExtra("ShopName", extras.getString("ShopName"));
            startActivity(menu);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setDrawer() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop/Shops").child(extras.getString("ShopKey"));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.with(getApplicationContext()).load((String) dataSnapshot.child("imageurl").getValue()).into((ImageView) findViewById(R.id.UserPic));
                Picasso.with(getApplicationContext()).load((String) dataSnapshot.child("imageurl").getValue()).into(new Target() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        findViewById(R.id.navHeader).setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
                ((TextView) findViewById(R.id.userName)).setText((String) dataSnapshot.child("name").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
