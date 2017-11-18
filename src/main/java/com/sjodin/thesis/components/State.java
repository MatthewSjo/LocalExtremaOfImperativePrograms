package com.sjodin.thesis.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// The state of the program - the values of variables.  Indexed via integers
public class State<T> {

    @Override
    public String toString() {
        return valueMap.toString();
    }

    private Map<Integer,T> valueMap = new HashMap<Integer, T>();

    public boolean contains(int index) {
        return valueMap.containsKey(index);
    }

    public Set<Map.Entry<Integer,T>> getAll() {
        return valueMap.entrySet();
    }

    public T get(int index) {
        return valueMap.get(index);
    }

    // returns a deep copy
    public State<T> copy() {
        State<T> state = new State<T>();
        for (Map.Entry<Integer, T> entry : getAll()) {
                state.put(entry.getKey(), entry.getValue());
        }
        return state;
    }

    public void put(int index, T value) {
        valueMap.put(index, value);
    }
}

