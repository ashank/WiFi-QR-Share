package com.vasudevrb.wifishare.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.vasudevrb.wifishare.R;
import com.vasudevrb.wifishare.presenter.WifiInfoPresenter;
import com.vasudevrb.wifishare.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiInfoActivity extends AppCompatActivity implements WifiInfoView {

    @BindView(R.id.text_wifi_info_ssid)
    TextView textSSID;

    @BindView(R.id.edit_wifi_info_password)
    TextInputEditText editPassword;

    @BindView(R.id.container_wifi_info)
    LinearLayout containerWifiInfo;

    @BindView(R.id.bottomsheet_qr)
    BottomSheetLayout bottomsheetQR;

    WifiInfoPresenter presenter;

    WifiConfiguration wifiConfiguration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);
        ButterKnife.bind(this);

        presenter = new WifiInfoPresenter();
        presenter.attachView(this);

        wifiConfiguration = getIntent().getParcelableExtra("config");

        String ssid = Utils.getSSID(wifiConfiguration);
        String security = Utils.getSecurity(wifiConfiguration);
        //Check if the password for selected ssid is present in SharedPreferences.
        //We save it when Generate QR button is clicked
        String password = getSharedPreferences("prefs_password", MODE_PRIVATE).getString(ssid + "/" + security, null);

        textSSID.setText(ssid);
        if (password != null) {
            editPassword.setText(password);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @OnClick(R.id.btn_wifi_info_generate_qr)
    public void onButtonClick() {
        if (editPassword.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Password can\'t be left empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String ssid = Utils.getSSID(wifiConfiguration);
        String password = editPassword.getText().toString();
        String security = Utils.getSecurity(wifiConfiguration);

        presenter.getQR(wifiConfiguration, editPassword.getText().toString());
        getSharedPreferences("prefs_password", MODE_PRIVATE).edit().putString(ssid + "/" + security, password).apply();
        hideKeyboard();
    }

    @OnClick(R.id.btn_wifi_info_back)
    public void onBackClick() {
        finish();
    }

    @Override
    public void showQR(Bitmap bitmap) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sheet_qr, bottomsheetQR, false);

        ImageView imageQR = (ImageView) view.findViewById(R.id.image_qr);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btn_sheet_close);

        imageQR.setImageBitmap(bitmap);
        btnClose.setOnClickListener(v -> bottomsheetQR.dismissSheet());

        bottomsheetQR.showWithSheetView(view);
        bottomsheetQR.expandSheet();
    }

    @Override
    public Context getContext() {
        return WifiInfoActivity.this;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
