package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.*;

import java.util.Optional;
import java.util.function.Function;

// sneakily tracks stats about program use via delegation
public class ProgramTracker implements StatementTree {

    private StatementTree program;
    private int runTimes = 0;

    public ProgramTracker(StatementTree program) {
        this.program = program;
    }

    // returns the number of times this program has been run (generally for analysis purposes)
    public int TimesRun() {
        return runTimes;
    }

    @Override
    public StatementResult run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        runTimes++;
        return program.run(state, smoother, equalitySmoother, smoothingRange);
    }
}
