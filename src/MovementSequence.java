import java.util.ArrayList;
import java.util.List;

public class MovementSequence {
    ArrayList<Integer> ramp;
    ArrayList<Integer> slow;
    ArrayList<Integer> middle;
    int time;
    int goal;
    int finishingPosition;

    public MovementSequence(int goal) {
        ramp = new ArrayList<>(List.of(0));
        slow = new ArrayList<>(List.of(0));
        middle = new ArrayList<>();
        this.goal = goal;
        time = 2;
        finishingPosition = 0;
    }

    public void generateSequence() {

    }

    public void addRamp(int speed) {
        if (speed != 0) {
            finishingPosition += speed > 0 ? 1 : -1;
        }
        this.time += speed;
        ramp.add(speed, ramp.indexOf(speed));
    }

    public void addSlow(int speed) {
        if (speed != 0) {
            finishingPosition += speed > 0 ? 1 : -1;
        }
        this.time += speed;
        slow.add(speed, slow.indexOf(speed));
    }

    public String getSequenceAsString() {
        StringBuilder str =  new StringBuilder();
        for (Integer i : ramp) {
            str.append(i).append(" ");
        }
        str.append(middle).append(" ");
        for (int i = slow.size() - 1; i >= 0; i--) {
            str.append(slow.get(i)).append(" ");
        }
        return str.toString();
    }

}
