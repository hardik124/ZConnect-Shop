package zconnectcom.zutto.zconnectshophandle.UI.Activities.Misc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;


public class logoFlash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logo_flash);
        // Setting full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handlePermission();
    }

//    void post(){
//        String email = "khincharishab@gmail.com\n" +
//                "f2015253@gia.bits-pilani.ac.in\n" +
//                "f2015670@goa.bits-pilani.ac.in\n" +
//                "f2016339@goa.bits-pilani.ac.in\n" +
//                "yourmom@goa.bits-pilani.ac.in\n" +
//                "f2016129@goa.bits-pilani.ac.in\n" +
//                "f2016613@goa.bits-pilani.ac.in\n" +
//                "f2015578@goa.bits-pilani.ac.in\n" +
//                "f2016372@goa.bits-pilani.ac.in\n" +
//                "f2016598@goa.bits-pilani.ac.in\n" +
//                "f2015728@goa.bits-pilani.ac.in\n" +
//                "f2016712@goa.bits-pilani.ac.in\n" +
//                "f2015034@goa.bits-pilani.ac.in\n" +
//                "f2013901@goa.bits-pilani.ac.in\n" +
//                "f2014685@goa.bits-pilani.ac.in\n" +
//                "f2016754@gmail.com\n" +
//                "f2016022@goa.bits-pilani.ac.in\n" +
//                "pranavgarg27@gmail.com\n" +
//                "f2013059@goa.bits-pilani.ac.in\n" +
//                "f2015601@goa.bits-pilani.ac.in\n" +
//                "akhilmjohn@gmail.com\n" +
//                "f2014551@goa.bits-pilani.ac.in\n" +
//                "f2016631@goa.bits-pilani.ac.in\n" +
//                "f2015126@goa.bits-pilani.ac.in\n" +
//                "adibatra1997@icloud.com\n" +
//                "f2015075@goa.bits-pilani.ac.in";
//
//        String name = "Rishab Khincha \n" +
//                "Ashish Phogat\n" +
//                "Vivek Khanna\n" +
//                "Mohnish Wadhwa\n" +
//                "Your Mom\n" +
//                "siddharth singh bhardwaj\n" +
//                "Shubham Singhal\n" +
//                "Salil Jain\n" +
//                "Praveen Karra\n" +
//                "Joshua Thampy\n" +
//                "Rishabh Sharma\n" +
//                "Arihant A Joshi\n" +
//                "Shrehal Bohra\n" +
//                "Eaga Akhil\n" +
//                "Varad Kulkarni\n" +
//                "Astitva Agrawal\n" +
//                "Gunjan Chhablani\n" +
//                "pranav garg\n" +
//                "Harnoor Singh Sachar\n" +
//                "Abhay Khandelwal\n" +
//                "Akhil John\n" +
//                "Sanket Patil\n" +
//                "Rushil gupta\n" +
//                "Rohit Srivastava\n" +
//                "Aditya Batra\n" +
//                "Nikhil Khedekar";
//
//        String no = "7888013992\n" +
//                "9702324766\n" +
//                "7696271970\n" +
//                "9967778430\n" +
//                "96969696969\n" +
//                "7083097424\n" +
//                "9601638386\n" +
//                "7775862751\n" +
//                "8411022553\n" +
//                "7888014682\n" +
//                "7030223206\n" +
//                "7057043237\n" +
//                "9479393202\n" +
//                "9503519346\n" +
//                "860587681\n" +
//                "7218946304\n" +
//                "7719900640\n" +
//                "9818913575\n" +
//                "8805301046\n" +
//                "7507554929\n" +
//                "+917769926296\n" +
//                "9518580184\n" +
//                "9970009299\n" +
//                "8698593374\n" +
//                "9552662467\n" +
//                "7798987753";
//
//        String details = "\tNirmaan, Drama, LDC\tBITSMUN, DEPP\n" +
//                "\tAbhigyaan, Cubix\t\n" +
//                "\t\t\n" +
//                "\tMAC\tDOSAM, DOCW\n" +
//                "President Bitch\tBITSkrieg, AeroD, TEDx, SAE, Nirmaan, MAC, Music Society, Celestia, Kala, Mime, Dance, Drama, ERC, CEL, Abhigyaan, Fash P, Cubix, Genesis, EPAC, MATRIX, ENACTUS, IEEE, Option 23, Your Mom\tSpree, WAVES, BITSMUN, DOPY, DOSAM, DOFAM, DORAH, Placement Unit, Arts & Deco, BackStage, DOJMA, DOMAC, DOCW, DOPN( Pro Nights), Quark, DEPP, Your Mom\n" +
//                "nothing\tERC\t\n" +
//                "\t\t\n" +
//                "\t\t\n" +
//                "\t\tDOPY, DOMAC\n" +
//                "\tAeroD, Abhigyaan\tDOSAM\n" +
//                "\t\t\n" +
//                "\tERC\tDORAH\n" +
//                "\tCEL, don't you consider LDC as a club ?\tQuark\n" +
//                "\t\t\n" +
//                "\"No\n" +
//                "\"\tnone\tnone\n" +
//                "\tWall Street\t\n" +
//                "\tKala, SDS\tQuark\n" +
//                "no\tENACTUS\t\n" +
//                "\tChess Club\t\n" +
//                "CoFunder of z connect\tCEL\tDOFAM\n" +
//                "\t\tBackStage\n" +
//                "No\t\t\n" +
//                "\tCelestia\t\n" +
//                "\"1. Sub-Coordinator of Film Making Club\n" +
//                "2. Co-Founder of  BPGC Film Making Club\"\tFMaC\t\n" +
//                "\t\tSpree, DORAH, DEPP\n" +
//                "Coordinator , Sandbox Student Committee\tAeroD, ERC\t";
//
//        String hostel =  "CH 6\n" +
//                "AH 4\n" +
//                "AH 5\n" +
//                "AH 6\n" +
//                "AH 1\n" +
//                "AH 8\n" +
//                "AH 8\n" +
//                "AH 5\n" +
//                "AH 6\n" +
//                "AH 7\n" +
//                "AH 5\n" +
//                "AH 8\n" +
//                "AH 3\n" +
//                "AH 6\n" +
//                "AH 2\n" +
//                "AH 7\n" +
//                "AH 7\n" +
//                "CH 3\n" +
//                "CH 1\n" +
//                "AH 5\n" +
//                "CH 1\n" +
//                "CH 2\n" +
//                "AH 8\n" +
//                "AH 4\n" +
//                "AH 8\n" +
//                "AH 3";
//
//        StringTokenizer nameST = new StringTokenizer(name);
//        StringTokenizer detailST = new StringTokenizer(details,"\n");
//        StringTokenizer hostelST = new StringTokenizer(hostel);
//        StringTokenizer noST = new StringTokenizer(no);
//        StringTokenizer emailST = new StringTokenizer(email);
//
//        while(true)
//        {
//            DatabaseReference mPost = FirebaseDatabase.getInstance().getReference("Phonebook");
//            String phnNo = noST.nextToken();
//            mPost=mPost.child(phnNo);
//            {
//                String detailsP = detailST.nextToken();
//                Log.d("no",detailsP);
//                mPost.child("category").setValue("S");
//                mPost.child("desc").setValue(detailsP);
//                mPost.child("email").setValue(emailST.nextToken());
//                mPost.child("hostel").setValue(hostelST.nextToken());
//                mPost.child("name").setValue(nameST.nextToken());
//                mPost.child("number").setValue(noST.nextToken());
//                mPost.child("imageurl").setValue("https://firebasestorage.googleapis.com/v0/b/zconnect-89fbd.appspot.com/o/PhonebookImage%2FdefaultprofilePhone.png?alt=media&token=5f814762-16dc-4dfb-ba7d-bcff0de7a336");
//            }
//        }
//
//    }

    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(logoFlash.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(logoFlash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED||
                 ContextCompat.checkSelfPermission(logoFlash.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(logoFlash.this, Manifest.permission.CAMERA)||
                        ActivityCompat.shouldShowRequestPermissionRationale(logoFlash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                        ActivityCompat.shouldShowRequestPermissionRationale(logoFlash.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(logoFlash.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Permissions are required .");
                    alertBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(logoFlash.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},7);

                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(logoFlash.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);
                }
            } else {
                return true;
            }
        }
        else {
            return true;
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 7:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   openHome();
                } else {
                    Toast.makeText(this,"Permission Denied !, Retrying.",Toast.LENGTH_SHORT).show();
                    checkPermission();
                }
                break;
        }
    }

    void openHome() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(logoFlash.this, logIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2800);
    }

    void handlePermission() {
        if (checkPermission())
            openHome();

    }
}

