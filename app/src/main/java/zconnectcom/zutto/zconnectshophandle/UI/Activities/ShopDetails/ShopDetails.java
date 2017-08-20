package zconnectcom.zutto.zconnectshophandle.UI.Activities.ShopDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Gallery.ShopGallery;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Menu.ShopMenu;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Misc.AddShop;
import zconnectcom.zutto.zconnectshophandle.Utils.IntentHandle;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;
import zconnectcom.zutto.zconnectshophandle.models.ShopDetailsItem;

public class ShopDetails extends BaseActivity {
    private final int GALLERY_REQUEST = 7;
    private EditText name, details, number, address, et_code, et_title;
    private Button category;
    private ImageView image;
    private ShopDetailsItem item = new ShopDetailsItem();
    private String ShopKey;
    private IntentHandle intentHandle;
    private Uri mImageUri;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        extras = getIntent().getExtras();
        ShopKey = extras.getString("ShopKey");
        setToolbar();
        item.setShopid(ShopKey);
        setTitle(extras.getString("ShopName") + "- Edit Details");
        showBackButton();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initLayout();
        setbuttons();
        setData();
        setGalleryAndMenu();


    }

    void initLayout() {
        name = (EditText) findViewById(R.id.shop_details_name);
        details = (EditText) findViewById(R.id.shop_details_details);
        et_code = (EditText) findViewById(R.id.et_offer_code);
        et_title = (EditText) findViewById(R.id.et_offer_title);


        number = (EditText) findViewById(R.id.shop_details_number);
        image = (ImageView) findViewById(R.id.shop_details_image);

        address = (EditText) findViewById(R.id.address);

    }

    void setbuttons() {

        Button menuBtn, GalBtn;
        GalBtn = (Button) findViewById(R.id.editGal);
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
                startActivityForResult(intentHandle.getPickImageIntent(ShopDetails.this), GALLERY_REQUEST);
            }
        });

        Button done, mapBtn;
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);

        done = (Button) appBarLayout.findViewById(R.id.Btndone);
        mapBtn = (Button) findViewById(R.id.mapButton);

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
                    startActivityForResult(builder.build(ShopDetails.this), 124);
                } catch (Exception e) {
                    showSnack("Cannot open Maps , Please input your venue.", Snackbar.LENGTH_LONG);
                }
            }
        });

        category = (Button) findViewById(R.id.category);

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showProgressDialog();
                final ArrayList<String> names = new ArrayList();
                FirebaseDatabase.getInstance().getReference("Shop").child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                            names.add(snapshot.child("category").getValue().toString());
                        }
                        hideProgressDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetails.this);
                        builder.setTitle("Select Category")
                                .setItems(names.toArray(new String[names.size()]), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        item.setCat(names.get(which));
                                        category.setText(names.get(which));
                                    }
                                })
                                .setCancelable(true)
                                .show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        hideProgressDialog();
                    }
                });
            }
        });

    }

    void setData() {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Shop/Shops");
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    item = dataSnapshot.child(ShopKey).getValue(ShopDetailsItem.class);
                    if(item == null)
                        item = new ShopDetailsItem();
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

        number.setPaintFlags(number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        number.setText(item.getNumber());

        number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        address.setText(item.getAddress());
        name.setText(item.getName());
        et_code.setText(item.getCode());
        details.setText(item.getDetails());
        et_title.setText(item.getCouponTitle());
        if(!TextUtils.isEmpty(item.getCat()))
            category.setText(item.getCat());


        if(URLUtil.isValidUrl(item.getImageurl())) {
            Picasso.with(ShopDetails.this).load(item.getImageurl()).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    image.setImageResource(R.drawable.ic_material_user_icon_black_24dp);
                }
            });
        } else
            image.setImageResource(R.drawable.ic_material_user_icon_black_24dp);
//            menu.setImageURI(Uri.parse(menuurl));

        LinearLayout linearLayout, numberlayout;

        numberlayout = (LinearLayout) findViewById(R.id.shop_details_num);
        linearLayout = (LinearLayout) findViewById(R.id.shop_details_directions);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + item.getLat() + "," + item.getLon()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        numberlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getNumber())));
            }
        });



    }

    void setGalleryAndMenu() {
        DatabaseReference mDatabase, mDatabaseMenu;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop").child("Gallery").child(ShopKey);
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference().child("Shop").child("Menu").child(ShopKey);
        mDatabase.keepSynced(true);
        mDatabaseMenu.keepSynced(true);
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

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView galleryRecycler;
        RecyclerView menuRecycler;
        galleryRecycler = (RecyclerView) findViewById(R.id.galleryRecycler);
        galleryRecycler.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuRecycler = (RecyclerView) findViewById(R.id.menuRecycler);
        menuRecycler.setLayoutManager(layoutManager1);

        galleryRecycler.setAdapter(firebaseRecyclerAdapter);
        FirebaseRecyclerAdapter<GalleryFormat, GalleryViewHolder> firebaseRecyclerAdapterMenu = new FirebaseRecyclerAdapter<GalleryFormat, GalleryViewHolder>(
                GalleryFormat.class,
                R.layout.gallery_row,
                ShopDetails.GalleryViewHolder.class,
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
        final DatabaseReference newData = FirebaseDatabase.getInstance().getReference().child("Shop/Shops").child(ShopKey);
        try {
            item.setName(name.getText().toString());
            item.setNumber(number.getText().toString());
            item.setAddress(address.getText().toString());
            item.setDetails(details.getText().toString());
            item.setCode(et_code.getText().toString());
            item.setCouponTitle(et_title.getText().toString());


            if(TextUtils.isEmpty(item.getCat()))
            {
                showSnack("Please select category");
                return;
            }
            else if(TextUtils.isEmpty(item.getName()))

            {
                showSnack("Please enter name");
                return;
            }
            else if(TextUtils.isEmpty(item.getNumber())||TextUtils.isEmpty(item.getAddress())||TextUtils.isEmpty(item.getDetails())) {
                showSnack("Fields are empty");
                return;
            }

            newData.setValue(item);
            showToast("Updated Successfully");
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
            showToast("Error updating");
        }
        hideProgressDialog();

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
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    mImageUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    Double ratio = Math.ceil(150000.0 / bitmap.getByteCount());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.min(ratio, 100), out);
                    String path = MediaStore.Images.Media.insertImage(ShopDetails.this.getContentResolver(), bitmap, mImageUri.getLastPathSegment(), null);

                    mImageUri = Uri.parse(path);
                    image.setImageURI(mImageUri);


                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Shops");
                    final StorageReference filepath = mStorage.child("Cover");
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();
                            item.setImageurl(imageUrl);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showSnack(error.getMessage());
            }
        }


        if (requestCode == 124 && resultCode == RESULT_OK) {
            Place addr = PlacePicker.getPlace(this, data);
            item.setLat(String.valueOf(addr.getLatLng().latitude));
            item.setLon(String.valueOf(addr.getLatLng().longitude));

            address.setText(addr.getName().toString() + addr.getAddress());
        }



    }


    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public GalleryViewHolder(View itemView) {
            super(itemView);
        }
        void setImage(Context ctx, String ImageUrl) {

            ImageView imageHolder = (ImageView) itemView.findViewById(R.id.galleryImage);
            Picasso.with(ctx).load(ImageUrl).into(imageHolder);
        }

    }

}

