package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/* grammage a rajouter en optionel
* tva spéciale pour dissocier de la tva de la catégorie */

public class GestionProduit {
    private int cptOption = 0;

    private List<Spinner> l_spinner;

    private List<String> l_option;

    private LinearLayout llAddOption;
    public GestionProduit(MainActivity mainActivity, ServiceBase sb, int indexbtn, String current_category, String value) {

        AlertDialog diag = new AlertDialog.Builder(mainActivity)
                .setTitle("Modification produit")
                .setView(R.layout.saisie_produit)
                .create();
        diag.show();
        //diag.getWindow().setLayout(500, 350);

        Button btn_valider = diag.findViewById(R.id.btnValid);

        Button btn_suppression = diag.findViewById(R.id.btnSuppr);

        EditText et_nom = diag.findViewById(R.id.etNom);
        EditText et_prix = diag.findViewById(R.id.etPrix);
        String nom = et_nom.getText().toString();
        if(nom.equals("Nom")) diag.setTitle("Création produit");
        if(value.length()>0){
            String value_nom = value.split("\n")[0];
            et_nom.setText(value_nom);
            String value_prix = value.split("\n")[1].split("\\s+")[0];
            et_prix.setText(value_prix);
        }

        llAddOption = diag.findViewById(R.id.LLaddOption);
        l_option = new ArrayList<String>();
        l_option.add("no option");
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = et_nom.getText().toString();
                l_option = new ArrayList<String>();
                for(int i = 0; i < llAddOption.getChildCount();i++){
                    //Toast.makeText(mainActivity, i+ " int: "+llAddOption.getChildAt(i).getVisibility(), Toast.LENGTH_SHORT).show();
                    if(llAddOption.getChildAt(i).getVisibility()==View.VISIBLE){
                        l_option.add(l_spinner.get(i).getSelectedItem().toString());
                    }
                }
                if(nom.length()>0){
                    Double prix;
                    try
                    {
                        prix = Double.parseDouble(et_prix.getText().toString());
                        Produit p = new Produit();
                        p.setNom(nom);
                        p.setIndex(indexbtn+1);
                        p.setCouleur("blue");
                        p.setCategorie(sb.getServiceBaseCategorie().getCurrent_categorie());
                        p.setPrix(prix);
                        p.setOption(l_option);
                        sb.modifierProduit(p, indexbtn);

                    }
                    catch(NumberFormatException e)
                    {
                        Toast.makeText(mainActivity, "erreur prix" , Toast.LENGTH_SHORT).show();
                    }
                }
                diag.dismiss();
            }
        });

        btn_suppression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mainActivity)
                        .setTitle("Supprimer")
                        .setMessage("Êtes-vous sur de vouloir supprimer ce produit ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sb.supprimerProduit(sb.getServiceBaseCategorie().getCurrent_categorie(),indexbtn);
                                diag.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        if(et_nom.getText().toString().equalsIgnoreCase("nom"))btn_suppression.setVisibility(View.GONE);
        
        LinearLayout llbtnAdd = diag.findViewById(R.id.llbtnAdd);
        ImageButton btnAddOption = diag.findViewById(R.id.btnAddOption);

        Produit p = sb.getProduit(et_nom.getText().toString(), sb.getServiceBaseCategorie().getCurrent_categorie());
        l_spinner = new ArrayList<Spinner>();


        for(int i = 0; i < p.getOption().size(); i++){
            cptOption++;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 2, 0, 2);

            LinearLayout llOption = new LinearLayout(mainActivity);
            llOption.setLayoutParams(params);
            llOption.setOrientation(LinearLayout.HORIZONTAL);

            List<String> l_option = new ArrayList<String>();

            ImageButton btnCancelOption = new ImageButton(mainActivity);
            btnCancelOption.setImageResource(R.drawable.baseline_cancel_24);
            btnCancelOption.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams paramsBtnCancelOption = new LinearLayout.LayoutParams(
                    40,
                    40);

            paramsBtnCancelOption.gravity = Gravity.CENTER;

            btnCancelOption.setLayoutParams(paramsBtnCancelOption);
            btnCancelOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cptOption--;
                    llbtnAdd.setVisibility(View.VISIBLE);
                    LinearLayout ll = (LinearLayout)btnCancelOption.getParent();
                    ll.setVisibility(View.GONE);

                }
            });
            llOption.addView(btnCancelOption);

            String[][] option = sb.getOption_produit();
            l_option.add("aucun");
            for(int j = 0; j < option.length; j++){
                l_option.add(option[j][0]);
            }
            Spinner spinOption = new Spinner(mainActivity);
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(mainActivity.getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, l_option);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

            spinOption.setAdapter(adapter);
            //spinOption.setSelection(p.getOption().get(i));
            for(int k = 0; k < l_option.size();k++){
                if(l_option.get(k).equals(p.getOption().get(i))){
                    spinOption.setSelection(k);
                }
            }
            l_spinner.add(spinOption);

            llOption.addView(spinOption);
            llAddOption.addView(llOption);

            if(cptOption==4) llbtnAdd.setVisibility(View.GONE);
        }



        llbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cptOption++;
                if(cptOption<5){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(5, 2, 0, 2);

                    LinearLayout llOption = new LinearLayout(mainActivity);
                    llOption.setLayoutParams(params);
                    llOption.setOrientation(LinearLayout.HORIZONTAL);

                    List<String> l_option = new ArrayList<String>();

                    ImageButton btnCancelOption = new ImageButton(mainActivity);
                    btnCancelOption.setImageResource(R.drawable.baseline_cancel_24);
                    btnCancelOption.setBackgroundColor(Color.TRANSPARENT);
                    LinearLayout.LayoutParams paramsBtnCancelOption = new LinearLayout.LayoutParams(
                            40,
                            40);

                    paramsBtnCancelOption.gravity = Gravity.CENTER;

                    btnCancelOption.setLayoutParams(paramsBtnCancelOption);
                    btnCancelOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cptOption--;
                            llbtnAdd.setVisibility(View.VISIBLE);
                            LinearLayout ll = (LinearLayout)btnCancelOption.getParent();
                            ll.setVisibility(View.GONE);

                        }
                    });
                    llOption.addView(btnCancelOption);

                    String[][] option = sb.getOption_produit();
                    l_option.add("aucun");
                    for(int i = 0; i < option.length; i++){
                        l_option.add(option[i][0]);
                    }

                    Spinner spinOption = new Spinner(mainActivity);
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(mainActivity.getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, l_option);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                    spinOption.setAdapter(adapter);
                    l_spinner.add(spinOption);

                    llOption.addView(spinOption);
                    llAddOption.addView(llOption);
                    if(cptOption==4) llbtnAdd.setVisibility(View.GONE);
                }
            }
        });


    }
}
