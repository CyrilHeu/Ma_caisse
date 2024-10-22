package com.example.myapplication.ui;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import com.example.myapplication.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeedTicket {

    private MainActivity mainActivity;
    private String entete;

    private ArrayList<Produit_ticket> l_produit;
    private String etatAsuivre = "À suivre";
    private String etatAenvoyer = "À envoyer";

    public boolean changetable = false;

    private DocumentSnapshot old_snapshot;
    private LinearLayout linearLayoutProduit;
    LinearLayout linearLayoutPrix;
    ServiceBase sb;
    String str_key = "";
    String nb_key = "";
    int temps_max_annulation = 60;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public FeedTicket(MainActivity mainActivity,ArrayList<DocumentSnapshot> detail, ServiceBase sb) {
        this.mainActivity = mainActivity;
        this.sb = sb;
    }

    public void refresh(String v){
        Toast.makeText(mainActivity, v, Toast.LENGTH_SHORT).show();
    }

    public void refresh(DocumentSnapshot head_ticket, ArrayList<DocumentSnapshot> detail, Ticket ticket_en_cours, TextView infotabletv) {
        //Log.d("tag123", "refresh called");



        linearLayoutProduit = mainActivity.findViewById(R.id.LLproduit);
        linearLayoutPrix = mainActivity.findViewById(R.id.LLprix);

        int nb_produit = linearLayoutProduit.getChildCount();

        l_produit = new ArrayList<>();
        for(int i = 0; i < detail.size(); i++){
            //str_ticket += String.valueOf(detail.get(i).getData());
            Produit_ticket p = new Produit_ticket();
            p.setNom(detail.get(i).get("nom").toString());
            p.setEtat(detail.get(i).get("etat").toString());
            p.setPrix(Double.valueOf(detail.get(i).get("prix").toString()));
            p.setPaye_separe((boolean) detail.get(i).get("paye_separe"));
            p.setTimestamp(Long.valueOf(detail.get(i).get("timestamp").toString()));
            p.setColorEtat(detail.get(i).get("colorEtat").toString());
            p.setValueOption((List<String>) detail.get(i).get("options"));
            p.setTimestamp_envoi(Long.valueOf(detail.get(i).get("timestamp_envoi").toString()));
            if(detail.get(i).get("commentaire")!=null){
                p.setCommentaire(detail.get(i).get("commentaire").toString());
            }
            if(!p.getEtat().equals("annule") && !p.getEtat().equals("annule") && !p.getEtat().contains("supprime")) {
                l_produit.add(p);
            }
        }
        /* sorting la list par timestamp */
        Collections.sort(l_produit, new Comparator<Produit_ticket>() {
            public int compare(Produit_ticket o1, Produit_ticket o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        });

        TextView tv_infotable = mainActivity.findViewById(R.id.tvInfotable);

        sb.setTable_en_cours(tv_infotable.getText().toString());
        /*String old = "";
        if(sb.getTable_en_cours().equals("Ticket direct")){
            old = "direct";
        }else{
            old = sb.getTable_en_cours().split("Ticket table ")[1];
        }
        */
        ticket_en_cours.set(l_produit, sb.getTable_en_cours());
        /*
        String head = head_ticket.get("table").toString();

        if(old.equals(head)==false){
            Log.d("changement", "changement de table   old="+ old +"  head="+head);
            changetable = true;
        }*/

        /* nb de saisie */
        int nb_asuivre = 0;
        for(int i = 0 ; i<l_produit.size(); i++){
            if(l_produit.get(i).getEtat().equals(etatAsuivre)){
                nb_asuivre++;
            }
        }
        int nb_asuivre_ui = 0;
        for(int i = 0; i < linearLayoutProduit.getChildCount(); i++){
            TextView tvProduit = (TextView) linearLayoutProduit.getChildAt(i);
            if(tvProduit.getText().toString().contains(etatAsuivre)){
                nb_asuivre_ui++;
            }
        }

        int nb_a_envoyer = 0;
        for(int i = 0 ; i<l_produit.size(); i++){
            if(l_produit.get(i).getEtat().equals(etatAenvoyer)){
                nb_a_envoyer++;
            }
        }
        int nb_a_envoyer_ui = 0;
        for(int i = 0; i < linearLayoutProduit.getChildCount(); i++){
            TextView tvProduit = (TextView) linearLayoutProduit.getChildAt(i);
            if(tvProduit.getText().toString().contains(etatAenvoyer)){
                nb_a_envoyer_ui++;
            }
        }

        double total = 0;

        Log.d("123456chg","head ticket ="+head_ticket.get("table").toString()
            + " infotable ="+tv_infotable.getText().toString()
            + " sb.gettablencours"+ sb.getTable_en_cours()
        );

        boolean p_supp = false;
        for(int i = 0; i < l_produit.size(); i++){
            //Log.d("CheckSupp", "CheckSupp");
            if(l_produit.get(i).getEtat().contains("supprime")){
                p_supp = true;
                Log.d("tagSupp", "Supp yes");

            }
        }

        if(nb_produit!=l_produit.size()
                || (nb_produit!=l_produit.size() && head_ticket.get("table").equals(tv_infotable.getText().toString().split("table ")[1])==false)
                || (nb_produit!=l_produit.size() && head_ticket.get("table").equals("direct")==false)
                || nb_asuivre!=nb_asuivre_ui || nb_a_envoyer!=nb_a_envoyer_ui
                || old_snapshot==null
                || ((nb_produit==l_produit.size() && (head_ticket.get("table").equals(old_snapshot.get("table") )==false)))

                ){
            changetable = false;
            Log.d("tag123", "refresh called headticket="+head_ticket.get("table")+ "  infotable :"+tv_infotable.getText().toString());
            old_snapshot = head_ticket;
            linearLayoutProduit.removeAllViews();
            linearLayoutPrix.removeAllViews();
            for(int i = 0; i < l_produit.size(); i++){
                Produit_ticket p = l_produit.get(i);

                TextView tvProduit = new TextView(mainActivity);
                tvProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mainActivity, p.getNom() + " time:"+p.getTimestamp(), Toast.LENGTH_SHORT).show();
                    }
                });
                tvProduit.setTextColor(Color.WHITE);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                String nom = l_produit.get(i).getNom().substring(0, 1).toUpperCase() + l_produit.get(i).getNom().substring(1).toLowerCase();
                builder.append(nom);
                SpannableString s8 = new SpannableString("\n"+l_produit.get(i).getEtat());
                int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
                //if(l_produit.get(i).getEtat().equals(etatAsuivre)){

                s8.setSpan(new RelativeSizeSpan(0.5f), 0, s8.length(), flag);
                String color = l_produit.get(i).getColorEtat();
                if(color.equals("null")) color="#FFFFFF";
                if(l_produit.get(i).getEtat().equals("À envoyer")) color="#f70000";

                s8.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, s8.length(), flag);

                builder.append(s8);
               // }
                tvProduit.setText(builder);
                tvProduit.setTextSize(18);
                tvProduit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView tvPrix = new TextView(mainActivity);
                tvPrix.setText(df.format(l_produit.get(i).getPrix())+" €");
                tvPrix.setTextSize(18);
                tvPrix.setTextColor(Color.BLACK);
                tvPrix.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvPrix.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                linearLayoutProduit.addView(tvProduit);
                tvProduit.post(new Runnable() {
                    @Override
                    public void run() {
                        tvPrix.setHeight(tvProduit.getHeight());
                        linearLayoutPrix.addView(tvPrix);
                    }
                });

                total += l_produit.get(i).getPrix();
                TextView tvTotal = mainActivity.findViewById(R.id.tvTotal);
                tvTotal.setText(df.format(total)+" €");

                tvProduit.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Dialog tDialog = new Dialog(mainActivity);
                        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.annulation_detail, null);
                        Button btn_annulation = dialogView.findViewById(R.id.btnAnnulation_);
                        Button btn_suppression = dialogView.findViewById(R.id.btnSuppression);
                        LinearLayout llAnnulation = dialogView.findViewById(R.id.LLannulation_detail);
                        TextView tvOptions = new TextView(mainActivity);
                        TextView tvTempsRestant = new TextView(mainActivity);
                        TextView tvCommentaire = new TextView(mainActivity);
                        String str_options = "";
                        for(int i = 0; i < p.getValueOption().size();i++){
                            str_options += "- "+p.getValueOption().get(i) + "\n";
                        }
                        tvOptions.setText(str_options);
                        llAnnulation.addView(tvOptions);

                        if(p.getCommentaire()!=null){
                            llAnnulation.addView(tvCommentaire);
                            tvCommentaire.setText("Commentaire : "+p.getCommentaire()+ "\n");
                        }

                        llAnnulation.addView(tvTempsRestant);
                        CountDownTimer timer = new CountDownTimer(4000, 250) {

                            public void onTick(long l) {
                                long temps = temps_max_annulation-((Utils.timestamp_milliseconde()-p.getTimestamp())/1000);
                                if(temps>0 && (p.getEtat().equals("À envoyer") || p.getEtat().equals("À suivre"))){
                                    tvTempsRestant.setText("Temps restant pour annulation : " +temps+ " secondes");
                                }else{
                                    tvTempsRestant.setText("Annulation impossible");
                                    btn_annulation.setEnabled(false);
                                }
                            }
                            @Override
                            public void onFinish() {
                                start();
                            }
                        }.start();

                        btn_annulation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(Utils.timestamp_milliseconde()-p.getTimestamp()>60000 && p.getEtat()!="Envoyé"){ // 30 secondes annulation
                                    Toast.makeText(mainActivity, "annulation impossible",Toast.LENGTH_SHORT).show();
                                }else{
                                    sb.annuler(p.getNom(), p.getTimestamp());
                                    Toast.makeText(mainActivity, "annulation envoyée " +p.getNom() + " time:"+p.getTimestamp(), Toast.LENGTH_SHORT).show();
                                }

                                tDialog.dismiss();
                            }
                        });

                        btn_suppression.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                tDialog.dismiss();
                                //Toast.makeText(mainActivity, "suppression envoyée", Toast.LENGTH_SHORT).show();
                                String key = "1234";
                                // Ask for key manager//tDialog.dismiss();

                                Dialog tDialog1 = new Dialog(mainActivity);
                                LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                                View dialogView1 = inflater.inflate(R.layout.validation_supp_detail, null);
                                Button btn_validation = dialogView1.findViewById(R.id.btnValidationSuppr);
                                Button btn0 = dialogView1.findViewById(R.id.btn0_key);
                                Button btn1 = dialogView1.findViewById(R.id.btn1_key);
                                Button btn2 = dialogView1.findViewById(R.id.btn2_key);
                                Button btn3 = dialogView1.findViewById(R.id.btn3_key);
                                Button btn4 = dialogView1.findViewById(R.id.btn4_key);
                                Button btn5 = dialogView1.findViewById(R.id.btn5_key);
                                Button btn6 = dialogView1.findViewById(R.id.btn6_key);
                                Button btn7 = dialogView1.findViewById(R.id.btn7_key);
                                Button btn8 = dialogView1.findViewById(R.id.btn8_key);
                                Button btn9 = dialogView1.findViewById(R.id.btn9_key);
                                Button btnC = dialogView1.findViewById(R.id.btnCancel_key);

                                TextView tvKey = dialogView1.findViewById(R.id.tvKeySuppression);

                                EditText et_justif_supp = dialogView1.findViewById(R.id.et_justification_suppression);
                                Spinner spin_justif_supp = dialogView1.findViewById(R.id.spinChoixJustificationSuppr);

                                List<String> options = new ArrayList<>();
                                // liste des raisons de base : suppression produit sur ticket
                                options.add("");
                                options.add("Raison 1");
                                options.add("Raison 2");
                                options.add("Raison 3");

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(dialogView1.getContext(), android.R.layout.simple_spinner_item, options);

                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin_justif_supp.setSelection(-1);
                                spin_justif_supp.setAdapter(adapter);

                                str_key="";
                                nb_key="";
                                tvKey.setText(nb_key);

                                btn0.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="0";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="1";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="2";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="3";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="4";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="5";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="6";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="7";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="8";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);
                                    }
                                });
                                btn9.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key+="9";
                                        nb_key+="*";
                                        tvKey.setText(nb_key);

                                    }
                                });
                                btnC.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        str_key="";
                                        nb_key="";
                                        tvKey.setText(nb_key);
                                    }
                                });


                                btn_validation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        if(str_key.length()>3){
                                            Toast.makeText(mainActivity, "Key : "+str_key, Toast.LENGTH_SHORT).show();
                                            if(true){ // check base de key pour validation
                                                String justification = et_justif_supp.getText().toString();
                                                String raison = spin_justif_supp.getSelectedItem().toString();
                                                sb.suppression_p_feed(p.getNom(), p.getTimestamp(), key, justification, raison);
                                            }
                                            tDialog1.cancel();
                                        }else {
                                            Toast.makeText(mainActivity, "Saisie invalide", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                                tDialog1.setContentView(dialogView1);
                                tDialog1.show();
                                tDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        str_key="";
                                    }
                                });
                                // Ask for key manager

                            }
                        });

                        tDialog.setContentView(dialogView);
                        tDialog.show();
                        return false;
                    }
                });

            }
            Log.d("tag123", "refresh done of n element "+l_produit.size());

        }

    }

}
