package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Produit {
    private String nom;
    private String couleur;
    private int index;
    private String categorie;
    private double prix;

    private List<String> option = new ArrayList<String>();
    public Produit() {
    }

    public Produit(String nom, String categorie, double prix, int index, String couleur) {
        this.nom = nom.toLowerCase();
        this.couleur = couleur;
        this.index = index;
        this.categorie = categorie.toLowerCase();
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom.toLowerCase();
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie.toLowerCase();
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
