package zconnectcom.zutto.zconnectshophandle.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Coupons.home;

public class logIn extends BaseActivity {

    private EditText shopCode;
    private Button logInButton;
    private DatabaseReference mDatabaseUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = (Button) findViewById(R.id.login);
        shopCode = (EditText) findViewById(R.id.shopcode);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Shops/Shopkeepers");
        mDatabaseUsers.keepSynced(true);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(logIn.this))
                    showProgressDialog();
                checkUser(shopCode.getText().toString());

            }
        });}



    private void checkUser(final String code) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(getString(R.string.emailId), getString(R.string.password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            //Auth
                            if (dataSnapshot.hasChild(code)) {
                                Intent loginIntent = new Intent(logIn.this, home.class);
                                loginIntent.putExtra("ShopKey", dataSnapshot.child(code).child("Key").getValue().toString());
                                loginIntent.putExtra("ShopName", (dataSnapshot.child(code).child("Name").getValue()).toString());
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                hideProgressDialog();
                                startActivity(loginIntent);
                            } else
                                showSnack("Unable to find user , contact ZConnect");
                        hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showSnack("Unable to connect , retry again.");
                            showSnack(databaseError.toString());
                            hideProgressDialog();
                        }
                    });
                } else
                    showSnack("Unable to connect , retry again.");
                hideProgressDialog();
            }
        });
    }


}
