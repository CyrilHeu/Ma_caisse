package com.example.myapplication;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Encaissement {
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

    private Button btnVirgule;
    private Button btnCancel;
    private Button btnEsp;
    private Button btnCB;
    private Button btnTR;
    private Button btnTiroir;
    private Button btnDiviser;
    private Button btnSeparer;

    private TextView tv_saisie;

    private TextView tv_arendre;

    private TextView tv_total;

    private TextView tv_restant;
    private Double total;
    private Double restant;

    private String strMonnaie = "";                     // chaine de caracteres de la monnaie saisie
    private Boolean VirguleCheck = false;               // bool pour eviter double virgule dans la chaine
    private int ApresVirgule = 0;                       // var incrementé suivant chiffre apres virgule
    private int SizeMaxStrMonnaie = 8;
    public Encaissement(MainActivity mainActivity, ServiceBase sb, Ticket ticket_en_cours) {
        Dialog tDialog = new Dialog(mainActivity);
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.encaissement, null);

        tv_total = dialogView.findViewById(R.id.tvPaiementTotal);
        TextView _tv_total = mainActivity.findViewById(R.id.tvTotal);
        tv_total.setText(_tv_total.getText());
        total = Double.parseDouble(_tv_total.getText().toString().split("€")[0].replace(",","."));

        btnDiviser = dialogView.findViewById(R.id.btnDiviser);
        btnDiviser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //outil de division
                restant = 40.00;
                Encaissement_split encaissement_split = new Encaissement_split(mainActivity, total, restant);
            }
        });

        tDialog.setContentView(dialogView);
        tDialog.show();

        btnSeparer = dialogView.findViewById(R.id.btnSeparation);
        btnSeparer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restant = 40.00;
                Encaissement_separer encaissement_separer = new Encaissement_separer(mainActivity, total, restant, sb, ticket_en_cours);
            }
        });

        btn0 = dialogView.findViewById(R.id.btn0);
        btn1 = dialogView.findViewById(R.id.btn1);
        btn2 = dialogView.findViewById(R.id.btn2);
        btn3 = dialogView.findViewById(R.id.btn3);
        btn4 = dialogView.findViewById(R.id.btn4);
        btn5 = dialogView.findViewById(R.id.btn5);
        btn6 = dialogView.findViewById(R.id.btn6);
        btn7 = dialogView.findViewById(R.id.btn7);
        btn8 = dialogView.findViewById(R.id.btn8);
        btn9 = dialogView.findViewById(R.id.btn9);
        btnVirgule = dialogView.findViewById(R.id.btnvirgule);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnEsp = dialogView.findViewById(R.id.btnPayerEspece);
        btnCB = dialogView.findViewById(R.id.btnPayerCB);
        btnTR = dialogView.findViewById(R.id.btnPayerTR);

        tv_saisie = dialogView.findViewById(R.id.tvPaiementSaisie);
        tv_arendre = dialogView.findViewById(R.id.tvPaiementArendre);
        tv_total = dialogView.findViewById(R.id.tvPaiementTotal);
        tv_restant = dialogView.findViewById(R.id.tvPaiementRestant);



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
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
                tv_arendre.setText("0.0€");
                if(strMonnaie.length() < SizeMaxStrMonnaie) {
                    if(VirguleCheck == true) ApresVirgule++;
                    if(ApresVirgule < 3) {
                        strMonnaie += "9";
                        tv_saisie.setText(strMonnaie);
                    }
                }
            }
        });
        /*btnEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SaisieList.isEmpty()==false){
                    mode_paiment = 1;
                    clearTextViexRendu();
                    onEspClick();
                    textViewMonnaie.setText("");
                    strMonnaie="";
                    VirguleCheck = false;
                }else{
                    Snackbar.make(getCurrentFocus(), "ticket vide", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });*/

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_arendre.setText("0.0€");
                strMonnaie="";
                tv_saisie.setText(strMonnaie);
                VirguleCheck = false;
                ApresVirgule = 0;
            }
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_arendre.setText("0.0€");
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

                if(strMonnaie.length() < SizeMaxStrMonnaie-1) {
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

    }
}
