package com.example.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GestionTable {

    private static String str_numero = "";
    private static double numero;
    private static int SizeMaxStrMonnaie = 4;
    private static Boolean VirguleCheck = false;
    private static int ApresVirgule = 0;
    private ArrayList<String> ls_table;

    private DecimalFormat df = new DecimalFormat("0.00");
    private DecimalFormat df0 = new DecimalFormat("0");

    private TextView tv_infotable;
    public GestionTable(MainActivity mainActivity, ServiceBase sb) {
        Dialog tDialog = new Dialog(mainActivity);
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.list_table, null);

        LinearLayout llListTable = dialogView.findViewById(R.id.LLTables);
        //View linearLayout =  findViewById(R.id.info);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.info);
        ls_table  = sb.getListTable();

        tv_infotable = mainActivity.findViewById(R.id.tvInfotable);

        for(int i = 0 ; i < ls_table.size(); i++){
            Button valueBtn = new Button(mainActivity);
            valueBtn.setText(ls_table.get(i));
            valueBtn.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 2, 0, 2);
            valueBtn.setLayoutParams(params);
            //valueBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            valueBtn.setTextSize(28);
            valueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // click table in list
                    Double n_table_ = Double.valueOf(valueBtn.getText().toString());
                    tv_infotable.setText("Ticket table " + n_table_);
                    sb.setTable_en_cours("Ticket table " + n_table_);
                    Button btnDirect = mainActivity.findViewById(R.id.btnDirect);
                    btnDirect.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    tDialog.dismiss();
                }
            });
            llListTable.addView(valueBtn);
        }


        final int NUM_ROWS_TICKET = 4;
        final int NUM_COLS_TICKET = 3;

        Button btn_nouvelletable = (Button) dialogView.findViewById(R.id.btn_new_table);
        btn_nouvelletable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tDialog.cancel();
                Dialog tDialogNewTable = new Dialog(mainActivity);
                LayoutInflater inflater1 = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
                View dialogViewNewTable = inflater1.inflate(R.layout.clavier_pour_saisie_tables, null);

                TableLayout table_clavier = (TableLayout) dialogViewNewTable.findViewById(R.id.TableLayoutNouvelleTable);
                final TextView affichage = (TextView) dialogViewNewTable.findViewById(R.id.TextViewNouvelleTable);
                int cpt = 0;

                for(int row = 0; row < NUM_ROWS_TICKET; row++){
                    TableRow tableRow  = new TableRow(dialogView.getContext());

                    //tableRow.setPadding(0,0,0,0);
                    tableRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT,
                            1.0f));

                    //tableTicket.setBackgroundColor(Color.BLUE);
                    table_clavier.addView(tableRow);

                    for(int col = 0; col < NUM_COLS_TICKET; col++){
                        final int FINAL_COL = col;
                        final int FINAL_ROW = row;

                        final Button button = new Button(dialogView.getContext());
                        button.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1.0f));

                        //button.setBackground(context.getDrawable(R.drawable.layout_border_clavier_tables));
                        button.setTextColor(Color.BLACK);
                        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                        switch (cpt){
                            case 0 : button.setText("1") ;
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "1";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 1 : button.setText("2");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "2";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 2 : button.setText("3");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "3";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });

                                break;

                            case 3 : button.setText("4");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "4";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 4 : button.setText("5");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "5";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 5 : button.setText("6");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "6";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 6 : button.setText("7");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "7";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 7 : button.setText("8");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "8";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;
                            case 8 : button.setText("9");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "9";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;

                            case 9 : button.setText("C");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        str_numero="";
                                        affichage.setText(str_numero);
                                        VirguleCheck=false;
                                        ApresVirgule=0;
                                        numero = 0;
                                    }
                                });
                                break;
                            case 10 : button.setText("0");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(str_numero.length() < SizeMaxStrMonnaie) {
                                            if(VirguleCheck) ApresVirgule++;
                                            if(ApresVirgule < 3) {
                                                str_numero += "0";
                                                affichage.setText(str_numero);
                                                numero = Double.valueOf(str_numero);
                                            }
                                        }
                                    }
                                });
                                break;

                            case 11 : button.setText(".");
                                button.setTextSize(24);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(str_numero.length() < SizeMaxStrMonnaie-1) {
                                            if (!VirguleCheck) {
                                                if (str_numero.equals("")) {
                                                    str_numero += "0";
                                                }
                                                str_numero += ".";
                                                affichage.setText(str_numero);
                                            }
                                            VirguleCheck = true;
                                        }
                                    }
                                });
                                break;

                        }
                        cpt++;
                        tableRow.addView(button);
                    }
                }

                Button btn_valid_new_table = dialogViewNewTable.findViewById(R.id.btnValidationNouvelleTable);
                btn_valid_new_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //ouvertureTicket(str_numero);
                        if(str_numero.length()>0){
                            if(Double.valueOf(str_numero)>=0){
                                Toast.makeText(mainActivity, "add table " + str_numero, Toast.LENGTH_SHORT).show();
                                Double n_table = Double.valueOf(str_numero);
                                tv_infotable.setText("Ticket table " + n_table);
                                sb.setTable_en_cours("Ticket table " + n_table);
                                sb.nouveauTicket(n_table);
                                tDialogNewTable.cancel();
                                str_numero = "";
                                ApresVirgule = 0;
                                VirguleCheck = false;
                                Button btnDirect = mainActivity.findViewById(R.id.btnDirect);
                                btnDirect.setBackgroundColor(Color.parseColor("#FF6200EE"));

                            }
                        }else{
                            Toast.makeText(mainActivity, "no table " + str_numero, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                tDialogNewTable.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        str_numero = "";
                        ApresVirgule = 0;
                        VirguleCheck = false;
                    }
                });

                tDialogNewTable.setContentView(dialogViewNewTable);
                tDialogNewTable.show();
            }
        });
        tDialog.setContentView(dialogView);
        tDialog.show();



    }

}
