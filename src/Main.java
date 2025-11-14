import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static int timeLimit = 0;

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
            timeLimit = Integer.parseInt(split1[1]);

            line = reader.nextLine();
            var split3 = line.split(",");
            int ax = Integer.parseInt(split3[0]);
            int ay = Integer.parseInt(split3[1]);

            int diffx = ax;
            int diffy = ay;

            int min = Math.min(Math.abs(diffx), Math.abs(diffy));
            int target;

            List<Integer> pathX;
            List<Integer> pathY;
            String x;
            String y;
            if(min == Math.abs(diffy)) {
                double cross = sideOfLine(0, 0, sx, sy, ax, ay);
                if(cross > 0) {
                    target = ay < 0 ? ay + 3 : ay - 3;
                } else {
                    target = ay < 0 ? ay - 3 : ay + 3;
                }

                var goY1 = go(target);
                var waitX1 = wait(goY1);

                var goX = go(sx);
                var waitY = wait(goX);

                var goY2 = go(sy - target);
                var waitX2 = wait(goY2);

                pathX = constructPath(waitX1, goX, waitX2);
                pathY = constructPath(goY1, waitY, goY2);
                x = constructString(pathX);
                y = constructString(pathY);
            } else {
                double cross = sideOfLine(0, 0, sx, sy, ax, ay);
                if(cross > 0) {
                    target = ax < 0 ? ax + 3 : ax - 3;
                } else {
                    target = ax < 0 ? ax - 3 : ax + 3;
                }

                var goX1 = go(target);
                var waitY1 = wait(goX1);

                var goY = go(sy);
                var waitX = wait(goY);

                var goX2 = go(sx - target);
                var waitY2 = wait(goX2);

                pathX = constructPath(goX1, waitX, goX2);
                pathY = constructPath(waitY1, goY, waitY2);
                x = constructString(pathX);
                y = constructString(pathY);
            }

            int timeStep = 0;

            int cum = 0;
            List<Integer> pathXPadded = new ArrayList<>();
            for(var xEntry : pathX) {
                pathXPadded.add(xEntry);
                for(int j = 1;j < xEntry;j++) {
                    pathXPadded.add(-100);
                }
            }
            List<Integer> pathYPadded = new ArrayList<>();
            for(var yEntry : pathY) {
                pathYPadded.add(yEntry);
                for(int j = 1;j < yEntry;j++) {
                    pathYPadded.add(-100);
                }
            }

            int xPos = 0;
            int yPos = 0;
            int goneIndex = -1;
            for(int j = 0;j < Math.min(pathXPadded.size(), pathYPadded.size());j++) {
                int xEntry =  pathXPadded.get(j);
                int yEntry =  pathYPadded.get(j);

                if (xEntry >  0) {
                    xPos++;
                }
                if(yEntry > 0) {
                    yPos++;
                }

                //x check
                if(sx < 0) {
                    if(xPos < ax - 3) {
                        //gone
                        goneIndex = j;
                        break;
                    }
                } else {
                    if(xPos > ax + 3) {
                        //gone
                        goneIndex = j;
                        break;
                    }
                }

                //y check
                if(sy < 0) {
                    if(yPos < ay - 3) {
                        //gone
                        goneIndex = j;
                        break;
                    }
                } else {
                    if(yPos > ay + 3) {
                        //gone
                        goneIndex = j;
                        break;
                    }
                }
            }

            //scrap 0 after gone
            if(goneIndex >= 0) {
                for (int j = goneIndex; j < pathXPadded.size(); j++) {
                    if (pathXPadded.get(j) == 0) {
                        pathXPadded.remove(j);
                        j--;
                    }
                }

                for (int j = goneIndex; j < pathYPadded.size(); j++) {
                    if (pathYPadded.get(j) == 0) {
                        pathYPadded.remove(j);
                        j--;
                    }
                }

                int modIndex = 0;
                for (int j = 0; j < pathXPadded.size(); j++) {
                    if (pathXPadded.get(j) == 0) {
                        modIndex += 1;
                    }
                    if (modIndex >= 5) {
                        for (int k = -5; k < 0 ; k++) {
                            pathXPadded.remove(j + k);
                        }
                        for (int k = 1; k < 5; k++) {
                            boolean found = false;
                            for (int l = 0; l < pathXPadded.size(); l++) {
                                if  (Math.abs(pathXPadded.get(l)) == k) {
                                    pathXPadded.remove(l);
                                    found = true;
                                    break;
                                }
                            }
                            if(found) {
                                pathXPadded.add(j);
                                break;
                            }
                        }
                    }
                }

                //align 0
                int timediff = pathXPadded.size() - pathYPadded.size();
                if (timediff > 0) {
                    for (int j = 0; j < timediff; j++) {
                        pathYPadded.add(0);
                    }
                } else {
                    for (int j = 0; j < Math.abs(timediff); j++) {
                        pathXPadded.add(0);
                    }
                }

                List<Integer> finalX = new ArrayList<>(pathXPadded.stream().filter(aaa -> aaa != -100).toList());
                List<Integer> finalY = new ArrayList<>(pathYPadded.stream().filter(aaa -> aaa != -100).toList());

                finalX.add(0);
                finalY.add(0);
                x = constructString(finalX);
                y = constructString(finalY);
            }

            writer.write(x + "\n");
            writer.write(y + "\n\n");
        }
    }

    private static String constructString(List<Integer> path) {
        String s = path.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        return s;
    }

    private static List<Integer> constructPath(List<Integer> a, List<Integer> b, List<Integer> c) {
        var path = new ArrayList<Integer>();
        path.add(0);
        path.addAll(a);
        path.addAll(b);
        path.addAll(c);
        path.add(0);

        int sum = path.stream().reduce((aa, bb) -> Math.abs(aa) + Math.abs(bb)).get();
        if(sum > timeLimit) {
            throw new RuntimeException("invalid");
        }

        return path;
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

    private static List<Integer> goFrom(int x, int sx) throws IOException {
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

    public static double sideOfLine(double ax, double ay,
                                    double bx, double by,
                                    double px, double py) {

        return (bx - ax) * (py - ay) - (by - ay) * (px - ax);
    }
}
