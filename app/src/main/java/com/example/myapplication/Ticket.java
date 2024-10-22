package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;

public class Ticket {
    private String dateheure_ouverture;
    private String dateheure_fermeture;
    private int id;
    private String infotable = "";
    private String commentaireTable = "";
    private Double table;
    private ArrayList<Produit_ticket> list_produit;
    private Double total_ht;
    private Double total_ttc;
    private Double reste_a_payer;
    private String type_paiement;
    private int _z;

    public Ticket() {

    }

    public void set(ArrayList<Produit_ticket> l_produit, String table_en_cours) {
        list_produit = l_produit;
        infotable = table_en_cours;
        //Log.d("123456", String.valueOf(l_produit.get(0).getPaye_separe()));
    }

    public String getDateheure_fermeture() {
        return dateheure_fermeture;
    }

    public void setDateheure_fermeture(String dateheure_fermeture) {
        this.dateheure_fermeture = dateheure_fermeture;
    }

    public String getDateheure_ouverture() {
        return dateheure_ouverture;
    }

    public void setDateheure_ouverture(String dateheure_ouverture) {
        this.dateheure_ouverture = dateheure_ouverture;
    }

    public Double getTable() {
        return table;
    }

    public void setTable(Double table) {
        this.table = table;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfotable() {
        return infotable;
    }

    public void setInfo(String info) {
        this.infotable = info;
    }

    public ArrayList<Produit_ticket> getList_produit() {
        return list_produit;
    }

    public void setList_produit(ArrayList<Produit_ticket> list_produit) {
        this.list_produit = list_produit;
    }

    public Double getTotal_ht() {
        return total_ht;
    }

    public void setTotal_ht(Double total_ht) {
        this.total_ht = total_ht;
    }

    public Double getTotal_ttc() {
        return total_ttc;
    }

    public void setTotal_ttc(Double total_ttc) {
        this.total_ttc = total_ttc;
    }

    public Double getReste_a_payer() {
        return reste_a_payer;
    }

    public void setReste_a_payer(Double reste_a_payer) {
        this.reste_a_payer = reste_a_payer;
    }

    public String getType_paiement() {
        return type_paiement;
    }

    public void setType_paiement(String type_paiement) {
        this.type_paiement = type_paiement;
    }

    public int get_z() {
        return _z;
    }

    public void set_z(int _z) {
        this._z = _z;
    }
    public String getCommentaireTable() {
        return commentaireTable;
    }

    public void setCommentaireTable(String commentaireTable) {
        this.commentaireTable = commentaireTable;
    }

}
