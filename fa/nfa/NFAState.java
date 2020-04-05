package fa.nfa;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import fa.State;

/**
 * CS361 P2
 * NFAState Class
 * @author Alex Aceved & Derek Valenzuela
 *
 */

public class NFAState extends State{

    private HashMap<Character , LinkedHashSet<NFAState>> delta;
    private boolean isFinal = false;

    public NFAState(String name){


    }

    public void addTransition(char onsymb, NFAState name){

        LinkedHashSet<NFAState> currentStates = delta.get(onsymb);
        if(currentStates == null){
            currentStates = new LinkedHashSet<NFAState>();
        }

        currentStates.add(name);
        delta.put(onsymb, currentStates);
    }

    public LinkedHashSet<NFAState> getTo(char onsymb){
        LinkedHashSet<NFAState> currentStates = delta.get(onsymb);
        if(currentStates == null){
            return new LinkedHashSet<NFAState>();
        }

        return currentStates;
    }

    public void initialDefault(String name){
        delta = new HashMap<Character, LinkedHashed<NFAState>>();
        this.name = name;
    }


}