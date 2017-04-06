package zconnectcom.zutto.zconnectshophandle.UI.Views;

public interface BaseActivityCallback {

    void showProgressDialog(String message);

    void hideProgressDialog();

    void setToolbarTitle(String title);
}