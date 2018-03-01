package mainPackage;

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


    private int printMask = ERROR|MOREINFORMATIONS|DEBUGINFOS;

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

}
