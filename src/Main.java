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
            String line = reader.nextLine();
            var split1 = line.split(" ");
            var split2 = split1[0].split(",");

            int x = Integer.parseInt(split2[0]);
            int y = Integer.parseInt(split2[1]);
            int timeLimit = Integer.parseInt(split1[1]);

            line = reader.nextLine();
            var split3 = split1[0].split(",");
            int ax = Integer.parseInt(split3[0]);
            int ay = Integer.parseInt(split3[1]);

            int diffx = ax - x;
            int diffy = ay - y;

            int min = Math.min(diffx, diffy);
            int target;
            if(min == diffx) {
                target = ay + 3;
            } else {
                target = ax + 3;
            }

            var listX = compute(x, ax);
            var listY = compute(y, ay);

            writer.write("0 ");
            for(int j = 0;j < listX.size();j++) {
                int v = listX.get(j);
                writer.write(v + " ");
            }
            writer.write("0\n");

            writer.write("0 ");
            for(int j = 0;j < listY.size();j++) {
                int v =  listY.get(j);
                writer.write(v + " ");
            }
            writer.write("0\n");

            writer.write("\n");
        }
    }

    private static List<Integer> compute(int x, int ax) throws IOException {
        List<Integer> list = new ArrayList<>();

        int sign = x < 0 ? -1 : 1;
        int val = 5 * sign;
        for(int j = 0;j < Math.abs(x) / 2;j++) {
            list.add(val);
            if(sign == 1) {
                //brake
                int diff = 5 - val + 1;
                if(x + 2 + diff >= ax) {
                    if(val == 5) {
                        val = 0;
                    } else {
                        val++;
                    }
                } else {
                    //accelerate
                    if(val > 1) {
                        val--;
                    }
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

        if(Math.abs(x) % 2 == 1) {
            list.add(Math.abs(x) / 2, val);
        }

        return list;
    }
}
