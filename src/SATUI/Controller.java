package SATUI;

import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.Storage;
import Storages.AStarStorage;
import Storages.BreadthStorage;
import Storages.DepthStorage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private AnchorPane chartAnchor;

    @FXML
    private AnchorPane anchorAttemptPlot;

    @FXML
    private AnchorPane anchorClausesPlot;

    @FXML
    private Tab clausesTab;

    @FXML
    private Tab xyTab;

    @FXML
    private Tab attemptTab;

    @FXML
    Button runButton;

    @FXML
    private ChoiceBox<String> methodOptions;

    @FXML
    ProgressBar progress;

    @FXML
    private TextField attemptsField;

    @FXML
    private TextField timeField;

    @FXML
    private Button fileButton;

    @FXML
    private Text fileText;

    @FXML
    TextArea informationsArea;

    @FXML
    private ChoiceBox<String> heuristicOptions;

    @FXML
    private Text heuristicText;

    @FXML
    private CheckBox liveCh;

    @FXML
    private CheckBox attemptCh;

    @FXML
    private CheckBox xyCh;

    @FXML
    private CheckBox clausesCh;


    List<File> listOfFiles = null;

    LineChart<Number,Number> lineChart = null;
    XYChart.Series lineSeries = null;

    BarChart<Number,Number> barChartAttempt = null;
    BarChart.Series barSeriesAttempt = null;

    BarChart<Number,Number> barChartClauses = null;
    BarChart.Series barSeriesClauses = null;


    int i = 13;

    SATToUI toUI = new SATToUI(this);

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initCheckBoxes();
        initDigitTextFields();
        initCharts();
        informationsArea.setEditable(false);

        heuristicOptions.getItems().addAll(toUI.heuristics);
        heuristicOptions.getSelectionModel().select(0);

        methodOptions.getItems().addAll("Depth search","Breadth search","Heuristic search");
        methodOptions.getSelectionModel().select(0);
        methodOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(methodOptions.getSelectionModel().isSelected(2))
                {
                    heuristicOptions.setVisible(true);
                    heuristicText.setVisible(true);
                }
                else
                {
                    heuristicOptions.setVisible(false);
                    heuristicText.setVisible(false);
                }
            }
        });

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restartPlots();
                    toUI.runSolver();
            }
        });

        fileButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            final FileChooser fileChooser = new FileChooser();
            listOfFiles =
                    fileChooser.showOpenMultipleDialog(new Stage());
            if(listOfFiles != null)
                if(listOfFiles.size() == 1)
                    fileText.setText(listOfFiles.get(0).getName());
                else
                    fileText.setText(listOfFiles.size()+" files chosen");
            else
                fileText.setText("no file selected");
        }
    });
    }

    void restartPlots()
    {
        lineSeries.getData().clear();
//        barSeriesClauses.getData().clear();
//        barSeriesAttempt.getData().clear();
    }

    int getNumberOfAttempts()
    {
        if(attemptsField.getText().length() > 0)
            return Integer.parseInt(attemptsField.getText());
        return 1;
    }

    int getTimeLimit()
    {
        if(timeField.getText().length() > 0)
            return Integer.parseInt(timeField.getText());
        return 5;
    }

    void initCheckBoxes()
    {
        clausesTab.setDisable(true);
        xyTab.setDisable(true);
        attemptTab.setDisable(true);
        clausesCh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clausesTab.setDisable(!clausesCh.isSelected());
            }
        });

        xyCh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                xyTab.setDisable(!xyCh.isSelected());
            }
        });

        attemptCh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                attemptTab.setDisable(!attemptCh.isSelected());
            }
        });
    }

    boolean isLive()
    {
        return liveCh.isSelected();
    }

    void initDigitTextFields()
    {
        attemptsField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    attemptsField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


        timeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    timeField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    void initCharts()
    {
        initLineChart();
    }

    public void initLineChart()
    {
        if(lineChart == null)
        {
            //defining the axes
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("time");
            yAxis.setLabel("max number of clauses satisfied");
            //creating the chart
            lineChart = new LineChart<Number,Number>(xAxis,yAxis);
            lineChart.setTitle("Clauses satisfied");
            //defining a series
            lineSeries = new XYChart.Series();
            //populating the series with data

            lineChart.getData().add(lineSeries);
            chartAnchor.getChildren().add(lineChart);
        }
        else
            lineSeries.getData().clear();
    }

    public void addLineData(Number x, Number y)
    {
        System.out.println("is updating");
        lineSeries.getData().add(new XYChart.Data(x, y));
    }

    public String getMethod()
    {
        return methodOptions.getValue();
    }

    public String getHeuristic()
    {
        return heuristicOptions.getValue();
    }

}
