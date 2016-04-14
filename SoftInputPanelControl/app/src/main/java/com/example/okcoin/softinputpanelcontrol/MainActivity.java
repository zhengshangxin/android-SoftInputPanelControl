package com.example.okcoin.softinputpanelcontrol;

import com.example.lib.SoftInputTwoEdtCtrl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edtFirst  = (EditText) findViewById(R.id.edt_first);
        EditText edtSecond = (EditText) findViewById(R.id.edt_second);
        TextView txtConfirm = (TextView) findViewById(R.id.txt_confirm);
        View layoutBelowNav = findViewById(R.id.layout_below_nav);

        SoftInputTwoEdtCtrl twoEdtCtrl = new SoftInputTwoEdtCtrl(this);
        twoEdtCtrl.register(txtConfirm, layoutBelowNav, edtFirst, edtSecond);

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(v);
            }
        });
    }

    private void hideSoftInput(View v) {
        InputMethodManager imm  = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
    }
}
