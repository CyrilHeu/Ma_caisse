package com.example.myapplication;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceBaseCategorie {

    private ServiceBase sb;
    private MainActivity mainActivity;
    private FirebaseFirestore db;
    private String TAG = "12345678";
    private LinearLayout llCategorie;
    private ArrayList<Categorie> list_c;
    private ArrayList<Categorie> old_list_c;

    private boolean suppression_en_cours = false;

    private int cpt_nb_suppression = 0;
    private String current_categorie;
    private Categorie current_categorie_;
    private ArrayList<Produit> list_p;
    public ServiceBaseCategorie(ServiceBase _sb, MainActivity _mactivity, FirebaseFirestore _db){
        sb = _sb;
        db = _db;
        mainActivity = _mactivity;
        llCategorie = mainActivity.findViewById(R.id.LLCategorie);
        //list_p = new ArrayList<Produit>();
        initCategories();

    }
    public void setupButtonsCategorie(ArrayList<Categorie> _list_c){
        list_c = _list_c;
        trierIndex(list_c);

        for(Categorie c : list_c) {
            Button btn_cat = Utils.setupBtn("darkblue", mainActivity, c.getNom(), "", 160);

            btn_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectedButton(llCategorie, c.getNom());
                    sb.reloadCategorie();
                }
            });

            btn_cat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog tDialog = new AlertDialog.Builder(mainActivity)
                            .setTitle("Modification catégorie : "+btn_cat.getText().toString())
                            .setView(R.layout.saisie_categorie)
                            .create();
                    tDialog.show();
                    //Dialog tDialog = new Dialog(mainActivity);
                    /*LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.saisie_categorie, null);*/
                    EditText etNom = tDialog.findViewById(R.id.etNom);
                    etNom.setText(c.getNom());
                    EditText etTva = tDialog.findViewById(R.id.etTva);
                    etTva.setText(String.valueOf(c.getTaux()));
                    Button btn_supp = tDialog.findViewById(R.id.btnSuppr);

                    Button btn_enregistrer = tDialog.findViewById(R.id.btnValid);

                    Spinner spinIndex = tDialog.findViewById(R.id.SpinPosition);
                    ArrayList<Integer> l_index = new ArrayList<Integer>();
                    for (int i = 0; i < list_c.size(); i++) {
                        l_index.add(i + 1);
                    }
                    ArrayAdapter<Integer> adapter =
                            new ArrayAdapter<Integer>(mainActivity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, l_index);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinIndex.setAdapter(adapter);
                    spinIndex.setSelection(c.getIndex());

                    String old_nom = etNom.getText().toString();
                    btn_enregistrer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String nom = etNom.getText().toString();
                            Double tva = Double.valueOf(etTva.getText().toString());

                            if (nom.equalsIgnoreCase("nom") == false && nom.length() > 2) {
                                boolean doublon = false;
                                for(int i = 0; i < list_c.size(); i++){
                                    if(nom.equalsIgnoreCase(list_c.get(i).getNom())){
                                        if(i!=c.getIndex()) doublon = true;
                                        i = list_c.size();
                                    }
                                }
                                if(doublon==false){
                                    int index = Integer.valueOf(spinIndex.getSelectedItem().toString()) - 1;
                                    trierIndex(list_c);

                                    boolean trouve = false;
                                    int index_cherche = 0;
                                    for (int i = 0; i < list_c.size(); i++) {
                                        if (list_c.get(i).getIndex() == index) {
                                            trouve = true;
                                            index_cherche = i;
                                        }
                                    }
                                    int indexinitial = c.getIndex();

                                    old_list_c = new ArrayList<Categorie>();
                                    old_list_c = list_c;

                                    if (indexinitial == list_c.size() - 1) {

                                        for (int i = index; i < list_c.size() - 1; i++) {
                                            list_c.get(i).setIndex(list_c.get(i).getIndex() + 1);
                                        }
                                        list_c.get(indexinitial).setIndex(index);
                                        list_c.get(indexinitial).setTaux(tva);
                                        list_c.get(indexinitial).setNom(nom);
                                        trierIndex(list_c);

                                    }
                                    if (indexinitial == 0) {

                                        for (int i = 0; i <= index; i++) {
                                            if (i != indexinitial)
                                                list_c.get(i).setIndex(list_c.get(i).getIndex() - 1);
                                        }
                                        list_c.get(indexinitial).setIndex(index);
                                        list_c.get(indexinitial).setTaux(tva);
                                        list_c.get(indexinitial).setNom(nom);
                                        trierIndex(list_c);

                                    }

                                    if(indexinitial>0 && indexinitial<list_c.size() - 1){
                                        old_list_c = new ArrayList<Categorie>();
                                        old_list_c = list_c;
                                        if(indexinitial==index){
                                            list_c.get(indexinitial).setIndex(index);
                                            list_c.get(indexinitial).setTaux(tva);
                                            list_c.get(indexinitial).setNom(nom);
                                            trierIndex(list_c);


                                        }
                                        if(indexinitial<index){
                                            for (int i = indexinitial; i <= index; i++) {
                                                if (i != indexinitial)
                                                    list_c.get(i).setIndex(list_c.get(i).getIndex() - 1);
                                            }
                                            list_c.get(indexinitial).setIndex(index);
                                            list_c.get(indexinitial).setTaux(tva);
                                            list_c.get(indexinitial).setNom(nom);
                                            trierIndex(list_c);
                                        }
                                        if(indexinitial>index){
                                            for (int i = index; i <= indexinitial; i++) {
                                                if (i != indexinitial)
                                                    list_c.get(i).setIndex(list_c.get(i).getIndex() + 1);
                                            }
                                            list_c.get(indexinitial).setIndex(index);
                                            list_c.get(indexinitial).setTaux(tva);
                                            list_c.get(indexinitial).setNom(nom);
                                            trierIndex(list_c);
                                        }
                                    }


                                    tDialog.dismiss();
                                    llCategorie.removeAllViews();
                                    updateCategorie(list_c, old_nom, nom);
                                    setupButtonsCategorie(list_c);
                                    setupButtonAddCategorie();
                                    setSelectedButton(llCategorie, getCurrent_categorie());
                                }else{
                                    Toast.makeText(mainActivity, "Doublon impossible", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(mainActivity, "Erreur saisie", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    btn_supp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new AlertDialog.Builder(mainActivity)
                                    .setTitle("Supprimer")
                                    .setMessage("Êtes-vous sur de vouloir supprimer "+ old_nom +" ?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection("categorie")
                                                    .whereEqualTo("nom", c.getNom())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                boolean trouve = false;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    trouve = true;
                                                                    Log.d("test12345", document.getId() + " => " + document.getData());
                                                                    //creation ou overide du produit
                                                                    db.collection("categorie").document(document.get("nom").toString())
                                                                            .delete()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    //suppression variable
                                                                                    boolean trouve = false;
                                                                                    int size_list = list_c.size();
                                                                                    for(int i =0; i< size_list;i++){
                                                                                        if(c.getNom().equalsIgnoreCase(list_c.get(i).getNom())){
                                                                                            list_c.remove(i);
                                                                                            size_list = list_c.size();
                                                                                            trouve = true;
                                                                                        }
                                                                                        if(trouve == true && i<list_c.size()){
                                                                                            list_c.get(i).setIndex(list_c.get(i).getIndex()-1);
                                                                                        }

                                                                                    }
                                                                                    // debut update index
                                                                                    if(list_c.size()>0){
                                                                                        for(int i =0; i< list_c.size();i++) {
                                                                                            addCategorie(list_c.get(i), false);
                                                                                            if(i == list_c.size()-1){
                                                                                                addCategorie(list_c.get(i), true);
                                                                                            }
                                                                                        }
                                                                                    }else{
                                                                                        llCategorie.removeAllViews();
                                                                                        sb.cleanlistbtn(true);
                                                                                        setupButtonAddCategorie();
                                                                                    }
                                                                                    if(c.getNom().equalsIgnoreCase(btn_cat.getText().toString())){
                                                                                        if(list_c.size()>0){
                                                                                            setSelectedButton(llCategorie, list_c.get(0).getNom());
                                                                                            sb.reloadCategorie();
                                                                                        }
                                                                                    }
                                                                                    // fin update index
                                                                                    tDialog.dismiss();
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.w(TAG, "Error deleting document", e);
                                                                                }
                                                                            });
                                                                }
                                                            } else {
                                                                Log.d(TAG + "45", "Error getting documents: ", task.getException());
                                                            }
                                                        }
                                                    });
                                        }
                                    })
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    //tDialog.setContentView(dialogView);
                    //tDialog.show();
                    return false;
                }
            });
            llCategorie.addView(btn_cat);
        }
    }
    private void setSelectedButton(LinearLayout llCategorie, String nom) {

        for(int i = 0; i < llCategorie.getChildCount()-1; i++){
            Button b = (Button) llCategorie.getChildAt(i);
            if(b.getText().toString().equalsIgnoreCase(nom)){
                b.setBackgroundColor(Color.BLACK);
                current_categorie = nom;
            }else{
                b.setBackgroundColor(Color.parseColor("#FF6200EE"));
            }
        }
    }
    public ArrayList<Categorie> getList_c() {
        return list_c;
    }
    private int cpt = 0;
    private void updateCategorie(ArrayList<Categorie> list_c, String oldcaterogie, String current_categorie){

            db.collection("categorie")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean trouve = false;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    trouve = true;
                                    Log.d("test12345", document.getId() + " => " + document.getData());
                                    //creation ou overide du produit
                                    boolean existant = false;
                                    for(int j = 0; j < list_c.size(); j++){
                                        if(list_c.get(j).getNom().equalsIgnoreCase(document.get("nom").toString())){
                                            existant = true;
                                        }
                                    }
                                    if(existant==false){
                                        Categorie c = new Categorie();
                                        c.setNom((document.get("nom").toString()));
                                        supprimerCategorie(c);
                                    }
                                }

                                if(getCurrent_categorie().equalsIgnoreCase(oldcaterogie)){
                                    setSelectedButton(llCategorie,current_categorie);
                                }

                                for(int i = 0; i < list_c.size(); i++){

                                    Map<String, Object> p = new HashMap<>();
                                    p.put("nom", list_c.get(i).getNom());
                                    p.put("tva", list_c.get(i).getTaux());
                                    p.put("index", list_c.get(i).getIndex());

                                    db.collection("categorie").document(list_c.get(i).getNom())
                                            .set(p)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //
                                                    cpt++;
                                                    if(cpt == list_c.size()){
                                                        //Log.d("12.3456","update les categories");
                                                        updateProduitCategorie(list_p, oldcaterogie, current_categorie);
                                                        cpt = 0;

                                                    }
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG + "45", "Error getting documents: ", task.getException());
                            }
                        }
                    });

    }
    public void updateProduitCategorie(ArrayList<Produit> _list_p, String oldcaterogie, String current_category){

        for(int i = 0; i < list_p.size(); i++){
            //Produit p1 = list_p.get(i);
            if(list_p.get(i).getCategorie().equalsIgnoreCase(oldcaterogie)){
                list_p.get(i).setCategorie(current_category);
                Map<String, Object> p = new HashMap<>();
                p.put("nom", list_p.get(i).getNom());
                p.put("couleur", list_p.get(i).getCouleur());
                p.put("prix", list_p.get(i).getPrix());
                p.put("categorie", current_category);
                p.put("index", list_p.get(i).getIndex());
                p.put("options", list_p.get(i).getOption());

                db.collection("produit").document(list_p.get(i).getNom())
                        .set(p)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }
        }
        sb.setList_p(list_p);
        sb.reloadCategorie();

    }
    private void trierIndex(ArrayList<Categorie> list_c) {
        Collections.sort(list_c, new Comparator<Categorie>() {
            public int compare(Categorie o1, Categorie o2) {
                return Double.valueOf(o1.getIndex()).compareTo(Double.valueOf(o2.getIndex()));
            }
        });
    }
    public void initCategories() {
        ArrayList<Categorie> list_c = new ArrayList<>();
        list_p = new ArrayList<Produit>();
        db.collection("categorie")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                // parser l'objet
                                Categorie c = new Categorie();
                                c.setNom(document.get("nom").toString());
                                c.setIndex(Integer.valueOf(document.get("index").toString()));
                                c.setTaux(Double.valueOf(document.get("tva").toString()));
                                list_c.add(c);

                            }
                            if(list_c.size()==0){
                                sb.cleanlistbtn(true);
                            }

                            setupButtonsCategorie(list_c);
                            setupButtonAddCategorie();
                            if(list_c.size()>0){
                                setSelectedButton(llCategorie, list_c.get(0).getNom());
                                current_categorie = list_c.get(0).getNom();

                            }
                            /* init initial de la categorie de base au lancement */
                            db.collection("produit")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                                    // parser l'objet
                                                    Produit p = new Produit();
                                                    p.setNom(document.get("nom").toString());
                                                    p.setCategorie(document.get("categorie").toString());
                                                    p.setIndex(Integer.valueOf(document.get("index").toString()));
                                                    p.setPrix(Double.valueOf(document.get("prix").toString()));
                                                    p.setCouleur(document.get("couleur").toString());
                                                    p.setOption((List<String>)document.get("options"));
                                                    list_p.add(p);
                                                }
                                                sb.setList_p(list_p);
                                                sb.reloadCategorie();

                                                if(list_c.size()==0){
                                                    sb.cleanlistbtn(true);
                                                }
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }
                                    });

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    private void setupButtonAddCategorie() {
        Button btn_add_cat = Utils.setupBtn("darkblue", mainActivity, "+", "", 50);
        llCategorie.addView(btn_add_cat);
        btn_add_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(list_c, new Comparator<Categorie>() {
                    public int compare(Categorie o1, Categorie o2) {
                        return Double.valueOf(o1.getIndex()).compareTo(Double.valueOf(o2.getIndex()));
                    }
                });
                //Dialog tDialog = new Dialog(mainActivity);
                AlertDialog tDialog = new AlertDialog.Builder(mainActivity)
                        .setTitle("Ajout catégorie")
                        .setView(R.layout.saisie_categorie)
                        .create();
                tDialog.show();
               /* LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.saisie_categorie, null);*/

                EditText etNom = tDialog.findViewById(R.id.etNom);
                EditText etTva = tDialog.findViewById(R.id.etTva);
                Button btn_supp = tDialog.findViewById(R.id.btnSuppr);
                btn_supp.setVisibility(View.GONE);
                Button btn_enregistrer = tDialog.findViewById(R.id.btnValid);

                Spinner spinIndex = tDialog.findViewById(R.id.SpinPosition);
                ArrayList<Integer> l_index = new ArrayList<Integer>();
                for(int i = 0 ; i  < llCategorie.getChildCount(); i++){
                    l_index.add(i+1);
                }
                ArrayAdapter<Integer> adapter =
                        new ArrayAdapter<Integer>(mainActivity.getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item,l_index);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                spinIndex.setAdapter(adapter);
                spinIndex.setSelection(l_index.size()-1);

                btn_enregistrer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nom = etNom.getText().toString();
                        Double tva = 0.0;
                        Boolean doublon = false;
                        int sizelist = list_c.size();
                        if(nom.equalsIgnoreCase("nom")==false && nom.length() > 2){
                            for(int j = 0; j < list_c.size(); j++) {
                                if (list_c.get(j).getNom().equalsIgnoreCase(nom)) {
                                    doublon = true;
                                    Toast.makeText(mainActivity, "Doublon impossible", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(doublon == false){

                                    try {
                                        tva = Double.parseDouble(etTva.getText().toString());
                                        Categorie c_ = new Categorie();
                                        c_.setNom(nom);
                                        c_.setTaux(tva);
                                        int index = Integer.valueOf(spinIndex.getSelectedItem().toString()) - 1;
                                        c_.setIndex(index);
                                        // tri , llCategorie, list_c
                                        boolean trouve = false;
                                        for (Categorie c_modif : list_c) {
                                            if (c_modif.getIndex() == index) {
                                                trouve = true;
                                            }
                                            if (trouve == true) {
                                                c_modif.setIndex(c_modif.getIndex() + 1);
                                                addCategorie(c_modif, false);
                                            }
                                        }
                                        addCategorie(c_, false);
                                        list_c.add(c_);

                                        Collections.sort(list_c, new Comparator<Categorie>() {
                                            public int compare(Categorie o1, Categorie o2) {
                                                return Double.valueOf(o1.getIndex()).compareTo(Double.valueOf(o2.getIndex()));
                                            }
                                        });

                                        tDialog.dismiss();
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(mainActivity, "erreur saisie TVA", Toast.LENGTH_SHORT).show();
                                    }

                                    llCategorie.removeAllViews();
                                    setupButtonsCategorie(list_c);
                                    setupButtonAddCategorie();
                                    if(sizelist>0){
                                        setSelectedButton(llCategorie,getCurrent_categorie());
                                    }else{
                                        setSelectedButton(llCategorie,nom);
                                    }
                                    sb.reloadCategorie();

                            }else{
                                Toast.makeText(mainActivity, "Doublon impossible", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mainActivity, "Erreur saisie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //tDialog.setContentView(dialogView);
                //tDialog.show();
            }
        });
    }
    public void supprimerCategorie(Categorie c) {
        db.collection("categorie")
                .whereEqualTo("nom", c.getNom())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean trouve = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                trouve = true;
                                Log.d("test12345", document.getId() + " => " + document.getData());
                                //creation ou overide du produit
                                db.collection("categorie").document(document.get("nom").toString())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                /*for(Categorie c_ : list_c){
                                                    if(c_.getNom().equalsIgnoreCase(c.getNom())){
                                                        list_c.remove(c);
                                                    }
                                                }*/
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }

                        } else {
                            Log.d(TAG + "45", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void addCategorie(Categorie c1, boolean b){

        //CollectionReference produit_ref = db.collection("produit");
        Map<String, Object> p = new HashMap<>();
        p.put("nom", c1.getNom());
        p.put("tva", c1.getTaux());
        p.put("index", c1.getIndex());
        //produit_ref.document(p1.getNom()).set(p);

        db.collection("categorie").document(c1.getNom())
                .set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        //refreshListCategorie(c1,list_c);
                        if(b==true){
                            llCategorie.removeAllViews();
                            if(list_c.size()>0){
                                setupButtonsCategorie(list_c);
                                setupButtonAddCategorie();
                                setSelectedButton(llCategorie,getCurrent_categorie());
                            }



                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public String getCurrent_categorie() {
        return current_categorie;
    }


}
