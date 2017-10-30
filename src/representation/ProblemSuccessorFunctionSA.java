package representation;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import model.Petition;
import model.RestrictionViolationException;
import model.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ProblemSuccessorFunctionSA implements SuccessorFunction{
    
    @Override
    public List getSuccessors(Object state) {
        State current = (State) state;
        List<Successor> successors = new ArrayList<>();
        
        Random random = new Random();
        
        Petition petition = Global.getInstance().getAllPetitions().get(random.nextInt(Global.getInstance().getAllPetitions().size()));
        Truck truck = current.getTrucks().get(random.nextInt(current.getTrucks().size()));
        
        try {
        State next = new State(current);
        next.assignTruck(petition, truck.getId());
        successors.add(new Successor("Assign " + truck.getId() + " to Petition " + petition, next));
        }catch (RestrictionViolationException ignore) { }
        return successors;
    }
    
}
