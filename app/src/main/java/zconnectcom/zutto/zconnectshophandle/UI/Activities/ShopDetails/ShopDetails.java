package zconnectcom.zutto.zconnectshophandle.UI.Activities.ShopDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import zconnectcom.zutto.zconnectshophandle.Utils.CircularImageView;
import zconnectcom.zutto.zconnectshophandle.Utils.IntentHandle;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;
import zconnectcom.zutto.zconnectshophandle.models.ShopDetailsItem;

public class ShopDetails extends BaseActivity {
    final int GALLERY_REQUEST = 7;
    EditText name, details, number, address, et_code, et_title;
    LinearLayout linearLayout, numberlayout;
    CircularImageView image;
    String nam, detail, lat, lon, imageurl, num, menuurl, shopid = null, shopAdd, code, couponTitle;
    DatabaseReference mDatabase, mDatabaseMenu;
    HorizontalScrollView galleryScroll, menuScroll;
    Button done, mapBtn;
    String ShopKey;
    GalleryAdapter adapter;
    RecyclerView galleryRecycler;
    RecyclerView menuRecycler;
    IntentHandle intentHandle;
    Uri mImageUri;
    Button menuBtn, GalBtn;
    Boolean imageChanged = false;
    Bundle extras;
    Intent shopAddress;
    Place Venue;
    Boolean selectedFromMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        extras = getIntent().getExtras();
        ShopKey = extras.getString("ShopKey");

        setToolbar();
        setTitle(extras.getString("ShopName") + "- Edit Details");
        showBackButton();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initLayout();
        setbuttons();
        setData();


    }

    void initLayout() {
        name = (EditText) findViewById(R.id.shop_details_name);
        details = (EditText) findViewById(R.id.shop_details_details);
        et_code = (EditText) findViewById(R.id.et_offer_code);
        et_title = (EditText) findViewById(R.id.et_offer_title);
        numberlayout = (LinearLayout) findViewById(R.id.shop_details_num);
        linearLayout = (LinearLayout) findViewById(R.id.shop_details_directions);
        number = (EditText) findViewById(R.id.shop_details_number);
        image = (CircularImageView) findViewById(R.id.shop_details_image);
        menuBtn = (Button) findViewById(R.id.editPro);
        mapBtn = (Button) findViewById(R.id.mapButton);
        galleryScroll = (HorizontalScrollView) findViewById(R.id.galleryScroll);
        menuScroll = (HorizontalScrollView) findViewById(R.id.menuScroll);
        galleryScroll.setHorizontalScrollBarEnabled(false);
        menuScroll.setHorizontalScrollBarEnabled(false);
        address = (EditText) findViewById(R.id.address);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop").child("Shops").child(ShopKey).child("Gallery");
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference().child("Shop").child("Shops").child(ShopKey).child("Menu");

        mDatabase.keepSynced(true);
        mDatabaseMenu.keepSynced(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        galleryRecycler = (RecyclerView) findViewById(R.id.galleryRecycler);
        galleryRecycler.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagerMenu = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuRecycler = (RecyclerView) findViewById(R.id.menuRecycler);
        menuRecycler.setLayoutManager(layoutManagerMenu);
        GalBtn = (Button) findViewById(R.id.editGal);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        done = (Button) appBarLayout.findViewById(R.id.Btndone);
    }

    void setbuttons() {
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent menu = new Intent(ShopDetails.this, ShopMenu.class);
                menu.putExtra("ShopKey", extras.getString("ShopKey"));
                menu.putExtra("ShopName", extras.getString("ShopName"));
                startActivity(menu);

            }
        });

        GalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent menu = new Intent(ShopDetails.this, ShopGallery.class);
                menu.putExtra("ShopKey", extras.getString("ShopKey"));
                menu.putExtra("ShopName", extras.getString("ShopName"));
                startActivity(menu);

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHandle = new IntentHandle();
                startActivityForResult(intentHandle.getPickImageIntent(getApplicationContext()), GALLERY_REQUEST);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                LatLngBounds bitsGoa = new LatLngBounds(new LatLng(15.386095, 73.876165), new LatLng(15.396108, 73.878407));
                builder.setLatLngBounds(bitsGoa);
                try {
                    shopAddress = builder.build(ShopDetails.this);
                    selectAddress();
                } catch (Exception e) {
                    showSnack("Cannot open Maps , Please input your venue.", Snackbar.LENGTH_LONG);
                }
            }
        });
    }

    void setData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Shop/Shops");
        final ShopDetailsItem[] item = new ShopDetailsItem[1];
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    item[0] = dataSnapshot.child(ShopKey).getValue(ShopDetailsItem.class);
                    nam = item[0].getName();
                    detail = item[0].getDetails();
                    lat = item[0].getLat();
                    lon = item[0].getLon();
                    imageurl = item[0].getImageurl();
                    menuurl = item[0].getMenuurl();
                    num = item[0].getNumber();
                    shopid = item[0].getShopid();
                    shopAdd = item[0].getAddress();
                    code = item[0].getCode();
                    couponTitle = item[0].getCouponTitle();

                    initData();

                } catch (Exception e) {
                    showSnack("Shop details not available , add now .");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    void initData() {
        name.setEnabled(false);
        number.setPaintFlags(number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        number.setText(num);
        address.setText(shopAdd);
        name.setText(nam);
        et_code.setText(code);
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
        if (number.getText().toString().compareTo(num) != 0)
            newData.child("number").setValue(number.getText().toString());

        if (details.getText().toString().compareTo(detail) != 0)
            newData.child("details").setValue(details.getText().toString());

        if (et_code.getText().toString().compareTo(code) != 0)
            newData.child("code").setValue(et_code.getText().toString());
        if (et_title.getText().toString().compareTo(couponTitle) != 0)
            newData.child("couponTitle").setValue(et_title.getText().toString());

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
        if (selectedFromMap) {
            LatLng latLng = Venue.getLatLng();
            newData.child("lon").setValue(String.valueOf(latLng.longitude));
            newData.child("lat").setValue(String.valueOf(latLng.latitude));
        }
        newData.child("address").setValue(address.getText().toString().replace(".", " ").replace("/", "-"));
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
                showSnack(error.getMessage());
            }
        }


        if (requestCode == 124 && resultCode == RESULT_OK) {
            Venue = PlacePicker.getPlace(this, data);
            address.setText(Venue.getName().toString() + Venue.getAddress());
            selectedFromMap = true;
        }



    }

    void selectAddress() {
        startActivityForResult(shopAddress, 124);
    }

    private static class GalleryViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setImage(Context ctx, String ImageUrl) {

            ImageView imageHolder = (ImageView) mView.findViewById(R.id.galleryImage);
            Picasso.with(ctx).load(ImageUrl).into(imageHolder);


        }

    }

}

