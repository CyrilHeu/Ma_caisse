package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class Encaissement_separer {

    private MainActivity mainActivity;
    private Double total;
    private Double restant;
    private ServiceBase sb;

    private Ticket ticket;

    public Encaissement_separer(MainActivity mainActivity, Double total, Double restant, ServiceBase sb, Ticket ticket) {
        this.mainActivity = mainActivity;
        this.total = total;
        this.restant = restant;
        this.sb = sb;
        this.ticket = ticket;

        Dialog tDialog = new Dialog(mainActivity);
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.encaissement_separer, null);

        List<Produit_ticket> l_produit = ticket.getList_produit();
        LinearLayout llGauche = dialogView.findViewById(R.id.LLSeparerListDetail);
        LinearLayout llDroite  = dialogView.findViewById(R.id.LLSeparerChoixDetail);

        for(int i = 0; i < l_produit.size(); i++){
            if(l_produit.get(i).getPaye_separe()==false){
                TextView tvProduit = new TextView(mainActivity);
                tvProduit.setTextSize(22);
                tvProduit.setBackgroundResource(R.drawable.border_bottomright_notselected);

                tvProduit.setPadding(0,5,0,5);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                tvProduit.setText(l_produit.get(i).getNom());
                tvProduit.setLayoutParams(params);
                tvProduit.setTag("ns");

                int finalI = i;
                tvProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i = 0; i < llDroite.getChildCount(); i++){
                            TextView tvDroite = (TextView) llDroite.getChildAt(i);
                            tvDroite.setBackgroundResource(R.drawable.border_bottomleft_notselected);
                            tvDroite.setTag("rightnotselec");
                        }
                        if(tvProduit.getTag().equals("ns")){
                            tvProduit.setTag("selec");
                            tvProduit.setBackgroundResource(R.drawable.border_bottomright_selected);
                        }else{
                            tvProduit.setTag("ns");
                            tvProduit.setBackgroundResource(R.drawable.border_bottomright_notselected);
                        }
                    }
                });

                llGauche.addView(tvProduit);
            }
        }

        Button btnAdd = dialogView.findViewById(R.id.btnAddDetailSepare);
        Button btnRemove = dialogView.findViewById(R.id.btnRemoveDetailSepare);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < llGauche.getChildCount(); i++){
                    TextView tvGauche = (TextView) llGauche.getChildAt(i);
                    TextView tvDroite = new TextView(mainActivity);
                    tvDroite.setTextSize(22);
                    tvDroite.setPadding(0,5,0,5);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tvDroite.setLayoutParams(params);

                    if(tvGauche.getTag().equals("selec") && tvGauche.getVisibility()!=View.GONE){
                        tvGauche.setVisibility(View.GONE);
                        tvDroite.setTag("rightnotselec");
                        tvDroite.setText(tvGauche.getText().toString());
                        tvDroite.setBackgroundResource(R.drawable.border_bottomleft_notselected);

                        tvDroite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(int i = 0; i < llGauche.getChildCount(); i++){
                                    TextView tvGauche = (TextView) llGauche.getChildAt(i);
                                    tvGauche.setBackgroundResource(R.drawable.border_bottomright_notselected);
                                    tvGauche.setTag("ns");
                                    tvGauche.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            for(int i = 0; i < llDroite.getChildCount(); i++){
                                                TextView tvDroite = (TextView) llDroite.getChildAt(i);
                                                tvDroite.setBackgroundResource(R.drawable.border_bottomleft_notselected);
                                                tvDroite.setTag("rightnotselec");
                                            }
                                            if(tvGauche.getTag().equals("ns")){
                                                tvGauche.setTag("selec");
                                                tvGauche.setBackgroundResource(R.drawable.border_bottomright_selected);
                                            }else{
                                                tvGauche.setTag("ns");
                                                tvGauche.setBackgroundResource(R.drawable.border_bottomright_notselected);
                                            }
                                        }
                                    });
                                }
                                if(tvDroite.getTag().equals("rightnotselec")){
                                    tvDroite.setTag("rightselec");
                                    tvDroite.setBackgroundResource(R.drawable.border_bottomleft_selected);
                                }else{
                                    tvDroite.setTag("rightnotselec");
                                    tvDroite.setBackgroundResource(R.drawable.border_bottomleft_notselected);
                                }
                            }
                        });

                        llDroite.addView(tvDroite);
                    }
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < llDroite.getChildCount();i++){
                    TextView tvDroite = (TextView) llDroite.getChildAt(i);
                    if(tvDroite.getTag().equals("rightselec") && tvDroite.getVisibility()!=View.GONE){

                        tvDroite.setVisibility(View.GONE);
                        TextView tvGauche = new TextView(mainActivity);
                        tvGauche.setTextSize(22);
                        tvGauche.setPadding(0,5,0,5);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        tvGauche.setLayoutParams(params);
                        tvGauche.setText(tvDroite.getText().toString());
                        tvGauche.setBackgroundResource(R.drawable.border_bottomright_notselected);
                        tvGauche.setTag("ns");
                        tvGauche.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(int i = 0; i < llDroite.getChildCount(); i++){
                                    TextView tvDroite = (TextView) llDroite.getChildAt(i);
                                    tvDroite.setBackgroundResource(R.drawable.border_bottomleft_notselected);
                                    tvDroite.setTag("rightnotselec");
                                }
                                if(tvGauche.getTag().equals("ns")){
                                    tvGauche.setTag("selec");
                                    tvGauche.setBackgroundResource(R.drawable.border_bottomright_selected);
                                }else{
                                    tvGauche.setTag("ns");
                                    tvGauche.setBackgroundResource(R.drawable.border_bottomright_notselected);
                                }
                            }
                        });
                        llGauche.addView(tvGauche,0);
                    }
                }
            }
        });

        tDialog.setContentView(dialogView);
        tDialog.show();
    }
}
