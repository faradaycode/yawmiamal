package com.magentamedia.yaumiamal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Grafik extends AppCompatActivity {

    ArrayList<BarEntry> barData;
    ArrayList<String> labels;
    BarDataSet barDataSet;
    BarData bd;

    @BindView(R.id.mpchart)
    BarChart bc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

        ButterKnife.bind(this);

        chartDraw();
    }

    private void chartDraw() {
        barData = new ArrayList<>();
        labels = new ArrayList<>();

        barData.add(new BarEntry(4f, 0));
        barData.add(new BarEntry(8f, 1));
        barData.add(new BarEntry(6f, 2));
        barData.add(new BarEntry(12f, 3));
        barData.add(new BarEntry(18f, 4));
        barData.add(new BarEntry(9f, 5));

        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        barDataSet = new BarDataSet(barData, "Pencapaian");
        bd = new BarData(barDataSet);

        bc.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        bc.setData(bd);
        bc.animateY(3000);
    }
}
