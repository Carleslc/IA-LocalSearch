import representation.ProblemSuccessorFunction;
import representation.State;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        State initialState = new State();
        System.out.println(initialState);
        List states = new ProblemSuccessorFunction().getSuccessors(initialState);
        System.out.println("Successors: " + states.size());
        if (!states.isEmpty()) {
            System.out.println(states.get(0));
        }
    }

}
