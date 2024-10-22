package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class GestionSuite {

    private Button btnAsuivre;

    public GestionSuite(MainActivity mainActivity, ServiceBase sb) {

        Dialog tDialog = new Dialog(mainActivity);
        Window window = tDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        tDialog.getWindow().setDimAmount(0);
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.color_changesuite, null);

        LinearLayout suiteOrange = dialogView.findViewById(R.id.LLsuiteOrange);
        LinearLayout suiteCyan = dialogView.findViewById(R.id.LLsuiteCyan);
        LinearLayout suiteViolette= dialogView.findViewById(R.id.LLsuiteViolette);

        btnAsuivre = mainActivity.findViewById(R.id.btnAsuivre);

        suiteOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(isAsuivre()==true){
                    btnAsuivre.setBackgroundColor(Color.parseColor("#FF8C00"));
                    btnAsuivre.setTag("#FF8C00");
                    tDialog.dismiss();
                //}

            }
        });

        suiteCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(isAsuivre()==true) {
                    btnAsuivre.setBackgroundColor(Color.parseColor("#00FFFF"));
                    btnAsuivre.setTag("#00FFFF");
                    tDialog.dismiss();
              //  }
            }
        });
        suiteViolette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(isAsuivre()==true){
                    btnAsuivre.setBackgroundColor(Color.parseColor("#800080"));
                    btnAsuivre.setTag("#800080");
                    tDialog.dismiss();
              //  }

            }
        });

        tDialog.setContentView(dialogView);
        tDialog.show();

    }

    /*public boolean isAsuivre(){
        if(btnAsuivre.getTag().equals("FF8C00") || btnAsuivre.getTag().equals("00FFFF")
                || btnAsuivre.getTag().equals("800080")){
            return true;
        }
        return false;
    }*/
}
