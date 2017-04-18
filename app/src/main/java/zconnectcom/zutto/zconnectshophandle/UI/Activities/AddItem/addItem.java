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

import java.io.ByteArrayOutputStream;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.Utils.IntentHandle;

public class addItem extends BaseActivity {
    final int GALLERY_REQUEST = 7;
    Bundle extras;
    String type, key;
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
        setActionBarTitle(extras.getString("ShopName"));
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
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("ShopMenu").child(extras.getString("ShopName"));
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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop").child(type).child(key).push();
        mDatabase.child("imageurl").setValue(image);
        mDatabase.child("key").setValue(mDatabase.getKey());
        hideProgressDialog();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageUri = intentHandle.getPickImageResultUri(data);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Double ratio = 200000.0 / bitmap.getByteCount();
                bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.ceil(ratio * 100), out);
                String path = MediaStore.Images.Media.insertImage(addItem.this.getContentResolver(), bitmap, mImageUri.getLastPathSegment(), null);

                mImageUri = Uri.parse(path);
                mImage.setImageURI(mImageUri);
            } catch (Exception e) {
                showSnack("Cannot select image , Retry");
            }


        }


    }

}
