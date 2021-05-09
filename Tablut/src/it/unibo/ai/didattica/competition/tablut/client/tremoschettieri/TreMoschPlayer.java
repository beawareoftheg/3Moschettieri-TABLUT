package it.unibo.ai.didattica.competition.tablut.client.tremoschettieri;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.client.tremoschettieri.ai.ABSearch2;
import it.unibo.ai.didattica.competition.tablut.client.tremoschettieri.ai.TreMoschGame;
import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.io.IOException;

public class TreMoschPlayer extends TablutClient{

    public TreMoschPlayer(String player, String name, int timeout, String ipAddress) throws IOException {
        super(player, name, timeout, ipAddress);
    }

    @Override
    public void run() {
        State state;
        System.out.println("*********** I 3 MOSCHETTIERI ***********");
        System.out.println("********         role:"+this.getPlayer().toString()+"       **********");
        State startState = new StateTablut();
        startState.setTurn(this.getPlayer());
        TreMoschGame game = new TreMoschGame(startState, 2, 2, "gamelogs", "WHITE", "BLACK");
        ABSearch2 search = new ABSearch2(game, this.getTimeout(), 4);
        try {
        declareName();
        while (true) {
            read();
            state = getCurrentState();
            //System.out.println("state:\n" +state);
            if (state.getTurn() == getPlayer()) {
                long start = System.currentTimeMillis();
                Action action = search.makeDecision(state);
                long end = System.currentTimeMillis();
                long timePassed = end - start;
                System.out.println("Chosen "+action+" with "+timePassed+" seconds");
                write(action);
            } else {
                switch(state.getTurn()) {
                    case WHITEWIN :
                        if (getPlayer() == State.Turn.WHITE) System.out.println("YOU WIN");
                        else if (getPlayer() == State.Turn.BLACK) System.out.println("YOU LOOSE");
                        System.exit(0);
                    case BLACKWIN :
                        if (getPlayer() == State.Turn.BLACK) System.out.println("YOU WIN");
                        else if (getPlayer() == State.Turn.WHITE) System.out.println("YOU LOOSE");
                        System.exit(0);
                    case DRAW :
                        System.out.println("DRAW");
                        System.exit(0);
                    default:
                        //I must don't do anything when it's opposite move
                        System.out.println("Time for the opponent to move, waiting...");
                }
            }
        }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
