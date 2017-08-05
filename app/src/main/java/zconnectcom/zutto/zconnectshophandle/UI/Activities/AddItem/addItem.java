package zconnectcom.zutto.zconnectshophandle.UI.Activities.AddItem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.Utils.IntentHandle;
import zconnectcom.zutto.zconnectshophandle.models.GalleryFormat;

public class addItem extends BaseActivity {
    final int GALLERY_REQUEST = 7;
    Bundle extras;
    String type, key, shopName;
    IntentHandle intentHandle;
    ImageView mImage;
    Uri mImageUri;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setToolbar();
        extras = getIntent().getExtras();
        getSupportActionBar().setTitle(extras.getString("ShopName"));

        type = extras.getString("type");
        key = extras.getString("ShopKey");
        shopName = (extras.getString("ShopName"));
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.card).setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(shopName);
        mImage = (ImageView) findViewById(R.id.imgDisplay);
        mImage.setImageResource(R.drawable.addimage);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHandle = new IntentHandle();
                startActivityForResult(intentHandle.getPickImageIntent(getApplicationContext()), GALLERY_REQUEST);

            }
        });

        done = (Button) findViewById(R.id.btnDel);
        done.setVisibility(View.VISIBLE);
        done.setText("Add");
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageUri == null)
                    showSnack("Image not selected");
                else {
                    showProgressDialog();
                    final String name = Long.toHexString(Double.doubleToLongBits(Math.random()));
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Shops").child(key + shopName).child(type);
                    final StorageReference filepath = mStorage.child(name);
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();
                            setData(name, imageUrl);

                        }
                    });
                }

            }
        });

    }


    void setData(final String cName, String image) {
        final GalleryFormat item = new GalleryFormat();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop").child(type).child(key).push();
        item.setName(cName);
        item.setKey(mDatabase.getKey());
        item.setImage(image);
        mDatabase.setValue(item);
        hideProgressDialog();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageUri = intentHandle.getPickImageResultUri(data);
                CropImage.activity(mImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setSnapRadius(2)
                        .start(this);
            } catch (Exception e) {
                showSnack("Cannot select image , Retry");
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    mImageUri = result.getUri();
                    mImage.setImageURI(mImageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                    Double ratio = ((double) bitmap.getWidth()) / bitmap.getHeight();

                    if (bitmap.getByteCount() > 350000) {

                        bitmap = Bitmap.createScaledBitmap(bitmap, 960, (int) (960 / ratio), false);
                    }
                    String path = MediaStore.Images.Media.insertImage(addItem.this.getContentResolver(), bitmap, mImageUri.getLastPathSegment(), null);

                    mImageUri = Uri.parse(path);
                    mImage.setImageURI(mImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showSnack(error.getMessage());
            }
        }


    }

}
