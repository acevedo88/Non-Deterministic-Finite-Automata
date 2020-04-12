package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import fa.State;
import fa.dfa.DFA;

/**
 * CS361 P2
 * NFAState Class
 * @author Alex Acevedo & Derek Valenzuela
 *
 */

public class NFA implements NFAInterface {
	
    private NFAState startState;        //start state
    private Set<Character> alphabet;    //alphabets (abc)
    private Set<NFAState> allSetStates;  //all state sets
    private Set<NFAState> finalStates;  //final states
    private boolean containsFinalState;
    private Set<NFAState> visitedStates;  //states visited
    private Set<NFAState> eClosureStates;    //
    private Set<NFAState> nonStates;        //
    private Set<NFAState> startSet;          //

    public NFA(){
    	
        alphabet = new LinkedHashSet<Character>();    //alphabets (abc)
        allSetStates = new LinkedHashSet<NFAState>();    //all state sets
        finalStates = new LinkedHashSet<NFAState>();  //final states
        visitedStates = new LinkedHashSet<NFAState>();  //states visited
        eClosureStates = new LinkedHashSet<NFAState>(); //
        nonStates = new LinkedHashSet<NFAState>(); //
        startSet = new LinkedHashSet<NFAState>();  //

    }

    /**
     * Adds the initial state to the DFA instance
     * @param name is the label of the start state
     */
    public void addStartState(String name) { //called in NFADriver
    	/*for eClosure as input
    	 */
        NFAState st = getState(name);
        if(st == null){
            st = new NFAState(name);
            allSetStates.add(st);
            startSet.add(st);
        }
        
        startState = startSet.iterator().next();
      


    }

    /**
     * Adds a non-final, not initial state to the DFA instance
     * @param name is the label of the state
     */
    public void addState(String name){

        NFAState currentState = new NFAState(name);
        allSetStates.add(currentState);
        nonStates.add(currentState);

    }

    /**
     * Adds a final state to the DFA
     * @param name is the label of the state
     */
    public void addFinalState(String name){

        NFAState finalNFAState = new NFAState(name, true);  //may need a boolean **getFinalStates containsFinalStates
        finalStates.add(finalNFAState); //if this is called in getDFA when a visitedState that is considered final is that it?
        allSetStates.add(finalNFAState);


    }


    /**
     * Adds the transition to the DFA's delta data structure
     * @param fromState is the label of the state where the transition starts
     * @param onSymb is the symbol from the DFA's alphabet.
     * @param toState is the label of the state where the transition ends
     */
    public void addTransition(String fromState, char onSymb, String toState){

        //may need a get state method for this
    	 (getState(fromState)).addTransition(onSymb, getState(toState));
    	 //find alphabet symbols
         if(!alphabet.contains(onSymb) && onSymb != 'e'){
             alphabet.add(onSymb);
         }
    }

    /**
     * Getter for Q
     * @return a set of states that FA has
     */
    public Set<? extends State> getStates(){

        return allSetStates;
    }

    /**
     * Getter for F
     * @return a set of final states that FA has
     */
    public Set<? extends State> getFinalStates(){
    	
    	LinkedHashSet<NFAState> finalSetStates = new LinkedHashSet<>();
        for(NFAState st : allSetStates) {
            if (st.isFinal()) {
                finalSetStates.add(st);
            }
        }

        return finalSetStates;
    }

    /**
     * Getter for q0
     * @return the start state of FA
     */
    public State getStartState(){

        //return startSet.iterator().next();
    	return startState;
    }

    /**
     * Getter for the alphabet Sigma
     * @return the alphabet of FA
     */
    public Set<Character> getABC(){

        return alphabet;
    }
    
    /**
     * Verify if set of states includes any final states 
     * 
     * @param states
     * @return boolean
     */
    private boolean containsFinalState(Set<NFAState> allStates){
        boolean f = false;
        for(NFAState st: allStates){
            if(st.isFinal()){
                f = true;//break;
            }
        }
        return f;
    }
    

    /**
     *
     * @return equivalent DFA
     */
    public DFA getDFA(){
    	
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

            for (char c : alphabet) {
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
    
    /**
     * @param name
     * @return
     */
    private NFAState getState(String name){
        NFAState getState = null;                   //**Will find state given as input**
        for(NFAState st : allSetStates){
            if(st.getName().equals(name)){
                getState = st;
                break;
            }
        }
        return getState;
    }

    /**
     * Return delta entries
     * @param from - the source state
     * @param onSymb - the label of the transition
     * @return a set of sink states
     */
    public Set<NFAState> getToState(NFAState from, char onSymb){

        return from.getTo(onSymb);

    }

    /**
     * Traverses all epsilon transitions and determine
     * what states can be reached from s through e
     * @param s
     * @return set of states that can be reached from s on epsilon trans.
     */

    public Set<NFAState> eClosure(NFAState s){

        Set<NFAState> e_closureStates = s.getTo('e');
        Set<NFAState> returnValue = new LinkedHashSet<NFAState>();

        if(!e_closureStates.isEmpty() && !visitedStates.contains(s)){

            for(NFAState tempStates: e_closureStates){
                if(!returnValue.contains(tempStates)){
                    returnValue.add(tempStates);
                    visitedStates.add(tempStates);

                    if(!returnValue.contains(s)){
                        returnValue.add(s);
                        visitedStates.add(s);
                    }
                    eClosure(tempStates);
                }

            }
        }
        else{
            returnValue.add(s);
        }
        visitedStates.clear();
        return returnValue;
    }
    
}