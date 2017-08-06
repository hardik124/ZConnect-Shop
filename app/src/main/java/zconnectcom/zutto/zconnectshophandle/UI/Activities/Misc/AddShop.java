package zconnectcom.zutto.zconnectshophandle.UI.Activities.Misc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Coupons.home;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.ShopDetails.ShopDetails;

public class AddShop extends BaseActivity {

    private EditText shopCode , shopName;
    private Button signUpButton ;
    private DatabaseReference mDatabaseUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        signUpButton = (Button) findViewById(R.id.signUp);
        shopCode = (EditText) findViewById(R.id.shopcode);

        final View.OnClickListener signUpListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String code = shopCode.getText().toString();
                final String name = shopName.getText().toString();
                if(TextUtils.isEmpty(code))
                    showSnack(getString(R.string.shopCode));
                else if(TextUtils.isEmpty(name))
                    showSnack(getString(R.string.shopNameAlert));
                else if(code.contains("/")||code.contains(" "))
                    showSnack("Invalid characters , '/' and ' '");
                else if (isNetworkAvailable(AddShop.this)) {
                    showProgressDialog();
                    checkUser(code,name);
                }
                else
                    showSnack("Unable to connect , check your internet connection");

            }
        };
        final  TextView invalid = (TextView) findViewById(R.id.invalidCharacter);

        shopCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean signUp = true;
                for(int i = 0;i<s.length();i++)
                {
                    if(!Character.isLetterOrDigit(s.charAt(i))) {
                        invalid.setVisibility(View.VISIBLE);
                        signUp = false;
                        shopCode.setTextColor(ContextCompat.getColor(AddShop.this,R.color.invalidText));
                        invalid.setText(invalid.getText()+" "+s.charAt(i));
                    }
                }

                if(signUp) {
                    shopCode.setTextColor(ContextCompat.getColor(AddShop.this,android.R.color.white));
                    signUpButton.setOnClickListener(signUpListener);
                    invalid.setText("Invalid code");
                    invalid.setVisibility(View.INVISIBLE);
                }
                else {
                    shopCode.setTextColor(ContextCompat.getColor(AddShop.this,R.color.invalidText));
                    invalid.setVisibility(View.VISIBLE);
                    signUpButton.setOnClickListener(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        invalid.setTextColor(ContextCompat.getColor(AddShop.this,R.color.invalidText));
        shopName = (EditText) findViewById(R.id.shopName);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Shop/Shopkeepers");
        mDatabaseUsers.keepSynced(true);
    }



    private void checkUser(final String code,final String name) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                hideProgressDialog();
                showSnack("Connection timeout");
            }
        }, 6000);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(getString(R.string.emailId), getString(R.string.password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    try {
                        DatabaseReference ref = mDatabaseUsers.child(code);
                        DatabaseReference refShop = mDatabaseUsers.getParent().child("Shops").push();
                        ref.child("Key").setValue(refShop.getKey());
                        ref.child("Name").setValue(name);

                        Intent loginIntent = new Intent(AddShop.this, ShopDetails.class);
                        loginIntent.putExtra("ShopKey", refShop.getKey());
                        loginIntent.putExtra("ShopName", name);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        hideProgressDialog();
                        startActivity(loginIntent);

                    } catch (DatabaseException e) {
                        showSnack("Shop code must not contain '.', '#', '$', '[', or ']'");
                    }
                } else
                    showSnack("Unable to connect , retry again.");
                hideProgressDialog();
            }
        });
    }


}
