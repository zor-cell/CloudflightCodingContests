import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

            int sx = Integer.parseInt(split2[0]);
            int sy = Integer.parseInt(split2[1]);
            int timeLimit = Integer.parseInt(split1[1]);

            line = reader.nextLine();
            var split3 = line.split(",");
            int ax = Integer.parseInt(split3[0]);
            int ay = Integer.parseInt(split3[1]);

            int diffx = ax;
            int diffy = ay;

            int min = Math.min(diffx, diffy);
            int target;

            String x;
            String y;
            if(min == diffy) {
                target = ay < 0 ? ay - 3 : ay + 3;

                var goY1 = go(target);
                var waitX1 = wait(goY1);

                var goX = go(sx);
                var waitY = wait(goX);

                var goY2 = go(sy - target);
                var waitX2 = wait(goY2);

                x = constructPath(waitX1, goX, waitX2);
                y = constructPath(goY1, waitY, goY2);
            } else {
                target = ax < 0 ? ax - 3 : ax + 3;

                var goX1 = go(target);
                var waitY1 = wait(goX1);

                var goY = go(sx);
                var waitX = wait(goY);

                var goX2 = go(sx - target);
                var waitY2 = wait(goX2);


                x = constructPath(goX1, waitX, goX2);
                y = constructPath(waitY1, goY, waitY2);
            }


            writer.write(x + "\n");
            writer.write(y + "\n\n");
        }
    }

    private static String constructPath(List<Integer> a, List<Integer> b, List<Integer> c) {
        var path = new ArrayList<Integer>();
        path.add(0);
        path.addAll(a);
        path.addAll(b);
        path.addAll(c);
        path.add(0);

        String s = path.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        return s;
    }

    private static List<Integer> wait(List<Integer> path) {
        if(path.size() == 0) {
            return new ArrayList<>();
        }

        int sum = path.stream().reduce((a, b) -> Math.abs(a) + Math.abs(b)).get();

        var empty = new ArrayList<Integer>();
        for(int k = 0;k < sum;k++) {
            empty.add(0);
        }

        return empty;
    }

    private static List<Integer> go(int x) throws IOException {
        List<Integer> list = new ArrayList<>();

        int sign = x < 0 ? -1 : 1;
        int val = 5 * sign;
        for(int j = 0;j < Math.abs(x) / 2;j++) {
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

        if(Math.abs(x) % 2 == 1) {
            list.add(Math.abs(x) / 2, val);
        }

        return list;
    }
}
