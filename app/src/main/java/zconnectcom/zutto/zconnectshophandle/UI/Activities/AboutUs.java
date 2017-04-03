package zconnectcom.zutto.zconnectshophandle.UI.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import zconnectcom.zutto.zconnectshophandle.R;
import zconnectcom.zutto.zconnectshophandle.UI.Activities.Base.BaseActivity;

public class AboutUs extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        showBackButton();
        setActionBarTitle(R.string.title_activity_about_us);
    }

}
