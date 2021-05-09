package it.unibo.ai.didattica.competition.tablut.client.tremoschettieri.ai;

import java.util.ArrayList;
import java.util.List;
import it.unibo.ai.didattica.competition.tablut.util.Timer;
import it.unibo.ai.didattica.competition.tablut.client.tremoschettieri.heuristic.TreMoschHeuristic;
import it.unibo.ai.didattica.competition.tablut.domain.*;

public class ABSearch2 {

    public static TreMoschGame game;
    protected int depth;
    private Timer timeout;
    public State.Turn turn;

    public ABSearch2(TreMoschGame game, int timeout, int maxDepth) {
        ABSearch2.game = game;
        this.timeout = new Timer(timeout);
        this.depth = maxDepth;
    }

    protected double eval(State state) {
        return TreMoschHeuristic.evaluation(state, turn);
    }

    public Action makeDecision(State state) {
        int ind;
        this.turn = state.getTurn();
        State stateClone;
        List<Action> actions = game.getActions(state);
        timeout.startTime();
        double[] size = new double [actions.size()];
        depth = 0;
        do
        {
            depth++;
            ind = 0;
            for (Action action : actions)
            {
                stateClone = state.clone();
                size[ind] = minValue(game.movePawn(stateClone, action), 1, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
                if (timeout.isExpired()) {
                    break;
                }
                ind++;
            }
        } while(!timeout.isExpired() && !contains(size));
        int mv = 0;
        double max = size[0];
        for(int i = 1; i < size.length; i++) {
            if (size[i] > max) {
                mv = i;
                max = size[i];
            }
        }
        return actions.get(mv);
    }
    public double maxValue(State state, int depth, double beta, double alpha) {
        if (state.isTerminated()  || timeout.isExpired() || depth >= this.depth) {
            return eval(state);
        } else {
            double val = Double.NEGATIVE_INFINITY;
            ArrayList<State> succ = state.getSuccessors(game);
            for (State newState : succ)
            {
                val = Math.max(val, minValue(newState, depth + 1, beta, alpha));
                if (val >= beta)
                    return val;
                alpha = Math.max(alpha, val);
            }
            return val;
        }
    }
    public double minValue(State state, int depth, double beta, double alpha) {
        if (state.isTerminated() || timeout.isExpired() || depth >= this.depth) {
            return eval(state);
        } else {

            double val = Double.POSITIVE_INFINITY;
            ArrayList<State> succ = state.getSuccessors(game);

            for (State newState : succ)
            {
                val = Math.min(val, maxValue(newState, depth + 1, beta, alpha));
                if (val <= alpha)
                    return val;
                beta = Math.min(beta, val);
            }
            return val;
        }
    }

    private static boolean contains(final double[] array) {
        boolean res = false;
        for(double val : array)
            if(val == Double.POSITIVE_INFINITY){
                res = true;
                break;
            }
        return res;
    }
}