package zconnectcom.zutto.zconnectshophandle.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Shop.home;

public class logIn extends BaseActivity {

    private static final String TAG = "EmailPassword";
    private EditText shopCode;
    private Button logInButton;
    private DatabaseReference mDatabaseUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = (Button) findViewById(R.id.login);
        shopCode = (EditText) findViewById(R.id.shopcode);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Shopskeepers");
        mDatabaseUsers.keepSynced(true);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable(getApplicationContext())) {
                    showSnack("No internet available",Snackbar.LENGTH_INDEFINITE);
                } else {
                    showProgressDialog();
                    checkUser(shopCode.getText().toString());
                }

            }
        });}





public boolean isNetworkAvailable(final Context context) {
    final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
    return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
}



    private void checkUser(final String code) {
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(code)) {
                        hideProgressDialog();
                        Intent loginIntent = new Intent(logIn.this, home.class);
                        loginIntent.putExtra("ShopKey", (CharSequence) dataSnapshot.child(code).child("Key").getValue());
                        loginIntent.putExtra("ShopName",(CharSequence) dataSnapshot.child(code).child("Name").getValue());
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
