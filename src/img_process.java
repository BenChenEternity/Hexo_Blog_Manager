import javax.swing.*;

public class img_process {
    static String InputImgPath;
    static String OutputImgPath;

    public static void webp(int quality) {
        cmd transfer = new cmd(Main.WebpPath, "cwebp -q " + quality + " " + InputImgPath + " -o " + OutputImgPath);
        transfer.execute();
        try {
            transfer.waitFor();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to convert", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
