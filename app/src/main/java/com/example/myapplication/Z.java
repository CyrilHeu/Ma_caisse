package com.example.myapplication;

import com.google.firebase.database.core.utilities.Utilities;

public class Z {

    private String dateheure_ouverture;

    private String dateheure_fermeture;

    private String id;

    private String etat;

    public Z() {
        this.dateheure_ouverture = Utils.dateheure();
        this.id = Utils.getUUID();
        this.etat = "ouvert";
    }

    public String getDateheure_ouverture() {
        return dateheure_ouverture;
    }

    public void setDateheure_ouverture(String dateheure_ouverture) {
        this.dateheure_ouverture = dateheure_ouverture;
    }

    public String getDateheure_fermeture() {
        return dateheure_fermeture;
    }

    public void setDateheure_fermeture(String dateheure_fermeture) {
        this.dateheure_fermeture = dateheure_fermeture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double check_difference(){
        Double difference = 0.0;

        return difference;
    }

}
