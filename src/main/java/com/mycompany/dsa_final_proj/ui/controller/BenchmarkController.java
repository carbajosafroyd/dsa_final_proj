package com.mycompany.dsa_final_proj.ui.controller;

import com.mycompany.dsa_final_proj.ui.StubServices.StubBenchmarkService;
import com.mycompany.dsa_final_proj.ui.StubServices.BenchmarkResult;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.util.List;

public class BenchmarkController {

    @FXML private LineChart<Number, Number> performanceChart;
    @FXML private Label linearTimeLabel;
    @FXML private Label kdTimeLabel;
    @FXML private Label speedupLabel;

    private final StubBenchmarkService benchmarkService = new StubBenchmarkService();

    @FXML
    public void initialize() {
        // Run immediately on load
        handleRunBenchmark();
    }

    @FXML
    private void handleRunBenchmark() {
        performanceChart.getData().clear();

        XYChart.Series<Number, Number> linearSeries = new XYChart.Series<>();
        linearSeries.setName("Linear Search O(n)");

        XYChart.Series<Number, Number> kdSeries = new XYChart.Series<>();
        kdSeries.setName("KD-Tree Search O(log n)");

        List<BenchmarkResult> results = benchmarkService.runBenchmarkSuite();

        for (BenchmarkResult res : results) {
            linearSeries.getData().add(new XYChart.Data<>(res.dataSize, res.linearSearchTimeNs));
            kdSeries.getData().add(new XYChart.Data<>(res.dataSize, res.kdTreeSearchTimeNs));
        }

        performanceChart.getData().add(linearSeries);
        performanceChart.getData().add(kdSeries);

        // Update the stats panel with the highest N result
        if (!results.isEmpty()) {
            BenchmarkResult maxRes = results.get(results.size() - 1);
            linearTimeLabel.setText(String.format("%,d ns", maxRes.linearSearchTimeNs));
            kdTimeLabel.setText(String.format("%,d ns", maxRes.kdTreeSearchTimeNs));
            
            long speedup = maxRes.linearSearchTimeNs / Math.max(1, maxRes.kdTreeSearchTimeNs);
            speedupLabel.setText(speedup + "x Faster");
        }
    }
}
