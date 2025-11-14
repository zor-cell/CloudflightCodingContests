import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    /**
     * Do not change this method. Method Loads the Config and initializes the IOManager.
     * @param args
     */
    public static void main(String[] args) {
        Config config = ConfigLoader.loadConfig("config.xml");

        IOManager ioManager = new IOManager(config.getInputPath(), config.getOutputPath(), config.getAllowedExtensions());

        ioManager.setDebug(config.isDebug());
        ioManager.setTargetSpecificLevel(config.getTargetSpecificLevel());
        ioManager.setCleanupOutput(config.getCleanupOutput());

        ioManager.initialize();
        ioManager.execute();
    }


    /**
     * method for reading input from input file and writing solution to output file
     * gets applied to all given input files
     * example of a program to output line length of each line
     * DO NOT CHANGE PARAMETERS OR RETURN TYPE
     *
     * @param reader
     * @param writer
     * @throws IOException
     */
    public static void solve(Scanner reader, FileWriter writer) throws IOException {
        //read lines from input
        //and write to file using: writer.write(result + "\n");
        int n = reader.nextInt();
        reader.nextLine(); //skip linebreak

        int result = 0;
        for (int i = 0; i < n; i++) {
            String line = reader.nextLine();
            Scanner lineScanner = new Scanner(line);

            List<Integer> list = new ArrayList<>();
            while (lineScanner.hasNextInt()) {
                list.add(lineScanner.nextInt());
            }

            lineScanner.close();

            int res = list.stream().reduce(Integer::sum).orElse(0);

            writer.write(res + "\n");
        }

    }
}
