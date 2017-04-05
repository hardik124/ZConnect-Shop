package zconnectcom.zutto.zconnectshophandle.UI.Activities.ShopDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Gallery.ShopGallery;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Menu.ShopMenu;
import zconnectcom.zutto.zconnectshophandle.Utils.IntentHandle;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;
import zconnectcom.zutto.zconnectshophandle.models.ShopDetailsItem;

public class ShopDetails extends BaseActivity {
    final int GALLERY_REQUEST = 7;
    EditText name, details, number;
    LinearLayout linearLayout, numberlayout;
    ImageView menu, image;
    String nam, detail, lat, lon, imageurl, num, menuurl, shopid = null;
    DatabaseReference mDatabase, mDatabaseMenu;
    HorizontalScrollView galleryScroll, menuScroll;
    Button done;
    String ShopKey;
    GalleryAdapter adapter;
    RecyclerView galleryRecycler;
    RecyclerView menuRecycler;
    IntentHandle intentHandle;
    Uri mImageUri;
    Button menuBtn, GalBtn;
    Boolean imageChanged = false;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        extras = getIntent().getExtras();
        ShopKey = extras.getString("ShopKey");
        image = (ImageView) findViewById(R.id.shop_details_image);

        galleryScroll = (HorizontalScrollView) findViewById(R.id.galleryScroll);
        menuScroll = (HorizontalScrollView) findViewById(R.id.menuScroll);
        galleryScroll.setHorizontalScrollBarEnabled(false);
        menuScroll.setHorizontalScrollBarEnabled(false);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop").child("Shops").child(ShopKey).child("Gallery");
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference().child("Shop").child("Shops").child(ShopKey).child("Menu");

        mDatabase.keepSynced(true);
        mDatabaseMenu.keepSynced(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        done = (Button) appBarLayout.findViewById(R.id.Btndone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        buttons();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHandle = new IntentHandle();
                startActivityForResult(intentHandle.getPickImageIntent(getApplicationContext()), GALLERY_REQUEST);
            }
        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        galleryRecycler = (RecyclerView) findViewById(R.id.galleryRecycler);
        galleryRecycler.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagerMenu = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuRecycler = (RecyclerView) findViewById(R.id.menuRecycler);
        menuRecycler.setLayoutManager(layoutManagerMenu);
        setData();


    }


    void buttons() {
        menuBtn = (Button) findViewById(R.id.editPro);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent menu = new Intent(ShopDetails.this, ShopMenu.class);
                menu.putExtra("ShopKey", extras.getString("ShopKey"));
                menu.putExtra("ShopName", extras.getString("ShopName"));
                startActivity(menu);

            }
        });

        GalBtn = (Button) findViewById(R.id.editGal);
        GalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent menu = new Intent(ShopDetails.this, ShopGallery.class);
                menu.putExtra("ShopKey", extras.getString("ShopKey"));
                menu.putExtra("ShopName", extras.getString("ShopName"));
                startActivity(menu);

            }
        });
    }

    void setData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Shop/Shops");
        final ShopDetailsItem[] item = new ShopDetailsItem[1];
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item[0] = dataSnapshot.child(ShopKey).getValue(ShopDetailsItem.class);
                nam = item[0].getName();
                detail = item[0].getDetails();
                lat = item[0].getLat();
                lon = item[0].getLon();
                imageurl = item[0].getImageurl();
                menuurl = item[0].getMenuurl();
                num = item[0].getNumber();
                shopid = item[0].getShopid();
                initData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    void initData() {
        name = (EditText) findViewById(R.id.shop_details_name);
        name.setEnabled(false);
        details = (EditText) findViewById(R.id.shop_details_details);
//        Menu = (SimpleDraweeView) findViewById(R.id.shop_details_menu_image);
        number = (EditText) findViewById(R.id.shop_details_number);
        number.setPaintFlags(number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        numberlayout = (LinearLayout) findViewById(R.id.shop_details_num);
        linearLayout = (LinearLayout) findViewById(R.id.shop_details_directions);

        name.setText(nam);
        details.setText(detail);
        Picasso.with(ShopDetails.this).load(imageurl).into(image);
//            menu.setImageURI(Uri.parse(menuurl));
        number.setText(num);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        numberlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num)));
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("ShopName") + "- Edit Details");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(extras.getString("ShopName") + "- Edit Details");
        }


        FirebaseRecyclerAdapter<GalleryFormat, GalleryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GalleryFormat, GalleryViewHolder>(
                GalleryFormat.class,
                R.layout.gallery_row,
                GalleryViewHolder.class,
                mDatabase
        ) {

            @Override
            protected void populateViewHolder(GalleryViewHolder viewHolder, GalleryFormat model, int position) {

                viewHolder.setImage(getApplicationContext(), model.getImage());
            }

        };

        galleryRecycler.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerAdapter<GalleryFormat, GalleryViewHolder> firebaseRecyclerAdapterMenu = new FirebaseRecyclerAdapter<GalleryFormat, GalleryViewHolder>(
                GalleryFormat.class,
                R.layout.gallery_row,
                GalleryViewHolder.class,
                mDatabaseMenu
        ) {

            @Override
            protected void populateViewHolder(GalleryViewHolder viewHolder, GalleryFormat model, int position) {

                viewHolder.setImage(getApplicationContext(), model.getImage());
            }

        };

        menuRecycler.setAdapter(firebaseRecyclerAdapterMenu);


    }

    void updateData() {
        showProgressDialog();
        final DatabaseReference newData = FirebaseDatabase.getInstance().getReference().child("Shop/Shops").child(ShopKey);
        if (name.getText().toString().compareTo(nam) != 0)
            newData.child("name").setValue(name.getText().toString());
        if (name.getText().toString().compareTo(num) != 0)
            newData.child("number").setValue(number.getText().toString());

        if (name.getText().toString().compareTo(detail) != 0)
            newData.child("details").setValue(details.getText().toString());

        if (imageChanged) {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Shops");
            final StorageReference filepath = mStorage.child(nam);
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String imageUrl = taskSnapshot.getDownloadUrl().toString();
                    newData.child("imageurl").setValue(imageUrl);
                }
            });
        }
        hideProgressDialog();
        showSnack("Updated Successfully");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageUri = intentHandle.getPickImageResultUri(data);
            } catch (Exception e) {
                showSnack("Cannot select image , Retry");
            }
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setSnapRadius(2)
                    .setAspectRatio(3, 2)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    mImageUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 15, out);
                    String path = MediaStore.Images.Media.insertImage(ShopDetails.this.getContentResolver(), bitmap, mImageUri.getLastPathSegment(), null);

                    mImageUri = Uri.parse(path);
                    image.setImageURI(mImageUri);
                    imageChanged = true;


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setImage(Context ctx, String ImageUrl) {

            ImageView imageHolder = (ImageView) mView.findViewById(R.id.galleryImage);
            Picasso.with(ctx).load(ImageUrl).into(imageHolder);


        }

    }

}

