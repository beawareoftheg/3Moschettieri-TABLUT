package it.unibo.ai.didattica.competition.tablut.client.tremoschettieri.ai;

import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.util.*;

import java.io.IOException;
import java.util.*;

public class TreMoschGame extends GameAshtonTablut implements Game{


    public enum Tipo {FREE, CASTLE, CAMPS, ESCAPES};
    private Tipo Zona[][];

    public TreMoschGame(State state, int repeated_moves_allowed, int cache_size, String logs_folder, String whiteName, String blackName) {
        super(state, repeated_moves_allowed, cache_size, logs_folder, whiteName, blackName);
        this.Zona = new Tipo[9][9];
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                Zona[i][j] = Tipo.FREE;
            }
        }
        //Ashton Tablut board scheme
        Zona[0][1] = Tipo.ESCAPES;
        Zona[0][2] = Tipo.ESCAPES;
        Zona[0][6] = Tipo.ESCAPES;
        Zona[0][7] = Tipo.ESCAPES;
        Zona[1][0] = Tipo.ESCAPES;
        Zona[2][0] = Tipo.ESCAPES;
        Zona[6][0] = Tipo.ESCAPES;
        Zona[7][0] = Tipo.ESCAPES;
        Zona[8][1] = Tipo.ESCAPES;
        Zona[8][2] = Tipo.ESCAPES;
        Zona[8][6] = Tipo.ESCAPES;
        Zona[8][7] = Tipo.ESCAPES;
        Zona[1][8] = Tipo.ESCAPES;
        Zona[2][8] = Tipo.ESCAPES;
        Zona[6][8] = Tipo.ESCAPES;
        Zona[7][8] = Tipo.ESCAPES;

        Zona[0][3] = Tipo.CAMPS;
        Zona[0][4] = Tipo.CAMPS;
        Zona[0][5] = Tipo.CAMPS;
        Zona[1][4] = Tipo.CAMPS;
        Zona[8][3] = Tipo.CAMPS;
        Zona[8][4] = Tipo.CAMPS;
        Zona[8][5] = Tipo.CAMPS;
        Zona[7][4] = Tipo.CAMPS;
        Zona[3][0] = Tipo.CAMPS;
        Zona[4][0] = Tipo.CAMPS;
        Zona[5][0] = Tipo.CAMPS;
        Zona[4][1] = Tipo.CAMPS;
        Zona[3][8] = Tipo.CAMPS;
        Zona[4][8] = Tipo.CAMPS;
        Zona[5][8] = Tipo.CAMPS;
        Zona[4][7] = Tipo.CAMPS;
        
        Zona[4][4] = Tipo.CASTLE;
    }

    public State getInitialState(){
        return new StateTablut();
    }

    @Override
    public boolean isTerminal(Object state) {
        return false;
    }

   @Override
    public State.Turn[] getPlayers() {
        return new State.Turn[]{
                State.Turn.BLACK, State.Turn.WHITE
        };
    }

    @Override
    public Object getPlayer(Object o) {
        return ((State) o).getTurn();
    }


    public State getResult(State state, Action action) {
        if (state != null && action != null) {
            State myClone = state.clone();
            return this.moveChecker(myClone, action);
        } else {
            return this.getInitialState();
        }
    }
    @Override
    public double getUtility(Object state, Object turn) {
        return state instanceof State && turn instanceof State.Turn ? ((turn != State.Turn.BLACK || ((State)state).getTurn() != State.Turn.BLACKWIN) && (turn != State.Turn.WHITE || ((State)state).getTurn() != State.Turn.WHITEWIN) ? ((turn != State.Turn.BLACK || ((State)state).getTurn() != State.Turn.WHITEWIN) && (turn != State.Turn.WHITE || ((State)state).getTurn() != State.Turn.BLACKWIN) ? 0.5D : Double.NEGATIVE_INFINITY) : Double.POSITIVE_INFINITY) : Double.NEGATIVE_INFINITY;
    }

    @Override
    public List<Action> getActions(Object state)
    {
        State st;
        if(state instanceof State) st = (State) state; else return null;
        Coordinate tmp;
        int countEmpty;
        //WHITE
        if(st.getTurn().equals(st.getTurn().WHITE)) {
            List<Coordinate> whiteActions = new ArrayList<>();
            List<Action> res = new ArrayList<>();
            //list of white position in the game
            List<Coordinate> whitePos = new ArrayList<>();
            for (int i = 0; i < st.getBoard().length; i++) {
                for (int j = 0; j < st.getBoard().length; j++) {
                    if (st.getPawn(i, j).equals(Pawn.KING) || st.getPawn(i, j).equals(Pawn.WHITE))  {
                        tmp = new Coordinate(i, j, i,j, false);
                        whitePos.add(tmp);
                    }
                }
            }
            for (Coordinate whiteCheck : whitePos) {
                //vertical
                for (int j = 0; j < st.getBoard().length; j++) {
                    // bottom
                    if(((whiteCheck.getY() + j) < st.getBoard().length) && st.getPawn(whiteCheck.getX(), whiteCheck.getY() + j) == Pawn.EMPTY
                            && (Zona[whiteCheck.getX()][whiteCheck.getY() + j] != Tipo.CAMPS)
                            && (Zona[whiteCheck.getX()][whiteCheck.getY() + j] != Tipo.CASTLE)) {
                        countEmpty = 0;
                        for (int f = whiteCheck.getY() + 1; f < whiteCheck.getY() + j; f++) {
                            if(st.getPawn(whiteCheck.getX(), f) != Pawn.EMPTY || Zona[whiteCheck.getX()][f] == Tipo.CAMPS || Zona[whiteCheck.getX()][f] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            whiteActions.add(new Coordinate(whiteCheck.getX(), whiteCheck.getY() + j, whiteCheck.getX(), whiteCheck.getY(), false));
                        }
                    }
                    // up
                    if(((whiteCheck.getY() - j) >= 0) && st.getPawn(whiteCheck.getX(), whiteCheck.getY() - j) == Pawn.EMPTY
                            && (Zona[whiteCheck.getX()][whiteCheck.getY() - j] != Tipo.CAMPS)
                            && (Zona[whiteCheck.getX()][whiteCheck.getY() - j] != Tipo.CASTLE)) {
                        countEmpty = 0;
                        for (int f = whiteCheck.getY() - 1; f > whiteCheck.getY() - j; f--) {
                            if(st.getPawn(whiteCheck.getX(), f) != Pawn.EMPTY || Zona[whiteCheck.getX()][f] == Tipo.CAMPS || Zona[whiteCheck.getX()][f] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            whiteActions.add(new Coordinate(whiteCheck.getX(), whiteCheck.getY() - j,whiteCheck.getX(), whiteCheck.getY(), false));
                        }
                    }
                }
                //HORIZONTAL
                for (int i = 0; i < st.getBoard().length; i++) {
                    // right
                    if(((whiteCheck.getX() + i) < st.getBoard().length) && st.getPawn(whiteCheck.getX() + i, whiteCheck.getY()) == Pawn.EMPTY
                            && (Zona[(whiteCheck.getX() + i)][ whiteCheck.getY()] != Tipo.CAMPS)
                            && (Zona[(whiteCheck.getX() + i)][ whiteCheck.getY()] != Tipo.CASTLE)) {
                        countEmpty = 0;
                        for (int f = whiteCheck.getX() + 1; f < whiteCheck.getX() + i; f++) {
                            if(st.getPawn(f, whiteCheck.getY()) != Pawn.EMPTY || Zona[f][ whiteCheck.getY()] == Tipo.CAMPS || Zona[f][ whiteCheck.getY()] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            whiteActions.add(new Coordinate(whiteCheck.getX() + i, whiteCheck.getY(), whiteCheck.getX(), whiteCheck.getY(), false));
                        }
                    }
                    // left
                    if(((whiteCheck.getX() - i) >= 0) && st.getPawn(whiteCheck.getX() - i, whiteCheck.getY()) == Pawn.EMPTY
                            && (Zona[(whiteCheck.getX() - i)][ whiteCheck.getY()] != Tipo.CAMPS)
                            && (Zona[(whiteCheck.getX() - i)][ whiteCheck.getY()] != Tipo.CASTLE)) {
                       countEmpty = 0;
                        for (int f = whiteCheck.getX() - 1; f > whiteCheck.getX() - i; f--) {
                            if(st.getPawn(f, whiteCheck.getY()) != Pawn.EMPTY || Zona[f][ whiteCheck.getY()] == Tipo.CAMPS || Zona[f][ whiteCheck.getY()] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            whiteActions.add(new Coordinate(whiteCheck.getX() - i, whiteCheck.getY(), whiteCheck.getX(), whiteCheck.getY(), false));
                        }
                    }

                }

            }
            for(Coordinate coord : whiteActions)
            {
                char from1 = Character.toUpperCase((char)(coord.getOldY() + 97));
                String from = String.valueOf(from1) + String.valueOf(coord.getOldX()+1);
                char to1 = Character.toUpperCase((char)(coord.getY() + 97));
                String to = String.valueOf(to1) + String.valueOf(coord.getX()+1);
                try {
                    res.add(new Action(from,to,st.getTurn()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.shuffle(res);
            return res;
        } else {
            //BLACK
            List<Coordinate> blackPos = new ArrayList<>();
            List<Coordinate> blackAction = new ArrayList<>();

            List<Action> res = new ArrayList<>();
            for (int i = 0; i < st.getBoard().length; i++) {
                for (int j = 0; j < st.getBoard().length; j++) {
                    if (st.getPawn(i, j).equalsPawn(Pawn.BLACK.toString()))  {
                        if(Zona[i][j] == Tipo.CAMPS) {
                            tmp = new Coordinate(i, j, i, j, false);
                            blackPos.add(tmp);
                        } else if (Zona[i][j] == Tipo.FREE || Zona[i][j] == Tipo.ESCAPES) {
                            tmp = new Coordinate(i, j, i, j, true);
                            blackPos.add(tmp);
                        }
                    }
                }
            }
            for (Coordinate blackCheck : blackPos) {
                // vertical
                for (int j = 0; j < st.getBoard().length; j++) {
                    // bottom
                    if(((blackCheck.getY() + j) < st.getBoard().length) && st.getPawn(blackCheck.getX(), blackCheck.getY() + j) == Pawn.EMPTY
                            && (Zona[blackCheck.getX()][blackCheck.getY() + j] != Tipo.CASTLE)
                            && (Zona[blackCheck.getX()][blackCheck.getY() + j] != Tipo.CAMPS)) {
                        countEmpty = 0;
                        for (int f = blackCheck.getY() + 1; f < blackCheck.getY() + j; f++) {
                            if(st.getPawn(blackCheck.getX(), f) != Pawn.EMPTY || (Zona[blackCheck.getX()][f] == Tipo.CAMPS && blackCheck.isHasLeft()) || Zona[blackCheck.getX()][f] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            if((blackCheck.isHasLeft() && (Zona[blackCheck.getX()][blackCheck.getY() + j] != Tipo.CAMPS)) || (!blackCheck.isHasLeft())) {
                                blackAction.add(new Coordinate(blackCheck.getX(), blackCheck.getY() + j, blackCheck.getX(), blackCheck.getY(), blackCheck.isHasLeft()));
                            }
                        }
                    }
                    // up
                    if(((blackCheck.getY() - j) >= 0) && st.getPawn(blackCheck.getX(), blackCheck.getY() - j) == Pawn.EMPTY
                            && (Zona[blackCheck.getX()][blackCheck.getY() - j] != Tipo.CASTLE)
                            && (Zona[blackCheck.getX()][blackCheck.getY() - j] != Tipo.CAMPS)) {
                        countEmpty = 0;
                        for (int f = blackCheck.getY() - 1; f > blackCheck.getY() - j; f--) {
                            if(st.getPawn(blackCheck.getX(), f) != Pawn.EMPTY || (Zona[blackCheck.getX()][f] == Tipo.CAMPS && blackCheck.isHasLeft()) || Zona[blackCheck.getX()][f] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            // if a black is no more in a camp, he cannot enter in any camp anymore
                            if((blackCheck.isHasLeft() && (Zona[blackCheck.getX()][blackCheck.getY() - j] != Tipo.CAMPS)) || (!blackCheck.isHasLeft())) {
                                blackAction.add(new Coordinate(blackCheck.getX(), blackCheck.getY() - j, blackCheck.getX(), blackCheck.getY(), blackCheck.isHasLeft()));
                            }
                        }
                    }

                }
                // horizontal
                for (int i = 0; i < st.getBoard().length; i++) {
                    // left
                    if(((blackCheck.getX() - i) >= 0) && st.getPawn(blackCheck.getX() - i, blackCheck.getY()) == Pawn.EMPTY
                            && (Zona[blackCheck.getX() - i][blackCheck.getY()] != Tipo.CASTLE)
                            && (Zona[blackCheck.getX() - i][blackCheck.getY()] != Tipo.CAMPS)) {
                        countEmpty = 0;
                        for (int f = blackCheck.getX() - 1; f > blackCheck.getX() - i; f--) {
                            if(st.getPawn(f, blackCheck.getY()) != Pawn.EMPTY || (Zona[f][blackCheck.getY()] == Tipo.CAMPS && blackCheck.isHasLeft()) || Zona[f][blackCheck.getY()] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            if((blackCheck.isHasLeft() && (Zona[blackCheck.getX() - i][blackCheck.getY()] != Tipo.CAMPS)) || (!blackCheck.isHasLeft())) {
                                blackAction.add(new Coordinate(blackCheck.getX() - i, blackCheck.getY(), blackCheck.getX(), blackCheck.getY(), blackCheck.isHasLeft()));
                            }
                        }

                    }
                    // right
                    if(((blackCheck.getX() + i) < st.getBoard().length) && st.getPawn(blackCheck.getX() + i, blackCheck.getY()) == Pawn.EMPTY
                            && (Zona[blackCheck.getX() + i][blackCheck.getY()] != Tipo.CASTLE)
                            && (Zona[blackCheck.getX() + i][blackCheck.getY()] != Tipo.CAMPS)) {
                        countEmpty = 0;
                        for (int f = blackCheck.getX() + 1; f < blackCheck.getX() + i; f++) {
                            if(st.getPawn(f, blackCheck.getY()) != Pawn.EMPTY || (Zona[f][blackCheck.getY()] == Tipo.CAMPS && blackCheck.isHasLeft()) || Zona[f][blackCheck.getY()] == Tipo.CASTLE) {
                                countEmpty++;
                            }
                        }
                        if(countEmpty == 0) {
                            if((blackCheck.isHasLeft() && (Zona[blackCheck.getX() + i][blackCheck.getY()] != Tipo.CAMPS)) || (!blackCheck.isHasLeft())) {
                                blackAction.add(new Coordinate(blackCheck.getX() + i, blackCheck.getY(), blackCheck.getX(), blackCheck.getY(), blackCheck.isHasLeft()));
                            }
                        }
                    }
                }

            }
            for(Coordinate coord : blackAction)
            {
                char from1 = Character.toUpperCase((char)(coord.getOldY() + 97));
                String from = String.valueOf(from1) + String.valueOf(coord.getOldX()+1);
                char to1 = Character.toUpperCase((char)(coord.getY() + 97));
                String to = String.valueOf(to1) + String.valueOf(coord.getX()+1);
                try {
                    res.add(new Action(from,to,st.getTurn()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.shuffle(res);
            return res;
        }
    }

    @Override
    public State getResult(Object state, Object action) {
        if (state != null && action != null) {
            return this.moveChecker((State)state, (Action) action);
        } else {
            return this.getInitialState();
        }
    }

    public final State moveChecker(State state, Action a) {
        Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
        Pawn[][] newBoard = state.getBoard();
        if (a.getColumnFrom() == 4 && a.getRowFrom() == 4) {
            newBoard[a.getRowFrom()][a.getColumnFrom()] = Pawn.THRONE;
        } else {
            newBoard[a.getRowFrom()][a.getColumnFrom()] = Pawn.EMPTY;
        }

        newBoard[a.getRowTo()][a.getColumnTo()] = pawn;
        state.setBoard(newBoard);
        if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
            state.setTurn(State.Turn.BLACK);
        } else {
            state.setTurn(State.Turn.WHITE);
        }
        if (state.getTurn().equalsTurn("W")) {
            return this.checkCaptureBlack(state, a);
        } else if (state.getTurn().equalsTurn("B")) {
            return this.checkCaptureWhite(state, a);
        } else {
            return state;
        }
    }






}