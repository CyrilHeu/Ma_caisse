package com.example.myapplication;

public class Categorie {
    private String nom;
    private double taux;

    private int index;

    public Categorie(String nom) {
        this.nom = nom;
    }

    public Categorie() {
    }

    public String getNom() {
        return nom;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }
}
