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

        for (int i = 0; i < n; i++) {
            int station = reader.nextInt();
            int timeLimit = reader.nextInt();
            reader.nextLine();

            List<Integer> list = new ArrayList<>();

            int sign = station < 0 ? -1 : 1;
            int val = 5 * sign;
            for(int j = 0;j < Math.abs(station) / 2;j++) {
                list.add(val);
                if(sign == 1) {
                    if(val > 1) {
                        val--;
                    }
                } else {
                    if (val < -1) {
                        val++;
                    }
                }
            }

            // append reversed list
            for (int j = list.size() - 1; j >= 0; j--) {
                list.add(list.get(j));
            }

            if(Math.abs(station) % 2 == 1) {
                list.add(Math.abs(station) / 2, val);
            }

            writer.write("0 ");
            for(int j = 0;j < list.size();j++) {
                int v =  list.get(j);
                writer.write(v + " ");
            }
            writer.write("0\n");
        }
    }
}
