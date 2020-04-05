package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import fa.State;
import fa.dfa.DFA;

public class NFA implements FAInterface, NFAInterface {

    public NFA(){

        private NFAState startState;        //start state
        private Set<Character> alphabet = new LinkedHashSet<Character>();    //alphabets (abc)
        private Set<NFAState> setStates = new LinkedHashSet<NFAState>();    //all state sets
        private Set<NFAState> finalStates = new LinkedHashSet<NFAState>();  //final states
        private Set<NFAState> visitedStates = new LinkedHashSet<NFAState>();  //states visited
        private Set<NFAState> eClosureStates = new LinkedHashSet<NFAState>(); //
        private Set<NFAState> nonStates = new LinkedHashSet<NFAState>(); //
        private Set<NFAState> startSet = new LinkedHashSet<NFAState>();  //
    }

    /**
     * Adds the initial state to the DFA instance
     * @param name is the label of the start state
     */
    public void addStartState(String name) {



    }

    /**
     * Adds a non-final, not initial state to the DFA instance
     * @param name is the label of the state
     */
    public void addState(String name){

        NFAState currentState = new NFAState(name);
        setStates.add(currentState);
        nonStates.add(currentState);

    }

    /**
     * Adds a final state to the DFA
     * @param name is the label of the state
     */
    public void addFinalState(String name){

        NFAState finalNFAState = new NFAState(name);  //may need a booleam
        finalStates.add(finalNFAState);
        setStates.add(finalNFAState);


    }


    /**
     * Adds the transition to the DFA's delta data structure
     * @param fromState is the label of the state where the transition starts
     * @param onSymb is the symbol from the DFA's alphabet.
     * @param toState is the label of the state where the transition ends
     */
    public void addTransition(String fromState, char onSymb, String toState){

    }

    /**
     * Getter for Q
     * @return a set of states that FA has
     */
    public Set<? extends State> getStates(){

    }

    /**
     * Getter for F
     * @return a set of final states that FA has
     */
    public Set<? extends State> getFinalStates(){

        return finalStates;
    }

    /**
     * Getter for q0
     * @return the start state of FA
     */
    public State getStartState(){

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
     *
     * @return equivalent DFA
     */
    public DFA getDFA(){

    }

    /**
     * Return delta entries
     * @param from - the source state
     * @param onSymb - the label of the transition
     * @return a set of sink states
     */
    public Set<NFAState> getToState(NFAState from, char onSymb){

    }

    /**
     * Traverses all epsilon transitions and determine
     * what states can be reached from s through e
     * @param s
     * @return set of states that can be reached from s on epsilon trans.
     */

    public Set<NFAState> eClosure(NFAState s){

    }
}