package com.example.myapplication;

import java.util.List;

public class Produit_ticket {
    private String nom;
    //private int qte;
    private Double prix;
    private String etat, ColorEtat_initale;
    private Long timestamp;
    private String colorEtat;
    private String commentaire;
    private Long timestamp_envoi;
    private List<String> value_option;
    private boolean paye_separe;

    public Produit_ticket(String nom, Double prix) {
        this.nom = nom;
        this.prix = prix;
    }
    public Produit_ticket() {

    }
    public Produit_ticket(String nom, Double prix, String etat, String color, Long timestamp) {
        this.nom = nom;
        this.etat = etat;
        this.prix = prix;
        this.timestamp = timestamp;
        this.colorEtat = color;
    }

    public boolean getPaye_separe() {
        return paye_separe;
    }

    public void setPaye_separe(boolean paye_separe) {
        this.paye_separe = paye_separe;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getColorEtat_initial() {
        return ColorEtat_initale;
    }

    public void setColorEtat_initial(String colorEtat_initale) {
        ColorEtat_initale = colorEtat_initale;
    }

    public Long getTimestamp_envoi() {
        return timestamp_envoi;
    }

    public void setTimestamp_envoi(Long timestamp_envoi) {
        this.timestamp_envoi = timestamp_envoi;
    }

    public List<String> getValueOption() {
        return value_option;
    }

    public void setValueOption(List<String> v_option) {
        this.value_option = v_option;
    }

    public String getColorEtat() {
        return colorEtat;
    }

    public void setColorEtat(String colorEtat) {
        this.colorEtat = colorEtat;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
