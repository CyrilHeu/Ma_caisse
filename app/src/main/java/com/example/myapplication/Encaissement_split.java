package com.example.myapplication;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class Encaissement_split {
    private Button btnMoins;
    private Button btnPlus;
    private TextView edtNb;
    private TextView tvTotalSplit;
    private TextView tvTotalSplit2;
    private int nb;
    private LinearLayout llPrixsplited;
    private TabLayout tbDiviser;
private String split;
private Double d_split;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public Encaissement_split(MainActivity mainActivity, double total, double restant) {

        Dialog tDialog = new Dialog(mainActivity);
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.encaissement_split, null);

        btnMoins = dialogView.findViewById(R.id.btnPaiementMoinsSplit);
        btnPlus = dialogView.findViewById(R.id.btnPaiementPlusSplit);
        edtNb = dialogView.findViewById(R.id.edtNbSplit);
        tvTotalSplit = dialogView.findViewById(R.id.tvPaiementPrixSplited);
        tvTotalSplit2 = dialogView.findViewById(R.id.tvPaiementPrixSplited2);

        llPrixsplited = dialogView.findViewById(R.id.LLprixclientsplited);
        tbDiviser = dialogView.findViewById(R.id.tabLayoutDiviser);

        nb = Integer.valueOf(edtNb.getText().toString());
        edtNb.setText(String.valueOf(nb));
        split = df.format(total/nb).replace(',', '.');
        tvTotalSplit.setText(split+" €");
        d_split = Double.valueOf(split);

        tbDiviser.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0 :
                        nb = Integer.valueOf(edtNb.getText().toString());
                        split = df.format(total/nb).replace(',', '.');
                        tvTotalSplit.setText(split+" €");
                        d_split = Double.valueOf(split);
                        if(d_split*nb!=total){
                            tvTotalSplit2.setText(df.format(total-d_split*(nb-1)).replace(',', '.')+" €");
                        }
                        if(split.equals(df.format(total-d_split*(nb-1)).replace(',', '.'))){
                            llPrixsplited.setVisibility(View.GONE);
                        }else{
                            llPrixsplited.setVisibility(View.VISIBLE);
                        }
                        btnMoins.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nb--;
                                if(nb<2)nb=2;
                                edtNb.setText(String.valueOf(nb));
                                String split = df.format(total/nb).replace(',', '.');
                                tvTotalSplit.setText(split+" €");
                                Double d_split = Double.valueOf(split);

                                if(d_split*nb!=total){
                                    tvTotalSplit2.setText(df.format(total-d_split*(nb-1)).replace(',', '.')+" €");
                                }

                                if(split.equals(df.format(total-d_split*(nb-1)).replace(',', '.'))){
                                    llPrixsplited.setVisibility(View.GONE);
                                }else{
                                    llPrixsplited.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                        btnPlus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nb++;
                                edtNb.setText(String.valueOf(nb));
                                String split = df.format(total/nb).replace(',', '.');
                                tvTotalSplit.setText(split+" €");
                                Double d_split = Double.valueOf(split);

                                if(d_split*nb!=total){
                                    tvTotalSplit2.setText(df.format(total-d_split*(nb-1)).replace(',', '.')+" €");
                                }
                                if(split.equals(df.format(total-d_split*(nb-1)).replace(',', '.'))){
                                    llPrixsplited.setVisibility(View.GONE);
                                }else{
                                    llPrixsplited.setVisibility(View.VISIBLE);
                                }
                                //tvTotalSplit.setText("");
                            }
                        });
                        break;
                    case 1 :
                        nb = Integer.valueOf(edtNb.getText().toString());
                        split = df.format(restant/nb).replace(',', '.');
                        tvTotalSplit.setText(split+" €");
                        d_split = Double.valueOf(split);
                        if(d_split*nb!=restant){
                            tvTotalSplit2.setText(df.format(restant-d_split*(nb-1)).replace(',', '.')+" €");
                        }
                        if(split.equals(df.format(restant-d_split*(nb-1)).replace(',', '.'))){
                            llPrixsplited.setVisibility(View.GONE);
                        }else{
                            llPrixsplited.setVisibility(View.VISIBLE);
                        }
                        btnMoins.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nb--;
                                if(nb<2)nb=2;
                                edtNb.setText(String.valueOf(nb));
                                String split = df.format(restant/nb).replace(',', '.');
                                tvTotalSplit.setText(split+" €");
                                Double d_split = Double.valueOf(split);

                                if(d_split*nb!=restant){
                                    tvTotalSplit2.setText(df.format(restant-d_split*(nb-1)).replace(',', '.')+" €");
                                }

                                if(split.equals(df.format(restant-d_split*(nb-1)).replace(',', '.'))){
                                    llPrixsplited.setVisibility(View.GONE);
                                }else{
                                    llPrixsplited.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                        btnPlus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nb++;
                                edtNb.setText(String.valueOf(nb));
                                String split = df.format(restant/nb).replace(',', '.');
                                tvTotalSplit.setText(split+" €");
                                Double d_split = Double.valueOf(split);

                                if(d_split*nb!=restant){
                                    tvTotalSplit2.setText(df.format(restant-d_split*(nb-1)).replace(',', '.')+" €");
                                }
                                if(split.equals(df.format(restant-d_split*(nb-1)).replace(',', '.'))){
                                    llPrixsplited.setVisibility(View.GONE);
                                }else{
                                    llPrixsplited.setVisibility(View.VISIBLE);
                                }
                                //tvTotalSplit.setText("");
                            }
                        });
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnMoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nb--;
                if(nb<2)nb=2;
                edtNb.setText(String.valueOf(nb));
                String split = df.format(total/nb).replace(',', '.');
                tvTotalSplit.setText(split+" €");
                Double d_split = Double.valueOf(split);

                if(d_split*nb!=total){
                    tvTotalSplit2.setText(df.format(total-d_split*(nb-1)).replace(',', '.')+" €");
                }

                if(split.equals(df.format(total-d_split*(nb-1)).replace(',', '.'))){
                    llPrixsplited.setVisibility(View.GONE);
                }else{
                    llPrixsplited.setVisibility(View.VISIBLE);
                }

            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nb++;
                edtNb.setText(String.valueOf(nb));
                String split = df.format(total/nb).replace(',', '.');
                tvTotalSplit.setText(split+" €");
                Double d_split = Double.valueOf(split);

                if(d_split*nb!=total){
                    tvTotalSplit2.setText(df.format(total-d_split*(nb-1)).replace(',', '.')+" €");
                }
                if(split.equals(df.format(total-d_split*(nb-1)).replace(',', '.'))){
                    llPrixsplited.setVisibility(View.GONE);
                }else{
                    llPrixsplited.setVisibility(View.VISIBLE);
                }
                //tvTotalSplit.setText("");
            }
        });

        if(d_split*nb!=total){
            tvTotalSplit2.setText(df.format(total-d_split*(nb-1)).replace(',', '.')+" €");
        }
        if(split.equals(df.format(total-d_split*(nb-1)).replace(',', '.'))){
            llPrixsplited.setVisibility(View.GONE);
        }else{
            llPrixsplited.setVisibility(View.VISIBLE);
        }


        tDialog.setContentView(dialogView);
        tDialog.show();
    }
}
