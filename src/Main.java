import representation.Global;
import representation.ProblemSuccessorFunction;
import representation.State;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        State initialState = new State(true);
        List states = new ProblemSuccessorFunction().getSuccessors(initialState);
        System.out.println(initialState);
        System.out.println("Successors: " + states.size());
    }

}
