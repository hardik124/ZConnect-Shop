package zconnectcom.zutto.zconnectshophandle.UI.Activities.Coupons;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import zconnectcom.zutto.zconnectshophandle.Utils.IntentHandle;
import zconnectcom.zutto.zconnectshophandle.models.Coupon;

public class AddCoupon extends BaseActivity {
    final private int GALLERY_REQUEST = 7;
    ImageView mImage;
    Bundle extras;
    IntentHandle intentHandle;
    Button mPost;
    String key;
    EditText etName, etDesc, etCode;
    Boolean changeImage = false;
    private Uri mImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extras = getIntent().getExtras();

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_add_coupon);
        }
        initViews();
        extras = getIntent().getExtras();
        ImageViewClickListener();
        checkType();

    }

    void ImageViewClickListener() {
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentHandle = new IntentHandle();
                startActivityForResult(intentHandle.getPickImageIntent(getApplicationContext()), GALLERY_REQUEST);
            }
        });
    }

    void postButtonClickListener(String text) {
        mPost.setText(text);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(AddCoupon.this)) {
                    onClickPost();
                }
            }
        });
    }

    void initViews() {
        mPost = (Button) findViewById(R.id.postCoupon);
        mImage = (ImageView) findViewById(R.id.offerImage);
        etName = (EditText) findViewById(R.id.offerName);
        etDesc = (EditText) findViewById(R.id.offerDesc);
        etCode = (EditText) findViewById(R.id.offerCode);
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
                    String path = MediaStore.Images.Media.insertImage(AddCoupon.this.getContentResolver(), bitmap, mImageUri.getLastPathSegment(), null);

                    mImageUri = Uri.parse(path);
                    mImage.setImageURI(mImageUri);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    void onClickPost() {
        final String cName = etName.getText().toString();
        final String cDesc = etDesc.getText().toString();
        final String cCode = etCode.getText().toString();

        if (cName == null || cDesc == null || cCode == null) {
            showSnack("One or more fields are empty.");
            return;
        }
        if (mImageUri == null && !extras.containsKey("Coupons")) {
            Snackbar snack = Snackbar.make(mPost, "Image not selected ! Do you wish to continue?", Snackbar.LENGTH_INDEFINITE);
            snack.setAction("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postCoupon(cName, cDesc, cCode);
                }
            });

            TextView snackBarText = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
            snackBarText.setTextColor(Color.WHITE);
            snack.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.teal800));
            snack.show();
        } else
            postCoupon(cName, cDesc, cCode);

    }

    void postCoupon(final String cName, final String cDesc, final String cCode) {
        showProgressDialog();
        final String[] imageUrl = new String[1];
        if (mImageUri != null) {
            if (extras.containsKey("Coupons")) {
                if (changeImage) {
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("ShopCoupons").child(extras.getString("ShopName"));
                    final StorageReference filepath = mStorage.child(cName);
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();
                            setData(cName, cDesc, cCode, imageUrl);

                        }
                    });
                } else {
                    imageUrl[0] = ((Coupon) extras.get("Coupon")).getImage();
                }
            } else {

                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("ShopCoupons").child(extras.getString("ShopName"));
                final StorageReference filepath = mStorage.child(cName);
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imageUrl = taskSnapshot.getDownloadUrl().toString();
                        setData(cName, cDesc, cCode, imageUrl);
                    }
                });
            }
        } else
            setData(cName, cDesc, cCode, getResources().getString(R.string.defaultImage));
    }

    void setData(final String cName, final String cDesc, final String cCode, String image) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Shops/Coupons").child(extras.getString("ShopName"));
        if (extras.containsKey("Coupon"))
            mDatabase = mDatabase.child(key);
        else
            mDatabase = mDatabase.push();

        mDatabase.child("image").setValue(image);
        mDatabase.child("key").setValue(mDatabase.getKey());
        mDatabase.child("name").setValue(cName);
        mDatabase.child("desc").setValue(cDesc);
        mDatabase.child("code").setValue(cCode);
        hideProgressDialog();
        finish();
    }

    void checkType() {
        if (extras.containsKey("Coupon")) {
            Coupon coupon = (Coupon) extras.get("Coupon");
            setActionBarTitle(coupon.getName());
            key = coupon.getKey();
            inflateViews();
        } else {
            setActionBarTitle("Add coupon");
            postButtonClickListener("POST");

        }
    }

    void inflateViews() {
        Coupon coupon = (Coupon) extras.get("Coupon");
        Picasso.with(this).load(coupon.getImage()).into(mImage);
        etCode.setText(coupon.getCode());
        etDesc.setText(coupon.getDesc());
        etName.setText(coupon.getName());
        postButtonClickListener("DONE");

    }

}
