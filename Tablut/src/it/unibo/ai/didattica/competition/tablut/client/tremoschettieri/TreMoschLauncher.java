package it.unibo.ai.didattica.competition.tablut.client.tremoschettieri;

import java.util.Locale;

public class TreMoschLauncher {
    public static void main(String args[]) {
        if(args.length != 3) {
            System.out.println("Bad arguments, please run as: java -jar <jarName>.jar <playerRole> <timeout> <ipAddress>");
            return;
        }
        String role = args[0].trim();
        role = role.toLowerCase(Locale.ROOT);
        int timeout = Integer.parseInt(args[1].trim());
        String add = args[2].trim();
        if(timeout < 1 || timeout > 60)
        System.out.println("You have inserted "+timeout+", please insert a number between 1 and 60");
    else if(!role.equals("white") && !role.equals("black"))
            System.out.println("You have inserted $role, please insert black or white");
        else {
            System.out.println("Trying to connecting to the server...");
            try {
                (new TreMoschPlayer(role, "3Moschettieri", timeout, add)).run();
            } catch(Exception e)
            {
                e.printStackTrace();
                System.out.println("Cannot connect to server "+add+"!");
            }
        }
    }
}
