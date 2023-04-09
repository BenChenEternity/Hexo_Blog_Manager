import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class cmd {
    String directory;
    String Command;
    boolean debug;
    Process process;

    public cmd(String directory, String Command, boolean debug) {
        this.directory = directory;
        this.Command = Command;
        this.debug = debug;
    }

    public void execute(){
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", Command);
        if (debug) {
            builder.inheritIO();
        }
        try {
        builder.directory(new File(directory));
        builder.redirectErrorStream(true);
        process = builder.start();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "The folder path entered is incorrect.", "Hint", JOptionPane.INFORMATION_MESSAGE);
        }
        Main.childProcess.add(process);
    }

    public cmd(String directory, String Command) {
        this.directory = directory;
        this.Command = Command;
        this.debug = false;
    }

    public int waitFor() throws InterruptedException {
        return this.process.waitFor();
    }

    public void kill() throws IOException {
        process.getOutputStream().close();
        process.getInputStream().close();
        process.destroy();
    }
}

