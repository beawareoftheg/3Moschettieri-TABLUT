package it.unibo.ai.didattica.competition.tablut.client.tremoschettieri.heuristic;
import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.util.*;

public class TreMoschHeuristic {
    //White weigths for evaluation
    private final static double WHITE_ENCIRCLEMENT_KING = 2.5;
    private final static double WHITE_KING_GOODLINES = 4.0;
    private final static double WHITE_CHECKERS_COUNT = 3.0;
    private static final double WHITE_KING_NOT_THRONE = 1.0;

    //Black weights for evaluation
    private static final double BLACK_CHECKERS_COUNT = 3.0;
    private static final double BLACK_ENCIRCLEMENT_KING = 11.0;


    public static double evaluation(State state, State.Turn turn) {
        Coordinate kingPos = null;
        double whiteCount = 0;
        double blackCount = 0;
        double evaluationValue;
        //find kingPos and count of white and black checkers
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (state.getBoard()[i][j] == State.Pawn.KING) {
                    kingPos = new Coordinate(i, j, 0, 0, false);
                } else if (state.getBoard()[i][j] == State.Pawn.BLACK) {
                    blackCount++;
                } else if (state.getBoard()[i][j] == State.Pawn.WHITE) {
                    whiteCount++;
                }
            }
        }
        //safe state check
        if (kingPos == null) {
            System.out.println("ERROR state: " + state.boardString());
            return Double.NEGATIVE_INFINITY;
        }
        if (turn == State.Turn.BLACK) {
            evaluationValue = blackEuristic(state, kingPos, blackCount, whiteCount);
        } else {
            evaluationValue = whiteEuristic(state, kingPos, blackCount, whiteCount);
        }
        //System.out.println("valore: " + evaluationValue);
        return evaluationValue;
    }

    public static double blackEuristic(State state, Coordinate kingPos, double blackCount, double whiteCount) {
        if (state.getTurn() == State.Turn.BLACKWIN)
            return Double.POSITIVE_INFINITY;
        if (state.getTurn() == State.Turn.WHITEWIN)
            return Double.NEGATIVE_INFINITY;
        return (BLACK_CHECKERS_COUNT * (blackCount - whiteCount) + BLACK_ENCIRCLEMENT_KING * encirclementKing(state, kingPos));
    }

    public static double whiteEuristic(State state, Coordinate kingPos, double blackCount, double whiteCount) {
        if (state.getTurn() == State.Turn.WHITEWIN)
            return Double.POSITIVE_INFINITY;
        if (state.getTurn() == State.Turn.BLACKWIN)
            return Double.NEGATIVE_INFINITY;
        return (WHITE_CHECKERS_COUNT*(whiteCount - blackCount) - WHITE_ENCIRCLEMENT_KING * encirclementKing(state, kingPos) + WHITE_KING_GOODLINES * kingInGoodLines(state, kingPos) + WHITE_KING_NOT_THRONE * kingIsNotInThrone(kingPos));
    }

    /**
     * Function that valuate if the king is still in the throne
     *
     * @param kingPos position of the king in this state
     * @return 0 if the king is not moving from the throne, 1 otherwise
     */
    public static double kingIsNotInThrone(Coordinate kingPos) {
        if (kingPos.getX() != 4 && kingPos.getY() != 4) {
            return 1;
        } else return 0;
    }


    /**
     * Return a value between 0 and 1 with 0.25 increment for every enemy pieces or enemy camps that are encircle the King.
     *
     * @param state        the state of the game
     * @param kingPos position of the king in this state
     * @return value from 0.0 to 1.0 of black checkers around the King.
     */
    public static double encirclementKing(State state, Coordinate kingPos) {
        double dangerous = 0.0; //measure the  risk of the king
        if (kingPos.getX() - 1 >= 0) {
            //check left position
            if (state.getBoard()[kingPos.getX() - 1][kingPos.getY()] == State.Pawn.BLACK
                    || (kingPos.getX() == 2 && kingPos.getY() == 4)
                    || (kingPos.getX() == 5 && kingPos.getY() == 1)) {
                dangerous += 0.25;
            }
        }

        if (kingPos.getX() + 1 < 9) {
            //check right position
            if (state.getBoard()[kingPos.getX() + 1][kingPos.getY()] == State.Pawn.BLACK
                    || (kingPos.getX() == 6 && kingPos.getY() == 4)
                    || (kingPos.getX() == 3 && kingPos.getY() == 1)) {
                dangerous += 0.25;
            }
        }

        if (kingPos.getY() - 1 >= 0) {
            //check up position, enemy or camp
            if (state.getBoard()[kingPos.getX()][kingPos.getY() -1] == State.Pawn.BLACK
                    || (kingPos.getX() == 4 && kingPos.getY() == 2)
                    || (kingPos.getX() == 3 && kingPos.getY() == 1)
                    || (kingPos.getX() == 5 && kingPos.getY() == 1)) {
                dangerous += 0.25;
            }
        }

        if (kingPos.getY() + 1 < 9) {
            //check bottom position, enemy or camp
            if (state.getBoard()[kingPos.getX()][kingPos.getY()+1] == State.Pawn.BLACK
                    || (kingPos.getX() == 4 && kingPos.getY() == 6)) {
                dangerous += 0.25;
            }
        }

        return dangerous;
    }

    /**
     * Return a value between 0 and 1 with 0.25 increment for every enemy pieces or enemy camps that are encircle the King.
     *
     * @param state        the state of the game
     * @param kingPos      position of the king in this state
     * @return value from 0.0 to 2.0: 0.1 if row or columns 1 or 7, 0.3 in row or columns 2 or 6 and 1.0 if there is no obstacle
     */
    public static double kingInGoodLines(State state, Coordinate kingPos) {
        double res = 0.0;
        //checking rows
        if (kingPos.getY() == 2) {
            if (checkObstacleRow(state, 2)) {
                res += 0.3;
            } else res += 1.0;
        } else if (kingPos.getY() == 6) {
            if (checkObstacleRow(state, 6)) {
                res += 0.3;
            } else res += 1.0;
        } else if (kingPos.getY() == 1 || kingPos.getY() == 7) {
            res += 0.1;
        }

        //checking columns
        if (kingPos.getX() == 2) {
            if (checkObstacleCol(state, 2)) {
                res += 0.3;
            } else res += 1.0;
        } else if (kingPos.getX() == 6) {
            if (checkObstacleCol(state, 6)) {
                res += 0.3;
            } else res += 1.0;
        } else if (kingPos.getX() == 1 || kingPos.getX() == 7) {
            res += 0.1;
        }
        return res;
    }

    /**
     * Check if there are some obstacle (white or black checkers) in the row
     *
     * @param state        the state of the game
     * @param row          the row to check
     * @return boolean: true if there is an obstacle, false otherwise (no obstacle).
     * */
    public static boolean checkObstacleRow(State state, int row) {
        boolean obstacle = false;
        for (int i = 0; i < 9; i++) {
            if (!state.getBoard()[i][row].equalsPawn(State.Pawn.EMPTY.toString())) {
                obstacle = true;
                break;
            }
        }
        return obstacle;
    }

    /**
     * Check if there are some obstacle (white or black checkers) in the column
     *
     * @param state        the state of the game
     * @param col          the column to check
     * @return boolean: true if there is an obstacle, false otherwise (no obstacle).
     * */
    public static boolean checkObstacleCol(State state, int col) {
        boolean obstacle = false;
        for (int i = 0; i < 9; i++) {
            if (!state.getBoard()[col][i].equalsPawn(State.Pawn.EMPTY.toString())) {
                obstacle = true;
                break;
            }
        }
        return obstacle;
    }
}

