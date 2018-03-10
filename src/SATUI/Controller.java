package SATUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private AnchorPane chartAnchor;

    @FXML
    private Button runButton;

    @FXML
    private ChoiceBox<?> methodOptions;

    @FXML
    private ProgressBar progress;

    @FXML
    private TextField attemptsField;

    @FXML
    private TextField timeField;

    @FXML
    private Button fileButton;

    @FXML
    private Text fileText;

    @FXML
    private TextArea informationsArea;

    LineChart<Number,Number> lineChart;
    XYChart.Series series;
    int i = 13;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        lineChart.getData().add(series);
        chartAnchor.getChildren().add(lineChart);


        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                series.getData().add(new XYChart.Data(i, i));
                i++;
            }
        });
    }
}
