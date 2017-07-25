package zconnectcom.zutto.zconnectshophandle.UI.Activities.Gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.AddItem.addItem;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.Utils.FirebaseRVAdapter;

public class ShopGallery extends BaseActivity {
    DatabaseReference mMenu;
    String key, name;
    Bundle extras;
    RecyclerView mMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_menu);
        extras = getIntent().getExtras();
        setToolbar();
        showBackButton();
        key = extras.getString("ShopKey");
        name = extras.getString("ShopName");
        getSupportActionBar().setTitle(name);

        mMenu = FirebaseDatabase.getInstance().getReference().child("Shop").child("Gallery").child(key);
        mMenu.keepSynced(true);
        mMenuList = (RecyclerView) findViewById(R.id.menuRV);
        LinearLayoutManager productLinearLayout = new LinearLayoutManager(this);
        productLinearLayout.setReverseLayout(true);
        productLinearLayout.setStackFromEnd(true);
        mMenuList.setLayoutManager(productLinearLayout);
        initRV();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postItem = new Intent(ShopGallery.this, addItem.class);
                postItem.putExtra("ShopKey", extras.getString("ShopKey"));
                postItem.putExtra("ShopName", extras.getString("ShopName"));
                postItem.putExtra("type", "Gallery");
                startActivity(postItem);

            }
        });


    }


    void initRV() {
        FirebaseRVAdapter menuAdapt = new FirebaseRVAdapter("Gallery", mMenu, key, name);
        mMenuList.setAdapter(menuAdapt.showImage());

    }


}
