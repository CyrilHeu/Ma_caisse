/* Auteur : Cyril Heurtaux
 heurtauxcyril@gmail.com
 */
package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnStats;
    private Button btnComment;
    private Button btnAsuivre;
    private Button btnEnvoyer;
    private Button btnEntree;

    private Button btnPlat;
    private Button btnDessert;
    private Button btnBoisson;
    private Button btnAlcool;
    private Button btnVin;
    private Button btnP1;
    private Button btnP1_5;
    private Button btnP2;
    private Button btnP3;
    private Button btnP4;
    private Button btnP5;
    private Button btnP6;
    private Button btnP7;
    private Button btnP8;
    private Button btnP9;
    private Button btnP10;
    private Button btnP11;
    private Button btnP12;
    private Button btnP13;
    private Button btnP14;
    private Button btnP15;
    private Button btnP16;
    private Button btnP17;
    private Button btnP18;
    private Button btnTable;

    private Button btnDirect;
    //private Spinner spinTable;
    private String TAG = "123";

    private Button btnPaiement;

    private ServiceBaseCategorie sbC;
    private String current_categorie;

    private ArrayList<Button> list_btn_produit;
    private ImageButton btnConnected;

    private Double n_table_ouverte;
    private Boolean b_asuivre = false;
    private String etatAsuivre = "À suivre";
    private Ticket ticket_en_cours;

    private ImageButton button_cancel_table;
    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;

    private TextView tv_saisie;
    private TextView tv_restant;
    private TextView tv_total;
    private Button btnVirgule;
    private Button btnCancel;
    private Button btnEsp;
    private Button btnCB;
    private Button btnTR;

    private Double total;
    private Double restant;
    private String strMonnaie = "";
    private Boolean VirguleCheck = false;
    private int ApresVirgule = 0;
    private int SizeMaxStrMonnaie = 7;

    private LinearLayout llCategorie;
    private LinearLayout llButtons;

    private TextView tvInfotable;
    private  ArrayList<Button> list_btn_categorie;

    private Drawable d;
    private int index_test = 0;

    private boolean b_commentaire = false;
    String commentaire = "";

    private String str_key = "";
    private String nb_key = "";

    private boolean suiteMultiple = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStats = findViewById(R.id.btnStat);
        btnComment = findViewById(R.id.btnCommentaire);
        btnAsuivre = findViewById(R.id.btnAsuivre);
        btnEnvoyer = findViewById(R.id.btnEnvoyer);
        btnAsuivre.setTag("#FFFFFF"); // color de base
        btnDirect = findViewById(R.id.btnDirect);

        tvInfotable = findViewById(R.id.tvInfotable);
        btnConnected = findViewById(R.id.btnConnected);
        btnTable = findViewById(R.id.btnTable);
        //spinTable = findViewById(R.id.spinTable);
        btnPaiement = findViewById(R.id.btnPaiement);
        // init button Produit et init list

        list_btn_produit = new ArrayList<>();
        llCategorie = findViewById(R.id.LLCategorie);
        llButtons = findViewById(R.id.LLbuttons);

        ticket_en_cours = new Ticket();

        ServiceBase sb = new ServiceBase(list_btn_produit,llButtons, this, btnConnected, ticket_en_cours, llCategorie, list_btn_categorie, d);
        // wait fin de chargement
        //
        for(int i = 0; i < list_btn_produit.size(); i++){
            int index = i;
            index_test = i;
            list_btn_produit.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, ""/*String.valueOf(list_btn_produit.get(index))*/, Toast.LENGTH_SHORT).show();
                    String value = list_btn_produit.get(index).getText().toString();
                    current_categorie = sb.getServiceBaseCategorie().getCurrent_categorie();
                    if(tvInfotable.getText().toString().equalsIgnoreCase("info_table")==false){
                        if(value.length()>0){
                            if(suiteMultiple==true){
                                if(getSuiteChoice().equals("null")==true || getSuiteChoice().equals("#FFFFFF")==true){
                                    sb.clickProduit(value, "À envoyer", getSuiteChoice(), current_categorie, commentaire);
                                }else{
                                    sb.clickProduit(value, etatAsuivre, getSuiteChoice(), current_categorie, commentaire);
                                }
                            }else{
                                if(b_asuivre){
                                    sb.clickProduit(value, etatAsuivre, getSuiteChoice(), current_categorie, commentaire);
                                }else{
                                    sb.clickProduit(value, "À envoyer", getSuiteChoice(), current_categorie, commentaire);
                                }
                            }

                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Mode direct nécéssaire.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            list_btn_produit.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getApplicationContext(), "long click "+ index, Toast.LENGTH_SHORT).show();
                    //sb.modifierProduit(index, current_categorie);
                    GestionProduit gp = new GestionProduit(MainActivity.this, sb, index, current_categorie, list_btn_produit.get(index).getText().toString());

                    return false;
                }
            });

        }
        btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDirect.setBackgroundColor(Color.BLACK);
                tvInfotable.setText("Ticket direct");
                //LinearLayout llDetailTable = findViewById(R.id.LLDetailTable);
                //llDetailTable.removeAllViews();
                sb.setTable_en_cours("Ticket direct");
                sb.nouveauTicket((double)777);
            }
        });


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creation user
                //Toast.makeText(getBaseContext(), btnComment.getBackground(), Toast.LENGTH_SHORT).show();
                Dialog tDialog = new Dialog(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.saisie_commentaire, null);

                EditText et_saisie_commentaire = dialogView.findViewById(R.id.editNoteCommentaireProduitTicket);
                Button btn_valider_commentaire = dialogView.findViewById(R.id.btnValiderCommentaireProduitTicket);
                Button btn_annuler_commentaire = dialogView.findViewById(R.id.btnAnnulerSaisieCommentaireProduitTicket);
                if(!b_commentaire){
                    // ici ici

                    btn_valider_commentaire.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commentaire = et_saisie_commentaire.getText().toString();

                            if(commentaire.length()>0){
                                btnComment.setBackgroundColor(Color.CYAN);
                                tDialog.dismiss();
                                b_commentaire = true;
                            }else{
                                Toast.makeText(MainActivity.this, "Commentaire invalide", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btn_annuler_commentaire.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commentaire = "";
                            et_saisie_commentaire.setText("");
                            tDialog.dismiss();
                        }
                    });
                    tDialog.setContentView(dialogView);
                    tDialog.show();

                }else{
                    commentaire = "";
                    et_saisie_commentaire.setText("");
                    b_commentaire = false;
                    btnComment.setBackgroundColor(Color.parseColor("#FF6200EE"));
                }

            }
        });

        btnAsuivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(suiteMultiple==true){
                    if(isAsuivre()==true){
                        btnAsuivre.setBackgroundColor(Color.parseColor("#FF6200EE"));
                        btnAsuivre.setTag("#FFFFFF");

                        // pas a suivre
                    }else{
                        GestionSuite gs = new GestionSuite(MainActivity.this, sb);

                    }

                }

                if(suiteMultiple==false) {
                    if (!b_asuivre) {
                        b_asuivre = true;
                        btnAsuivre.setBackgroundColor(Color.CYAN);
                    } else {
                        b_asuivre = false;
                        btnAsuivre.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }
            }
        });

        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog tDialog = new Dialog(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.choix_envoyer, null);
                LinearLayout ll_l_produit = findViewById(R.id.LLproduit);
                Boolean asuivre = false;
                Button envoyerLasuiteOrange = dialogView.findViewById(R.id.btnEnvoyerAsuivreOrange);
                Button envoyerLasuiteCyan = dialogView.findViewById(R.id.btnEnvoyerAsuivreCyan);
                Button envoyerLasuiteViolette = dialogView.findViewById(R.id.btnEnvoyerAsuivreViolette);
                Button envoyerLasuite = dialogView.findViewById(R.id.btnEnvoyerAsuivre);

                if(suiteMultiple==true){

                    envoyerLasuite.setVisibility(View.GONE);
                    envoyerLasuiteOrange.setVisibility(View.VISIBLE);
                    envoyerLasuiteCyan.setVisibility(View.VISIBLE);
                    envoyerLasuiteViolette.setVisibility(View.VISIBLE);

                    envoyerLasuiteOrange.setBackgroundColor(Color.parseColor("#FF8C00"));
                    envoyerLasuiteCyan.setBackgroundColor(Color.parseColor("#00FFFF"));
                    envoyerLasuiteViolette.setBackgroundColor(Color.parseColor("#800080"));
                    for(int i = 0 ; i < ll_l_produit.getChildCount();i++){
                        TextView tvProduit = (TextView) ll_l_produit.getChildAt(i);
                        if(tvProduit.getText().toString().contains(etatAsuivre)){
                            asuivre = true;
                        }
                    }

                    Button envoyer = dialogView.findViewById(R.id.btnEnvoyer_);

                    if(asuivre==false){
                        envoyerLasuiteOrange.setEnabled(false);
                        envoyerLasuiteCyan.setEnabled(false);
                        envoyerLasuiteViolette.setEnabled(false);
                    }
                    envoyer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Envoyé", Toast.LENGTH_SHORT).show();
                            tDialog.dismiss();
                            sb.envoyer(1);
                        }
                    });
                    envoyerLasuiteOrange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Suite orange envoyée", Toast.LENGTH_SHORT).show();
                            tDialog.dismiss();
                            sb.envoyer(2);
                        }
                    });
                    envoyerLasuiteCyan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Suite cyan envoyée", Toast.LENGTH_SHORT).show();
                            tDialog.dismiss();
                            sb.envoyer(3);
                        }
                    });
                    envoyerLasuiteViolette.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Suite violette envoyée", Toast.LENGTH_SHORT).show();
                            tDialog.dismiss();
                            sb.envoyer(4);
                        }
                    });
                }

                if(suiteMultiple==false){

                    envoyerLasuite.setVisibility(View.VISIBLE);
                    envoyerLasuiteOrange.setVisibility(View.GONE);
                    envoyerLasuiteCyan.setVisibility(View.GONE);
                    envoyerLasuiteViolette.setVisibility(View.GONE);

                    for(int i = 0 ; i < ll_l_produit.getChildCount();i++){
                        TextView tvProduit = (TextView) ll_l_produit.getChildAt(i);
                        if(tvProduit.getText().toString().contains(etatAsuivre)){
                            asuivre = true;
                        }
                    }

                    Button envoyer = dialogView.findViewById(R.id.btnEnvoyer_);

                    if(asuivre==false){
                        envoyerLasuite.setEnabled(false);

                    }
                    envoyer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Envoyé", Toast.LENGTH_SHORT).show();
                            tDialog.dismiss();
                            sb.envoyer(1);
                        }
                    });
                    envoyerLasuite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Suite envoyée", Toast.LENGTH_SHORT).show();
                            tDialog.dismiss();
                            sb.envoyer(5);
                        }
                    });

                }

                tDialog.setContentView(dialogView);
                tDialog.show();
            }
        });


        btnTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GestionTable gt = new GestionTable(MainActivity.this, sb);
            }
        });

        btnPaiement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encaissement encaissement = new Encaissement(MainActivity.this, sb, ticket_en_cours);
            }
        });

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnVirgule = findViewById(R.id.btnvirgule);
        btnCancel = findViewById(R.id.btnCancel);
        btnEsp = findViewById(R.id.btnESP);
        btnCB = findViewById(R.id.btnCB);
        btnTR = findViewById(R.id.btnTR);

        tv_saisie = findViewById(R.id.tvSaisieTicket);
        tv_total = findViewById(R.id.tvTotal);
        tv_restant = findViewById(R.id.tvSoldeTicket);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "1";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "2";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "3";
                        tv_saisie.setText(strMonnaie);
                    }
                }

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "4";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "5";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "5";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "6";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "7";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "8";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "9";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMonnaie="";
                tv_saisie.setText("--");
                VirguleCheck = false;
                ApresVirgule = 0;
            }
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_saisie.getText().toString().equals("--")) tv_saisie.setText("");
                if(strMonnaie.length() < SizeMaxStrMonnaie ) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "0";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });


        btnVirgule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strMonnaie.length() < SizeMaxStrMonnaie-1 && tv_saisie.getText().toString().equals("--")==false) {
                    if (VirguleCheck == false) {
                        if (strMonnaie.equals("")) {
                            strMonnaie += "0";
                        }
                        strMonnaie += ".";
                        tv_saisie.setText(strMonnaie);
                    }
                    VirguleCheck = true;
                }
            }
        });


        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), homepage.class);
                startActivity(intent);
            }
        });



        button_cancel_table = findViewById(R.id.btnAnnulerTicket);
        button_cancel_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Annulation table", "Annulation table");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation d'annulation");

                String mess_partie1 = "Voulez-vous vraiment continuer ?";
                String mess_partie2 = "Le ticket sera soldé en état, cette action est irréversible.";

                String message = mess_partie1 +
                        "<br></br><font color='#FF0000'>"+mess_partie2+ "</font>";
                builder.setMessage(Html.fromHtml(message));

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Dialog tDialog1 = new Dialog(MainActivity.this);
                        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
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

                        String key = "1234";
                        List<String> options = new ArrayList<>();
                        // liste des raisons de base : annulation de ticket
                        options.add("");
                        options.add("Raison 1");
                        options.add("Raison 2");
                        options.add("Raison 3");

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(dialogView1.getContext(), android.R.layout.simple_spinner_item, options);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_justif_supp.setSelection(-1);
                        spin_justif_supp.setAdapter(adapter);

                        str_key = "";
                        nb_key = "";
                        tvKey.setText(nb_key);

                        btn0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "0";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "1";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "2";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "3";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "4";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "5";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "6";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "7";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "8";
                                nb_key += "*";
                                tvKey.setText(nb_key);
                            }
                        });
                        btn9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key += "9";
                                nb_key += "*";
                                tvKey.setText(nb_key);

                            }
                        });
                        btnC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                str_key = "";
                                nb_key = "";
                                tvKey.setText(nb_key);
                            }
                        });


                        btn_validation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (str_key.length() > 3) {
                                    Toast.makeText(MainActivity.this, "Key : " + str_key, Toast.LENGTH_SHORT).show();
                                    if (true) { // check base de key pour validation
                                        String justification = et_justif_supp.getText().toString();
                                        String raison = spin_justif_supp.getSelectedItem().toString();
                                        //sb.cloreTicket(ticket_en_cours, "annulé", key, justification, raison);
                                        Toast.makeText(getApplicationContext(), "Vous avez confirmé l'annulation de la table", Toast.LENGTH_SHORT).show();

                                    }
                                    tDialog1.cancel();
                                } else {
                                    Toast.makeText(MainActivity.this, "Saisie invalide", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        tDialog1.setContentView(dialogView1);
                        tDialog1.show();
                        tDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                str_key = "";
                            }
                        });

                    }
                });

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Action à effectuer si l'utilisateur annule
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    public boolean isAsuivre(){

        String colorCode = (String)btnAsuivre.getTag();

        if(colorCode == "#FF8C00" || colorCode == "#00FFFF" || colorCode == "#800080" ) {
            //Toast.makeText(this, colorCode, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public String getSuiteChoice(){
        String tag = (String)btnAsuivre.getTag();
        return tag;
    }

}