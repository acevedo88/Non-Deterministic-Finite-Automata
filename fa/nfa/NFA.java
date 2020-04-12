package fa.nfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;

/**
 * An NFA implementation using java collections data structures
 * Every NFA has a start state, a set of final states, a set of 
 * transition states and a set containing its alphabet
 * 
 * @author 
 *
 */
public class NFA implements fa.FAInterface, fa.nfa.NFAInterface {
	
    private NFAState startState; 
	private LinkedHashSet<NFAState> nfaStatesSet; 
    private LinkedHashSet<Character> alphabetSet; 

    /**
     * Default constructor
     */
    public NFA(){
    	 alphabetSet = new LinkedHashSet<Character>();
        nfaStatesSet = new LinkedHashSet<NFAState>();
    }

    @Override
    public void addStartState(String name) {
    	/* Check if state exists */
        NFAState state = getState(name);
        if(state == null){
            state = new NFAState(name);
            nfaStatesSet.add(state);
        }
        startState = state;
    }

    @Override
    public void addState(String name) {
        nfaStatesSet.add(new NFAState(name));
    }


    /**
     * Checks if a state with the give label exists
     * 
     * @param name The label of the state to be searched for
     * @return NFAState The state which has the label or null
     */
    private NFAState getState(String name){
        NFAState state = null;                   //**Will find state given as input**
        for(NFAState st : nfaStatesSet){
            if(st.getName().equals(name)){
                state = st;
                break;//**dont need this break**
            }
        }
        return state;
    }

    
    @Override
    public void addFinalState(String name) {
    	/* Initialize with overloaded constructor */
        NFAState state = new NFAState(name, true); //** Checking true boolean ** uses constructor for FinalStates **
        nfaStatesSet.add(state);
    }

    
    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
    	/* Get the corresponding states for the 'fromState' label and 'toState' labels. 
    	 * Add to to the fromState's 'delta' a transition to the corresponding
    	 * toState on the symbol 'onSymb */
        (getState(fromState)).addTransition(onSymb, getState(toState));
        if(!alphabetSet.contains(onSymb) && onSymb != 'e'){
            alphabetSet.add(onSymb);
        }
    }

    @Override
    public LinkedHashSet<NFAState> getStates() {
        return nfaStatesSet;
    }

    
    @Override
    public Set<? extends State> getFinalStates() {
    	LinkedHashSet<NFAState> finalStateSet = new LinkedHashSet<>();
        for(NFAState state : nfaStatesSet) {
            if (state.isFinal()) {
                finalStateSet.add(state);
            }
        }
        return finalStateSet;
    }
    

    /**
     * Checks to see if a set of states contains a final state 
     * 
     * @param states
     * @return boolean
     */
    private boolean containsFinalState(Set<NFAState> states){
        boolean b = false;
        for(NFAState state: states){
            if(state.isFinal()){
                b = true;
                break;
            }
        }
        return b;
    }

    
    @Override
    public State getStartState() {
        return startState;
    }

    
    @Override
    public Set<Character> getABC() {
        return alphabetSet;
    }

    
    @Override
    public DFA getDFA() {
    	/* Initialize new DFA */
        DFA dfa = new DFA();
        /* Keep track of visited states */
        Map<Set<NFAState>, String> visitedStates = new LinkedHashMap<>(); // ** Map visitedStates **
        
        /* Get the closure of the NFA's start state*/
        Set<NFAState> states = eClosure(startState); // ** check out eClosure method, only from the start state yeah?**
        /* Add to visited sates set */
        visitedStates.put(states, states.toString());//** tracking states, putting to a string? **
        
        LinkedList<Set<NFAState>> queue = new LinkedList<>();
        /* Adds the set of states to the end of the queue */  
        queue.add(states);
        /* Sets the start state of the DFS */
        dfa.addStartState(visitedStates.get(states)); // ** How does this set the start State for DFS? ** uses dfa method

        while(!queue.isEmpty()){
        	/* Queue based working of a linked list - Retrieves and removes the 
        	 * head (first element) of this list */
            states = queue.poll();

            for (char c : alphabetSet) {
            	LinkedHashSet<NFAState> temp = new LinkedHashSet<>();
                for (NFAState st : states) {
                	/* Adds all of the elements from 'st.getTo(c)' to temp */
                    temp.addAll(st.getTo(c));
                }
                LinkedHashSet<NFAState> temp1 = new LinkedHashSet<>();
                for(NFAState st : temp){
                    temp1.addAll(eClosure(st));
                }
                if(!visitedStates.containsKey(temp1)){
                    visitedStates.put(temp1, temp1.toString());
                    queue.add(temp1);

                    if(containsFinalState(temp1)){
                        dfa.addFinalState(visitedStates.get(temp1));
                    }else{
                        dfa.addState(visitedStates.get(temp1));
                    }
                }
                
                /* Add transitions to the DFA */
                dfa.addTransition(visitedStates.get(states), c, visitedStates.get(temp1));
            }
        }
        return dfa;
    }


    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) { //** input is the start state
    	/* Keeps track of visited states in the depth first state*/
    	LinkedHashSet<NFAState> l = new LinkedHashSet<>();
    	return depthFirstSearch(l, s); //** returns the eClosureSet
    }

    /**
     * Perform a depth first search to get a list of all reachable states from a state
     * on the empty string 'e'
     * 
     * @param l
     * @param st
     * @return Set<NFAState>
     */
    private Set<NFAState> depthFirstSearch(LinkedHashSet<NFAState> l, NFAState st){
    	LinkedHashSet<NFAState> visitedStates = l; // ** LinkedHashSet visitedStates
    	LinkedHashSet<NFAState> eClosureSet = new LinkedHashSet<>();
    
        eClosureSet.add(st);
        /* As long as there exists a state to go to on an empty transition */
        if(!st.getTo('e').isEmpty() && !visitedStates.contains(st)){           //** Key to eClosure ** NFAState getTo?
            visitedStates.add(st);
            for(NFAState nfa : st.getTo('e')){
                eClosureSet.addAll(depthFirstSearch(visitedStates, nfa));
            }
        }
        return eClosureSet;
    }

	
}