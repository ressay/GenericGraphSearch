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
import javafx.scene.chart.*;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    ArrayList<Number> lineXValues = new ArrayList<>();
    ArrayList<Number> lineYValues = new ArrayList<>();

    BarChart<String,Number> barChartAttempt = null;
    BarChart.Series barSeriesAttempt = null;
    ArrayList<String> attemptXValues = new ArrayList<>();
    ArrayList<Number> attemptYValues = new ArrayList<>();

    BarChart<String,Number> barChartClauses = null;
    BarChart.Series barSeriesClauses = null;
    ArrayList<String> clausesXValues = new ArrayList<>();
    ArrayList<Number> clausesYValues = new ArrayList<>();

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
        methodOptions.setOnAction(actionEvent -> {
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
        });

        runButton.setOnAction(actionEvent -> {
            restartPlots();
                toUI.runSolver();
        });

        fileButton.setOnAction(actionEvent -> {
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
        clausesCh.setOnAction(actionEvent -> clausesTab.setDisable(!clausesCh.isSelected()));

        xyCh.setOnAction(actionEvent -> xyTab.setDisable(!xyCh.isSelected()));

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
        attemptsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                attemptsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });


        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timeField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }


    void initCharts()
    {
        initLineChart();
        initAttemptChart();
        initClausesChart();
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
            lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Clauses satisfied");
            //defining a series
            lineSeries = new XYChart.Series();
            //populating the series with data

            lineChart.getData().add(lineSeries);
            chartAnchor.getChildren().add(lineChart);
        }
        else
            lineSeries.getData().clear();
        lineXValues.clear();
        lineYValues.clear();
    }

    public void addLineData(Number x, Number y)
    {
        lineSeries.getData().add(new XYChart.Data(x, y));
    }


    public void keepLineData(Number x,Number y)
    {
        lineXValues.add(x);
        lineYValues.add(y);
    }

    public void showLineKeptData()
    {
        for (int j = 0; j < lineXValues.size(); j++) {
            addLineData(lineXValues.get(j),lineYValues.get(j));
        }
    }

    public void initAttemptChart()
    {
        if(barChartAttempt == null)
        {
            //defining the axes
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Attemps");
            yAxis.setLabel("Number of clauses satisfied");
            //creating the chart
            barChartAttempt = new BarChart<String,Number>(xAxis, yAxis);
            barChartAttempt.setTitle("Clauses satisfied per attempt");
            //defining a series
            barSeriesAttempt = new BarChart.Series();
            //populating the series with data

            barChartAttempt.getData().add(barSeriesAttempt);
            anchorAttemptPlot.getChildren().add(barChartAttempt);
        }
        else
            barSeriesAttempt.getData().clear();
        attemptXValues.clear();
        attemptYValues.clear();
    }

    public void addAttemptData(String x, Number y)
    {
        barSeriesAttempt.getData().add(new BarChart.Data(x, y));
    }


    public void keepAttemptData(String x,Number y)
    {
        attemptXValues.add(x);
        attemptYValues.add(y);
    }

    public void showAttemptKeptData()
    {
        for (int j = 0; j < attemptXValues.size(); j++) {
            addAttemptData(attemptXValues.get(j),attemptYValues.get(j));
        }
    }


    public void initClausesChart()
    {
        if(barChartClauses == null)
        {

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Clauses");
            yAxis.setLabel("satisfaction frequency");

            barChartClauses = new BarChart<>(xAxis, yAxis);
            barChartClauses.setTitle("Clauses satisfaction frequency");

            barSeriesClauses = new BarChart.Series();

            barChartClauses.getData().add(barSeriesClauses);
            anchorClausesPlot.getChildren().add(barChartClauses);
        }
        else
            barSeriesClauses.getData().clear();
        clausesXValues.clear();
        clausesYValues.clear();
        System.out.println("done!!");
    }

    public void addClauseData(String x, Number y)
    {
        barSeriesClauses.getData().add(new BarChart.Data(x, y));
    }


    public void keepClauseData(String x,Number y)
    {
        clausesXValues.add(x);
        clausesYValues.add(y);
    }

    public void showClauseKeptData()
    {
        for (int j = 0; j < clausesXValues.size(); j++) {
            addClauseData(clausesXValues.get(j),clausesYValues.get(j));
        }
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
