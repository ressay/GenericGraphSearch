package mainPackage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * Created by ressay on 23/11/17.
 */
public class TextDisplayer
{


    public static final int WARNINGS = 1;
    public static final int IMPORTANTCOMMENTS = 2;
    public static final int RANDOMCOMMENTS = 4;
    public static final int DEBUGINFOS = 8;
    public static final int MOREINFORMATIONS = 16;
    public static final int ERROR = 32;
    public static final int ALLTEXTS = WARNINGS|IMPORTANTCOMMENTS|RANDOMCOMMENTS|MOREINFORMATIONS|ERROR;


    private int printMask = ERROR|DEBUGINFOS|MOREINFORMATIONS;

    private static TextDisplayer _instance = null;

    private TextDisplayer(int printMask) {
        this.printMask = printMask;
    }

    private TextDisplayer() {
    }

    public static TextDisplayer getInstance()
    {
        return (_instance == null)? new TextDisplayer():_instance;
    }

    private String getTextType(int type)
    {
        switch (type)
        {
            case WARNINGS: return "WARNINGS";
            case IMPORTANTCOMMENTS: return "IMPORTANTCOMMENTS";
            case RANDOMCOMMENTS: return "RANDOMCOMMENTS";
            case MOREINFORMATIONS: return "MOREINFORMATIONS";
            case ERROR : return "ERROR";
            case DEBUGINFOS : return "DEBUGINFOS";
        }
        return "";
    }


    public void showText(String text, int typeOfText)
    {
        if((typeOfText & printMask) != 0)
            System.out.println(getTextType(typeOfText) + ": " +text);
    }

    public static class Plotter extends ApplicationFrame {

        private XYSeries series ;
        private final XYSeriesCollection XYData;
        private ChartPanel chartPanel;
        /**
         * A demonstration application showing an XY series containing a null value.
         *
         * @param title  the frame title.
         */
        public Plotter(final String title,final  String Xtext , final String Ytext) {

            super(title);
            this.series = new XYSeries("MAPING");
            this.XYData = new XYSeriesCollection(series);
            final JFreeChart chart = ChartFactory.createXYLineChart(
                    title,
                    Xtext,
                    Ytext,
                    XYData,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            this.chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(640  , 480));
            setContentPane(chartPanel);

        }


        public void setUpAndShow(int X,int Y)
        {
            this.pack();
            this.setVisible(true);
            RefineryUtilities.centerFrameOnScreen(this);
            this.setLocation(X,Y);
        }


        public void addData(double X, double Y)
        {
            this.series.add(X,Y);
        }

        public XYSeries getSeries() {
            return series;
        }

        public void setSeries(XYSeries series) {
            this.series = series;
        }

        public XYSeriesCollection getXYData() {
            return XYData;
        }

        public ChartPanel getChartPanel() {
            return chartPanel;
        }

        public void setChartPanel(ChartPanel chartPanel) {
            this.chartPanel = chartPanel;
        }



        //public static void main(final String[] args) {

    //        final mainPackage.TextDisplayer.BarPlotter demo = new mainPackage.TextDisplayer.BarPlotter("XY Series Demo");
    //        final mainPackage.TextDisplayer.BarPlotter d = new mainPackage.TextDisplayer.BarPlotter("hah");
    //        for (int i = 0; i <100 ; i++) {
    //            d.addData(i,Math.random());
    //        }
    //        d.setUpAndShow(200,200);

        //}

    }
}
