package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class Statistiques extends AppCompatActivity {
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        /*lineChart = findViewById(R.id.lineChart);

        // Créer les données pour une journée (heures)
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(8, 5000)); // 8h, CA = 5000
        entries.add(new Entry(9, 8000)); // 9h, CA = 8000
        entries.add(new Entry(10, 12000)); // 10h, CA = 12000
        entries.add(new Entry(11, 15000)); // 11h, CA = 15000
        entries.add(new Entry(12, 18000)); // 12h, CA = 18000
        entries.add(new Entry(13, 20000)); // 13h, CA = 20000
        entries.add(new Entry(14, 22000)); // 14h, CA = 22000
        entries.add(new Entry(15, 18000)); // 15h, CA = 18000
        entries.add(new Entry(16, 15000)); // 16h, CA = 15000
        entries.add(new Entry(17, 12000)); // 17h, CA = 12000
        entries.add(new Entry(18, 15000)); // 18h, CA = 15000
        entries.add(new Entry(19, 18000)); // 19h, CA = 18000
        entries.add(new Entry(20, 20000)); // 20h, CA = 20000
        entries.add(new Entry(21, 22000)); // 21h, CA = 22000
        entries.add(new Entry(22, 24000)); // 22h, CA = 24000

        // Créer un ensemble de données
        LineDataSet dataSet = new LineDataSet(entries, "Chiffre d'affaires par heure");
        dataSet.setColor(ColorTemplate.COLORFUL_COLORS[0]);  // Couleur du graphe
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(true);  // Affiche des points
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[1]);
        dataSet.setDrawValues(false);  // Ne pas afficher les valeurs
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // C'est pour un lissage cubique
        dataSet.setCubicIntensity(0.5f);
        // Configurer les axes
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // X en bas
        xAxis.setGranularity(1f);  // Intervalles fixes (heures)
        xAxis.setLabelCount(6);    // Nombre d'étiquettes visibles sur l'axe des X

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(5000f);  // Intervalles pour les valeurs de CA
        leftAxis.setLabelCount(6);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);  // Désactive l'axe de droite

        // Autoriser le zoom
        lineChart.setPinchZoom(true);
        lineChart.setDoubleTapToZoomEnabled(true);

        // Définir les données pour le graphique
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Rafraîchir le graphique
        lineChart.invalidate();*/
    }
}