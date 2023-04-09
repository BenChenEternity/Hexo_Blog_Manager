import java.util.ArrayList;

public class Main {
    static ArrayList<Process> childProcess;
    static Deployment Deployer;
    static Settings Options;
    static NewArticle newer;
    static String Root;
    static String WebpPath;
    static Process localhost;
    static boolean serverOn;
    static TagCategoryManager TGManager;
    static ArticleListWindow ArticlePanel;
    static UIPanel GUI;
    static AuthorInfoPage Info;

    public static void main(String args[]) {
        Info = new AuthorInfoPage();
        ArticlePanel = new ArticleListWindow();
        Options = new Settings();
        newer = new NewArticle();
        Deployer = new Deployment();
        GUI = new UIPanel();
        UIPanel.setIconForWindows();

    }
}
