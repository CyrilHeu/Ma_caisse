package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.ui.FeedTicket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ServiceBase {

    private String TAG = "123";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Produit> list_p = new ArrayList<Produit>();
    private ArrayList<Categorie> list_c;
    private ArrayList<Button> list_btn_produit;
    private static MainActivity mainActivity;
    private boolean healthcheck = false;
    private ProgressDialog perte_co_dialog ;
    private boolean b_perte_co_dialog = false;
    private String perte_connexion;
    private String reprise_connexion;
    private Boolean perte_en_cours = false;
    private ArrayList<String> ls_table;
    private ArrayList<Ticket> ls_ticket;
    private ImageButton btnConnected;

    private ProgressDialog init_dialog ;
    private boolean b_init = false;
    private Boolean b_init_table = false;
    private String table_en_cours;
    private static boolean isSynchro = false;
    private static boolean b_dialog_synchro = false;
    private ProgressDialog perte_synchro_dialog ;

    private static ProgressDialog erreur_serveur_synchro ;
    private static Long diff = (long)0;
    private static String heure_desynchro_notif = "0";

    private static boolean b_onfailure = false;
    private FeedTicket feedTicket;
    private boolean b_connect = false;

    private ServiceBaseAuth sba;

    private ProgressDialog dialog_;

    private String[][] option_produit;

    private Ticket ticket_en_cours;

    private LinearLayout llCategorie ;

    private ArrayList<Button> list_btn_categorie;
    private ServiceBaseCategorie sbc;
    private Drawable d;
    private Boolean b_buttons_init = false;
    private Boolean b_init_produits = false;
    private boolean mesureScrollProduit = false;
    private boolean isMesureDone =false;
    private long init_timestamp;
    private boolean large_list = false;
    private boolean b_params = false;
    public MyParams myParams;

    private  CountDownTimer timerRefreshTicket;
    public ServiceBase(ArrayList<Button> list_button_produit, LinearLayout llButtons, MainActivity _mainActivity, ImageButton _btnConnected, Ticket _ticket_en_cours, LinearLayout _llCategorie, ArrayList<Button> _list_btn_categorie, Drawable _d) {
        mainActivity = _mainActivity;
        btnConnected = _btnConnected;
        ticket_en_cours = _ticket_en_cours;
        llCategorie = _llCategorie;
        list_btn_categorie = new ArrayList<Button>();

        dialog_ = new ProgressDialog(mainActivity);
        dialog_.setMessage("Connexion en cours...");
        dialog_.setCancelable(false);
        dialog_.show();
        //list_p = getAllProduit();
        d = _d;
        myParams = new MyParams(db);

        initButtons(llButtons, list_button_produit);
        initAllCategorie();

        sba = new ServiceBaseAuth();
        sba.authUser("heurtauxcyril@gmail.com","firebase1234!", _mainActivity);
        db = FirebaseFirestore.getInstance();


        CountDownTimer timer;
        timer = new CountDownTimer(4000, 250) {
            @Override
            public void onTick(long l) {
                if(sba.getUser()!=null && b_buttons_init /*&& myParams.b_init_params*/){

                    notifyInitConnexion();
                    checkHealth();
                    checkSynchroTime();

                    refreshListTicket();
                    initProduits();
                    list_btn_produit = list_button_produit;
                    initOptionProduit();
                    feedTicket = new FeedTicket(mainActivity, l_detail, ServiceBase.this);
                    refreshTicketUI(5);
                    initButtons(llButtons, list_button_produit);
                    //initAllCategorie(); // étonnament cette ligne a été supprimée puis remise puis en fait elle semble obsolete
                    cancel();
                    b_connect = true;


                }
            }

            @Override
            public void onFinish() {
                try{
                    if(b_connect == true){
                        cancel();

                    }else{
                        start();
                    }
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();


        //Encaissement encaissement = new Encaissement(this.mainActivity);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initButtons(LinearLayout llButtons, ArrayList<Button> list_button_produit) {

        /*CountDownTimer timer;
        timer = new CountDownTimer(4000, 250) {
            @Override
            public void onTick(long l) {
               if(myParams.b_init_params){*/
                   for(int h = 0; h < 6; h++){
                       LinearLayout llLigneButton = new LinearLayout(mainActivity);
                       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                               LinearLayout.LayoutParams.MATCH_PARENT,
                               LinearLayout.LayoutParams.WRAP_CONTENT
                       );
                       params.setMargins(5, 2, 0, 2);
                       llLigneButton.setLayoutParams(params);

                       for(int i = 0; i < 4; i++){
                           Button btn = Utils.setupBtnProduit("darkblue", mainActivity,"", "", 140);
                           btn.setText("");
                           list_button_produit.add(btn);
                           llLigneButton.addView(btn);
                       }
                       llButtons.addView(llLigneButton);
                   }

                   ScrollView svButtons = mainActivity.findViewById(R.id.SVbuttons);

                   svButtons.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Log.d("1234567", "clicked");
                       }
                   });

                svButtons.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View view, MotionEvent motionEvent) {
                           int y_max = svButtons.getChildAt(0).getHeight()-view.getHeight();

                           int init_position = svButtons.getScrollY();
                           if(init_position==y_max){
                               if(mesureScrollProduit==false){
                                   init_timestamp = Utils.timestamp_milliseconde();
                                   mesureScrollProduit = true;
                        /*Log.d("1234567",
                                "mise a true et init timestam ="+init_timestamp);*/
                               }else{
                                   if(init_timestamp+1000<Utils.timestamp_milliseconde() && isMesureDone == false){
                                       Log.d("1234567",
                                               "heyo");
                                       isMesureDone = true;
                                       new AlertDialog.Builder(mainActivity)
                                               .setTitle("Dernière ligne")
                                               .setMessage("Voulez vous ajouter ou supprimer ?")

                                               // Specifying a listener allows you to take an action before dismissing the dialog.
                                               // The dialog is automatically dismissed when a dialog button is clicked.
                                               .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       /*LinearLayout llLigneButton = new LinearLayout(mainActivity);
                                                       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                               LinearLayout.LayoutParams.MATCH_PARENT,
                                                               LinearLayout.LayoutParams.WRAP_CONTENT
                                                       );
                                                       params.setMargins(5, 2, 0, 2);
                                                       llLigneButton.setLayoutParams(params);
                                                       myParams.add_one_line_buttons();*/

                                                   }
                                               })

                                               // A null listener allows the button to dismiss the dialog and take no further action.
                                               .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int which) {

                                                   }
                                               })

                                               .setIcon(android.R.drawable.ic_dialog_info)
                                               .show();
                                   }
                               }
                           }else{
                             /*Log.d("1234567",
                            "mise à false");*/
                               mesureScrollProduit = false;
                               isMesureDone = false;
                           }

                           if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                               mesureScrollProduit = false;
                               isMesureDone = false;
                    /*Log.d("1234567",
                            " end of scrolling movement");*/
                           }

                           return false;
                       }
                   });
                  b_buttons_init = true;
                   /* cancel();
               }
            }

            @Override
            public void onFinish() {
                start();
            }
        }.start();*/
    }

    private void initProduits() {
        CountDownTimer timer;
        timer = new CountDownTimer(4000, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(b_init_produits==true){

                    cancel();
                }
            }
            @Override
            public void onFinish() {
                try{
                    start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }
    private void checkHealth() {
        CountDownTimer timer;
        timer = new CountDownTimer(4000, 250) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.e(TAG, "tick healthcheck");
                DocumentReference docRef = db.collection("api").document("healthcheck");
                Source source = Source.SERVER;
                docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, "Server document data: " + document.getData());
                            if(b_perte_co_dialog == true){
                                perte_co_dialog.dismiss();
                                // reprise connexion
                                reprise_connexion = Utils.dateheure();

                                if(perte_en_cours==true){
                                    perte_en_cours = false;
                                    notifyConnexionState(perte_connexion, reprise_connexion);
                                }
                            }
                            btnConnected.setBackgroundColor(Color.GREEN);

                        } else {
                            Log.d(TAG, "Server get failed: ", task.getException());
                            if(b_perte_co_dialog==false) {
                                perte_co_dialog = ProgressDialog.show(mainActivity, "",
                                        "Problème de connexion...", true);
                                b_perte_co_dialog = true;

                                btnConnected.setBackgroundColor(Color.RED);

                                //perte connexion
                                if(perte_en_cours == false){
                                    perte_connexion = Utils.dateheure();
                                    perte_en_cours = true;
                                }
                            }
                        }
                    }
                });
            }
            @Override
            public void onFinish() {
                try{
                    start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }
    public void setTable_en_cours(String table_en_cours) {
        this.table_en_cours = table_en_cours;
    }
    public String getTable_en_cours() {
        return table_en_cours;
    }
    public void addProduit(Produit p1){

        //CollectionReference produit_ref = db.collection("produit");
        Map<String, Object> p = new HashMap<>();
        p.put("nom", p1.getNom());
        p.put("couleur", p1.getCouleur());
        p.put("prix", p1.getPrix());
        p.put("categorie", p1.getCategorie());
        p.put("index", p1.getIndex());
        p.put("options", p1.getOption());
        //produit_ref.document(p1.getNom()).set(p);

        db.collection("produit").document(p1.getNom())
                .set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        refreshListProduit(p1,list_p);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    private void refreshListProduit(Produit p1, ArrayList<Produit> listp) {
        boolean trouve = false;
        //Toast.makeText(mainActivity, "refresh de la list p size = " + listp.size(), Toast.LENGTH_SHORT).show();
        for(int i = 0; i < listp.size(); i++){
            if(listp.get(i).getIndex()==p1.getIndex() && listp.get(i).getCategorie().equals(p1.getCategorie())){
                trouve = true;
                listp.set(i, p1);
                reloadCategorie();
            }
        }
        if(trouve == false){
                listp.add(p1);
                reloadCategorie();
        }
        //Toast.makeText(mainActivity, "refresh de la list p", Toast.LENGTH_SHORT).show();
    }
    public Produit getProduit(String nom, String category){
        for(int i = 0; i < list_p.size();i++){
            if(list_p.get(i).getNom().equalsIgnoreCase(nom) && list_p.get(i).getCategorie().equalsIgnoreCase(category)){
                return list_p.get(i);
            }
        }
        return new Produit();
    }
    public void setList_p(ArrayList<Produit> list_p) {
        this.list_p = list_p;
    }

    public ArrayList<Produit> getAllProduit(){
        ArrayList<Produit> list_p = new ArrayList<>();
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
                            reloadCategorie();
                            b_init_produits = true;
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return list_p;
    }
    public void initAllCategorie(){
        sbc = new ServiceBaseCategorie(this, mainActivity, db);
    }
    public ServiceBaseCategorie getServiceBaseCategorie(){
        return sbc;
    }
    private void notifyConnexionState(String perte, String reprise) {
        Map<String, Object> p = new HashMap<>();
        p.put("Perte connexion", perte);
        p.put("Reprise connexion", reprise);

        db.collection("notification").document("healthcheck")
                .collection("healthcheck_notification").document(perte)
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
                });
    }
    private void notifyInitConnexion() {

        dialog_.setMessage("Initialisation en cours...");

        Map<String, Object> p = new HashMap<>();
        p.put("Init connexion", Utils.dateheure());;

        db.collection("notification").document("init").collection("init_notification").document(Utils.dateheure())
                .set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        dialog_.dismiss();
                        b_init = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void cleanlistbtn(boolean b) {
        if(list_btn_produit!=null){
            for(Button c : list_btn_produit){
                c.setText("");
            }
            if(b==true){
                for(Button c : list_btn_produit){
                    c.setEnabled(false);
                }
            }else{
                for(Button c : list_btn_produit){
                    c.setEnabled(true);
                }
            }
        }

    }
    public void clickProduit(String value, String etat, String color_etat, String categorie, String commentaire) {

        if(value!=null || value.length()>0){
            String nom = value.split("\n")[0];
            Produit p1 = getProduit(nom, categorie);


            if(p1.getOption().size()>0){

                List<RadioButton> l_radioButton = new ArrayList<RadioButton>();
                List<CheckBox> l_checkBox = new ArrayList<CheckBox>();

                AlertDialog diag = new AlertDialog.Builder(mainActivity)
                        .setTitle("Saisie :")
                        .setView(R.layout.saisie_option)

                        .create();
                diag.show();

                LinearLayout llOptionSaisie = diag.findViewById(R.id.LLsaisieOption);
                FrameLayout.LayoutParams paramLLoptions = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, 380
                );

                paramLLoptions.setMargins(2,0,0,0);
                llOptionSaisie.setLayoutParams(paramLLoptions);

                for(int i = 0; i < p1.getOption().size(); i++){
                    TextView tvTitre = new TextView(mainActivity);
                    tvTitre.setTextSize(18);
                    FrameLayout.LayoutParams paramTvTitre = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    paramTvTitre.setMargins(5, 0, 0, 0);
                    tvTitre.setLayoutParams(paramTvTitre);
                    String optionTitre = p1.getOption().get(i).substring(0, 1).toUpperCase() + p1.getOption().get(i).substring(1).toLowerCase();
                    tvTitre.setText(optionTitre+ " :");
                    tvTitre.setTypeface(Typeface.DEFAULT_BOLD);
                    llOptionSaisie.addView(tvTitre);

                    RadioGroup rg = new RadioGroup(mainActivity);
                    for(int j = 0; j < option_produit.length; j++){
                        LinearLayout llLigneOption = new LinearLayout(mainActivity);
                        FrameLayout.LayoutParams paramLigneOption = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                        );
                        paramLigneOption.setMargins(8, 0, 0, 5);
                        llLigneOption.setLayoutParams(paramLigneOption);
                        llLigneOption.setOrientation(LinearLayout.VERTICAL);
                        llOptionSaisie.addView(llLigneOption);
                        if(option_produit[j][0].equalsIgnoreCase(p1.getOption().get(i))){

                            for(int k = 2; k < option_produit[j].length;k++){
                                if(option_produit[j][k]!=null) {
                                    String type = option_produit[j][1];

                                    if(type.equals("multiple")){
                                        CheckBox checkBoxOption = new CheckBox(mainActivity);
                                        String optionProduit = option_produit[j][k].substring(0, 1).toUpperCase() + option_produit[j][k].substring(1).toLowerCase();
                                        checkBoxOption.setText(optionProduit);
                                        llLigneOption.addView(checkBoxOption);
                                        l_checkBox.add(checkBoxOption);
                                    }
                                    if(type.equals("simple")){
                                        RadioButton radioButtonOption = new RadioButton(mainActivity);
                                        String optionProduit = option_produit[j][k].substring(0, 1).toUpperCase() + option_produit[j][k].substring(1).toLowerCase();
                                        radioButtonOption.setText(optionProduit);
                                        l_radioButton.add(radioButtonOption);
                                        rg.addView(radioButtonOption);
                                    }
                                    // checkbox or simplebox suivant choix de l'option multi ou pas
                                }
                            }
                            llLigneOption.addView(rg);

                        }

                    }
                }
                Button btnValidOption = diag.findViewById(R.id.btnValidOption);
                btnValidOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        List<String> options_choisies = new ArrayList<String>();
                        for(int i = 0 ; i < l_radioButton.size(); i++){
                            if(l_radioButton.get(i).isChecked()){
                                options_choisies.add(l_radioButton.get(i).getText().toString());
                            }
                        }
                        for(int i = 0; i < l_checkBox.size();i++){
                            if(l_checkBox.get(i).isChecked()){
                                options_choisies.add(l_checkBox.get(i).getText().toString());
                            }
                        }
                        if(options_choisies.size()==0) options_choisies.add("Pas d'option");

                        Double prix = Double.valueOf(value.split("\n")[1].split("\\s+")[0]);
                        Double tva = 0.0;
                        for(Produit p : list_p){
                            if(p.getNom().equals(nom)){
                                for(Categorie c : sbc.getList_c()){
                                    if(p.getCategorie().equals(c.getNom())){
                                        tva = c.getTaux();
                                    }

                                }
                            }
                        }
                        Produit_ticket p_ticket = new Produit_ticket(nom, prix, etat, color_etat, Utils.timestamp_milliseconde());
                        Map<String, Object> p = new HashMap<>();
                        p.put("nom", p_ticket.getNom());
                        p.put("prix", p_ticket.getPrix());
                        p.put("etat", p_ticket.getEtat());
                        p.put("paye_separe", false);
                        p.put("colorEtat_initial", p_ticket.getColorEtat());
                        p.put("colorEtat", p_ticket.getColorEtat());
                        p.put("timestamp", p_ticket.getTimestamp());
                        p.put("options", options_choisies);
                        p.put("timestamp_envoi", 0);

                        if(commentaire.length()>0){
                            p.put("commentaire", commentaire);
                        }

                        db.collection("ticket/"+table_en_cours+"/detail").document(p_ticket.getNom()+"_"+Utils.getUUID())
                                .set(p)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        diag.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                    }


                });

            }else{

                Double prix = Double.valueOf(value.split("\n")[1].split("\\s+")[0]);
                Double tva = 0.0;
                for(Produit p : list_p){
                    if(p.getNom().equals(nom)){
                        for(Categorie c : getServiceBaseCategorie().getList_c()){
                            if(p.getCategorie().equals(c.getNom())){
                                tva = c.getTaux();
                            }

                        }
                    }
                }
                Produit_ticket p_ticket = new Produit_ticket(nom, prix, etat, color_etat, Utils.timestamp_milliseconde());
                List<String> option_null = new ArrayList<String>();
                option_null.add("Pas d'option");

                Map<String, Object> p = new HashMap<>();
                p.put("nom", p_ticket.getNom());
                p.put("prix", p_ticket.getPrix());
                p.put("etat", p_ticket.getEtat());
                p.put("paye_separe", false);
                p.put("colorEtat", p_ticket.getColorEtat());
                p.put("colorEtat_initial", p_ticket.getColorEtat());
                p.put("timestamp", p_ticket.getTimestamp());
                p.put("options", option_null);
                p.put("timestamp_envoi", 0);
                if(commentaire.length()>0){
                    p.put("commentaire", commentaire);
                }

                db.collection("ticket/"+table_en_cours+"/detail").document(p_ticket.getNom()+"_"+Utils.getUUID())
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
                        });
            }
        }
    }
    public void nouveauTicket(Double n_table){
        feedTicket.changetable = true;
        Map<String, Object> p = new HashMap<>();

        if(n_table == 777){ //direct
            p.put("table", "direct");
            //p.put("dateheure_ouverture", Utils.dateheure());
            //p.put("infotable", "infotable");
            //p.put("commentaire", "");

            db.collection("ticket").document("Ticket direct")
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
                    });
        }else{
            p.put("table", n_table);
            p.put("dateheure_ouverture", Utils.dateheure());
            p.put("infotable", "infotable");
            p.put("commentaire", "");
            db.collection("ticket").document("Ticket table "+n_table)
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
                    });
        }


    }
    public ArrayList<String> getListTable() {
        ls_table = new ArrayList<>();
        ArrayList<Double> ls_d_table = new ArrayList<>();
        for(int i = 0; i < ls_ticket.size(); i++){
            Double n = ls_ticket.get(i).getTable();
            ls_d_table.add(n);
        }
        Collections.sort(ls_d_table);
        for(int i = 0 ; i < ls_d_table.size(); i++){
            String s_n = String.valueOf(ls_d_table.get(i));
            if(s_n.contains(".0")==true){
                s_n = String.valueOf((int)Math.round(ls_d_table.get(i)));
            }
            ls_table.add(s_n);
        }
        return ls_table;

    }
    public void refreshListTicket(){

        ls_ticket = new ArrayList<>();
        CountDownTimer timer;
        timer = new CountDownTimer(4000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

                db.collection("ticket")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ls_ticket = new ArrayList<>();
                                    if(task.getResult().size()>0){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.get("table").toString().equals("direct")==false){
                                                Ticket t = new Ticket();
                                                t.setTable(Double.valueOf(document.get("table").toString()));
                                                t.setDateheure_ouverture(document.get("dateheure_ouverture").toString());
                                                t.setInfo(document.get("infotable").toString());
                                                t.setCommentaireTable(document.get("commentaire").toString());
                                                ls_ticket.add(t);
                                                Log.w(TAG+"4", String.valueOf(document.getData()), task.getException());
                                            }

                                        }
                                        if(b_init_table==false){ // init table démarrage
                                            b_init_table = true;
                                            TextView tv_infotable = mainActivity.findViewById(R.id.tvInfotable);

                                            if(ls_ticket.size()>0){
                                                String table = ls_ticket.get(0).getTable().toString();

                                                table_en_cours = "Ticket table "+table;
                                                if(ls_ticket.size()>0){
                                                    tv_infotable.setText("Ticket table "+table);

                                                    tv_infotable.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if(!tv_infotable.getText().toString().equals("Ticket direct")) {
                                                                Log.d("testinfotable","infotable");
                                                                Dialog tDialogTableComment = new Dialog(mainActivity);
                                                                LayoutInflater inflater1 = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                                                                View dialogViewTableComment = inflater1.inflate(R.layout.infotable, null);

                                                                EditText editNote;
                                                                Button btnModifier, btnSupprimer;
                                                                TextView tv_infotable_heure_ouverture;

                                                                editNote = dialogViewTableComment.findViewById(R.id.editNoteCommentaireTable);
                                                                btnModifier = dialogViewTableComment.findViewById(R.id.btnValiderCommentaireTable);
                                                                btnSupprimer = dialogViewTableComment.findViewById(R.id.btnSupprimerCommentaireTable);
                                                                tv_infotable_heure_ouverture = dialogViewTableComment.findViewById(R.id.tv_infotable_heure_ouverture);
                                                                //editNote.setHint("Votre note ici...");
                                                                DocumentReference docRef = db.collection("ticket").document(table_en_cours);

                                                                // Récupérer le document
                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult();
                                                                            if (document.exists()) {
                                                                                // Récupérer le champ spécifique
                                                                                String fieldValue = document.getString("commentaire");
                                                                                editNote.setText(fieldValue);
                                                                                fieldValue = document.getString("dateheure_ouverture");
                                                                                tv_infotable_heure_ouverture.setText(tv_infotable_heure_ouverture.getText()+" "+fieldValue);
                                                                                Log.d("Firebase", "Valeur du champ : " + fieldValue);
                                                                            } else {
                                                                                Log.d("Firebase", "Le document n'existe pas.");
                                                                            }
                                                                        } else {
                                                                            Log.d("Firebase", "Erreur : ", task.getException());
                                                                        }
                                                                    }
                                                                });

                                                                editNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                                    private boolean isFirstFocus = true;


                                                                    @Override
                                                                    public void onFocusChange(View v, boolean hasFocus) {
                                                                        if (hasFocus && isFirstFocus) {
                                                                            editNote.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    editNote.selectAll();
                                                                                }
                                                                            });
                                                                            isFirstFocus = false; // Désactive la sélection pour les prochaines fois
                                                                        }
                                                                    }
                                                                });

                                                                btnModifier.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        // Logique pour modifier la note
                                                                        // Ici, vous pouvez ajouter votre code pour sauvegarder la note
                                                                        updateTableComment(tv_infotable.getText().toString(), editNote);
                                                                        tDialogTableComment.dismiss();
                                                                    }
                                                                });

                                                                btnSupprimer.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        editNote.setText(""); // Efface le texte de la note
                                                                        updateTableComment(tv_infotable.getText().toString(), editNote);
                                                                        tDialogTableComment.dismiss();
                                                                    }
                                                                });

                                                                tDialogTableComment.setContentView(dialogViewTableComment);
                                                                tDialogTableComment.show();
                                                            }
                                                        }
                                                    });

                                                }else{
                                                    tv_infotable.setText("Aucune table ouverte");
                                                }
                                            }else{
                                                nouveauTicket((double)777);
                                                String table = "Ticket direct";
                                                table_en_cours = "Ticket direct";
                                                tv_infotable.setText(table_en_cours);
                                                Button btnDirect = mainActivity.findViewById(R.id.btnDirect);
                                                btnDirect.setBackgroundColor(Color.BLACK);

                                            }

                                        }
                                    } else {
                                        //Pas d'heure d'ouverture, a revoir ici pour le direct...
                                        TextView tv_infotable = mainActivity.findViewById(R.id.tvInfotable);
                                        nouveauTicket((double)777);
                                        String table = "Ticket direct";
                                        table_en_cours = "Ticket direct";
                                        tv_infotable.setText(table_en_cours);
                                        Button btnDirect = mainActivity.findViewById(R.id.btnDirect);
                                        btnDirect.setBackgroundColor(Color.BLACK);
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                    }

                            }
                        });
            }
            @Override
            public void onFinish() {
                try{
                    start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();

    }
    public void modifierProduit(Produit p, int indexbtn){

        boolean doublon = false;
        for(int i = 0; i < list_p.size(); i++){
            if(list_p.get(i).getNom().equals(p.getNom())){
                if(list_p.get(i).getCategorie().equals(p.getCategorie())==false ){
                    Toast.makeText(mainActivity, "Doublon impossible", Toast.LENGTH_SHORT).show();
                    doublon = true;
                }
                if(list_p.get(i).getCategorie().equals(p.getCategorie())==true && indexbtn!=list_p.get(i).getIndex()-1){
                    Toast.makeText(mainActivity, "Doublon impossible", Toast.LENGTH_SHORT).show();
                    doublon = true;
                }
                if(list_p.get(i).getCategorie().equals(p.getCategorie())==true && indexbtn==list_p.get(i).getIndex()-1){
                    doublon = false;
                }
            }
        }
        if(doublon == false) {
            db.collection("produit")
                    .whereEqualTo("index", indexbtn + 1).whereEqualTo("categorie", p.getCategorie())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean trouve = false;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    trouve = true;
                                    Log.d(TAG + "45", document.getId() + " => " + document.getData());
                                    //creation ou overide du produit
                                    db.collection("produit").document(document.get("nom").toString())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });

                                    addProduit(p);
                                }
                                if (trouve == false) {

                                    addProduit(p);
                                }
                            } else {
                                Log.d(TAG + "45", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
    public void reloadCategorie() {
        cleanlistbtn(false);

        /*for(Produit p : list_p){
            if(p.getCategorie().equals(getServiceBaseCategorie().getCurrent_categorie())){
                int index = p.getIndex() - 1;
                list_btn_produit.get(index).setText(p.getNom()+"\n"+p.getPrix()+" €");
            }
        }*/
        CountDownTimer timer;
        timer = new CountDownTimer(4000, 25) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(list_btn_produit!=null){
                    for(int i = 0; i < list_btn_produit.size(); i ++){
                        if(i < list_p.size()){
                            if(list_p.get(i).getCategorie().equals(getServiceBaseCategorie().getCurrent_categorie())){
                                int index = list_p.get(i).getIndex() - 1;
                                if(index<list_btn_produit.size()){
                                    list_btn_produit.get(index).setText(list_p.get(i).getNom()+"\n"+list_p.get(i).getPrix()+" €");
                                    /*list_btn_produit.remove(index);
                                    list_btn_produit.add(Utils.setupBtnProduit("darkblue",mainActivity,list_p.get(i).getNom()+"\n"+list_p.get(i).getPrix()+" €","", 140));
                                     */
                                }
                            }
                        }
                    }
                    cancel();
                }else{
                    Log.e("list_btn_produit reloaded", "erreur");
                }
            }
            @Override
            public void onFinish() {
                try{
                    start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();

    }
    public static void isLocalTimeSynchroWithZ(/*int Z*/){

        // choix du pays : france
        /* switch case a definir
         * pour recuperer l heure internet correspondante*/

        String zone = String.valueOf(TimeZone.getDefault());

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://aperichill.fr/api/getSynchronisedTime.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("12345","failure");
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String dateheure_net = myResponse.split("<br>")[0];
                        String nombreRegex = "^-?\\d+(\\.\\d+)?$";

                        Pattern pattern = Pattern.compile(nombreRegex);
                        Matcher matcher = pattern.matcher(dateheure_net);

                        if (matcher.matches()) {
                            Log.d("12345", "date heure net :"+dateheure_net);
                            Log.d("12345","seconde machine : "+Utils.timestamp_seconde());
                            Log.d("12345","zone : "+zone);
                            diff = Long.valueOf(dateheure_net)-Utils.timestamp_seconde();
                            if(diff>60 || diff<-60){
                                Log.d("12345","Désynchronisation");
                                //notifySynchroError();
                                heure_desynchro_notif = myResponse.split("<br>")[1];
                                isSynchro = false;
                            }else{
                                Log.d("12345","Synchro OK");
                                isSynchro = true;
                            }
                        }else{
                            Log.d("connexion serveur", "problème de lecture temps serveur");
                        }
                    }
                });

            }
        });
    }
    private void checkSynchroTime() {
        CountDownTimer timer;
        timer = new CountDownTimer(3000, 1500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.e(TAG, "tick synchro time");

                if(isSynchro == false && b_init == true && b_dialog_synchro == false) {
                    perte_synchro_dialog = ProgressDialog.show(mainActivity, "",
                            "Problème de synchronisation serveur...", true);
                    b_dialog_synchro = true;
                    notifyPerteSynchro(heure_desynchro_notif, diff);
                }
                if (isSynchro == true && b_dialog_synchro == true) {

                    perte_synchro_dialog.dismiss();
                    b_dialog_synchro = false;

                }
                isLocalTimeSynchroWithZ();

            }
            @Override
            public void onFinish() {
                try{
                    start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }
    private void notifyPerteSynchro(String dateheure, long diff) {

        Map<String, Object> p = new HashMap<>();
        p.put("Difference detectée :", diff);;

        db.collection("notification").document("synchro").collection("synchro_notification").document(dateheure)
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
                });
    }
    private DocumentSnapshot elements_ticket;
    private ArrayList<DocumentSnapshot> l_detail;

    public void refreshTicketUI(int countDownIntervalRefresh) {


        timerRefreshTicket = new CountDownTimer(3000, countDownIntervalRefresh) {
        @Override
        public void onTick(long millisUntilFinished) {

            TextView infotabletv = mainActivity.findViewById(R.id.tvInfotable);
            DocumentReference docRef = db.collection("ticket").document(infotabletv.getText().toString());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Log.d("123456", "Elements ticket : " + document.getData());

                            elements_ticket = task.getResult();
                            // chargement detail
                            db.collection("ticket").document(table_en_cours).collection("detail")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                l_detail = new ArrayList<DocumentSnapshot>();
                                                for (DocumentSnapshot document : task.getResult()) {

                                                    //Log.w(TAG+"456", "detail : "+String.valueOf(document.getData()), task.getException());
                                                    l_detail.add(document);
                                                    switch(l_detail.size()){
                                                        case 1 :
                                                            //if(!large_list) countDownIntervalRefresh = 5;
                                                            break;
                                                        case 30 : //countDownIntervalRefresh = 1500;
                                                                //timerRefreshTicket.cancel();
                                                                //refreshTicketUI(1500);
                                                                //large_list = true;
                                                            break;
                                                        /*case 40 : countDownIntervalRefresh = 300;

                                                            break;
                                                        case 50 : countDownIntervalRefresh = 400;

                                                            break;*/
                                                        default: Log.d("refresh countdown","refresh count down: "+countDownIntervalRefresh);
                                                            break;
                                                    }
                                                }

                                                feedTicket.refresh(elements_ticket, l_detail,ticket_en_cours, infotabletv);

                                                //l_detail.add(document);
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }
                                    });
                            // fin chargement detail
                        } else {
                            Log.d("123456", "No such document");
                        }
                    } else {
                        Log.d("123456", "get failed with ", task.getException());
                    }
                }
            });

        }
        @Override
        public void onFinish() {
            try{
                start();
            }catch(Exception e){
                Log.e("Error", "Error: " + e.toString());
            }
        }
        }.start();
    }
    public void envoyer(int i) {
        switch(i){
            case 1:
                /* A envoyer null*/
                envoyer_suivre("À envoyer", "#FFFFFF");
                break;
            case 2:
                /* Orange FF8C00 */
                envoyer_suivre("À suivre", "#FF8C00");
                break;
            case 3:
                /* Cyan 00FFFF*/
                envoyer_suivre("À suivre", "#00FFFF");
                break;
            case 4:
                /* Purple 800080*/
                envoyer_suivre("À suivre", "#800080");
                break;
            case 5:
                /* Suite simple FFFFFF ~é*/
                envoyer_suivre("À suivre", "null");
                break;
        }
    }
    public void envoyer_suivre(String etat, String colorEtat){
        db.collection("ticket").document(table_en_cours).collection("detail")
                .whereEqualTo("etat", etat).whereEqualTo("colorEtat", colorEtat)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.get("nom").toString();
                                Double prix = Double.parseDouble(document.get("prix").toString());
                                String etat = "Envoyé";
                                String color_etat = "#FFFFFF";
                                String color_etat_initial = document.get("colorEtat_initial").toString();
                                Long timestamp = Long.parseLong(document.get("timestamp").toString());
                                List<String> options = (List<String>) document.get("options");
                                boolean paye_separe = (boolean) document.get("paye_separe");

                                Produit_ticket p_ticket = new Produit_ticket(nom, prix, etat, color_etat,timestamp);
                                Map<String, Object> p = new HashMap<>();
                                p.put("nom", p_ticket.getNom());
                                p.put("prix", p_ticket.getPrix());
                                p.put("etat", p_ticket.getEtat());
                                p.put("paye_separe", paye_separe);

                                p.put("colorEtat", p_ticket.getColorEtat());
                                p.put("colorEtat_initial", color_etat_initial);
                                p.put("timestamp", p_ticket.getTimestamp());
                                p.put("timestamp_envoi", Utils.timestamp_milliseconde());
                                p.put("options", options);

                                db.collection("ticket/"+table_en_cours+"/detail").document(document.getId())
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
                                        });
                            }

                        } else {
                            Log.d(TAG + "45", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void supprimerProduit(String current_category, int indexbtn) {
        db.collection("produit")
                .whereEqualTo("index", indexbtn + 1).whereEqualTo("categorie", current_category)
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
                                db.collection("produit").document(document.get("nom").toString())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                                list_btn_produit.get(indexbtn).setText("");

                                //addProduit(p);

                            }
                            if (trouve == false) {

                                //addProduit(p);
                            }
                        } else {
                            Log.d(TAG + "45", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void annuler(String nom, Long timestamp) {
        db.collection("ticket").document(table_en_cours).collection("detail")
                .whereEqualTo("timestamp", timestamp).whereEqualTo("nom", nom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.get("nom").toString();
                                Double prix = Double.parseDouble(document.get("prix").toString());
                                String etat = "À envoyer";

                                String color_etat = document.get("colorEtat_initial").toString();
                                if(color_etat.equals("#FF8C00") || color_etat.equals("#00FFFF") || color_etat.equals("#800080")){
                                    etat = "À suivre";
                                }

                                Long timestamp = Long.parseLong(document.get("timestamp").toString());
                                List<String> options = (List<String>) document.get("options");
                                boolean paye_separe = (boolean) document.get("paye_separe");

                                Produit_ticket p_ticket = new Produit_ticket(nom, prix, etat, color_etat,timestamp);
                                Map<String, Object> p = new HashMap<>();
                                p.put("nom", p_ticket.getNom());
                                p.put("prix", p_ticket.getPrix());
                                p.put("etat", "annule");
                                p.put("annulation_demand","");
                                p.put("paye_separe", paye_separe);
                                p.put("colorEtat", p_ticket.getColorEtat());
                                p.put("colorEtat_initial", color_etat);
                                p.put("timestamp", p_ticket.getTimestamp());
                                p.put("timestamp_envoi", Utils.timestamp_milliseconde());
                                p.put("options", options);

                                db.collection("ticket/"+table_en_cours+"/detail").document(document.getId())
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
                                        });
                            }

                        } else {
                            Log.d(TAG + "45", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void suppression_p_feed(String nom, Long timestamp, String key, String justification, String raison) {
        db.collection("ticket").document(table_en_cours).collection("detail")
                .whereEqualTo("timestamp", timestamp).whereEqualTo("nom", nom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.get("nom").toString();
                                Double prix = Double.parseDouble(document.get("prix").toString());
                                String etat = "À envoyer";

                                String color_etat = document.get("colorEtat_initial").toString();
                                if(color_etat.equals("#FF8C00") || color_etat.equals("#00FFFF") || color_etat.equals("#800080")){
                                    etat = "À suivre";
                                }

                                Long timestamp = Long.parseLong(document.get("timestamp").toString());
                                List<String> options = (List<String>) document.get("options");
                                boolean paye_separe = (boolean) document.get("paye_separe");

                                Produit_ticket p_ticket = new Produit_ticket(nom, prix, etat, color_etat,timestamp);
                                Map<String, Object> p = new HashMap<>();
                                p.put("nom", p_ticket.getNom());
                                p.put("prix", p_ticket.getPrix());
                                p.put("etat", "supprime");
                                p.put("suppression_key",key);
                                p.put("suppression_demand","");
                                p.put("suppression_justification", justification);
                                p.put("suppression_raison", raison);
                                p.put("paye_separe", paye_separe);
                                p.put("colorEtat", p_ticket.getColorEtat());
                                p.put("colorEtat_initial", color_etat);
                                p.put("timestamp", p_ticket.getTimestamp());
                                p.put("timestamp_envoi", Utils.timestamp_milliseconde());
                                p.put("options", options);

                                db.collection("ticket/"+table_en_cours+"/detail").document(document.getId())
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
                                        });
                            }

                        } else {
                            Log.d(TAG + "45", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void initOptionProduit() {

        db.collection("option_produit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int cpt = 0;

                        if (task.isSuccessful()) {
                            option_produit = new String[task.getResult().size()][50];
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                option_produit[cpt][0] = document.getId();
                                for(int i = 0; i < document.getData().size();i++){
                                    option_produit[cpt][i+1] = document.get(String.valueOf(i)).toString();
                                }
                                cpt++;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public String[][] getOption_produit() {
        return option_produit;
    }

    public ArrayList<Produit> getList_p() {
        return list_p;
    }

    public void updateTableComment(String table, EditText editNote) {
        DocumentReference docRef = db.collection("ticket").document(table);

        // Mettre à jour un ou plusieurs champs spécifiques
        docRef.update("commentaire", editNote.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Le champ a été mis à jour avec succès
                        Log.d("Firestore", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de la mise à jour du champ
                        Log.w("Firestore", "Error updating document", e);
                    }
                });
    }
}
