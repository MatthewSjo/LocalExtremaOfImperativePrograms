package com.sjodin.thesis.programs.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.programs.StatementResult;
import com.sjodin.thesis.programs.StatementTree;

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
    public StatementResult run(ImperativeProgramState<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        runTimes++;
        return program.run(state, smoother, equalitySmoother, smoothingRange);
    }
}
