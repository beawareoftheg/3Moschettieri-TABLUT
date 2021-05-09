package it.unibo.ai.didattica.competition.tablut.client.player

fun main(args: Array<String>) {
    var address = "localhost"
    var timeout = 57
    var role = ""
    var name = "3Moschettieri"
    if(args.size != 4) {
        println("Bad arguments\nUSAGE: java -jar <jarName>.jar -p <playerRole> -h <ipAddress> -t <timeout>")
        return
    }
    address = args[3].trim();
    role = args[0].trim();
    timeout = args[2].toInt();
    /*
    (0..4 step 2).forEach {
        when {
            args[it] == "-h" -> address = args[it+1].trim()
            args[it] == "-p" -> role = args[it+1].trim()
            args[it] == "-t" -> timeout = args[it+1].toInt()
            else -> { println("USAGE: java -jar <jarName>.jar -p <playerRole> -h <ipAddress> -t <timeout>"); return }
        }
    }*/
    if(role != "white" && role != "black")
        println("Wrong player role: insert white or black")
    else if(timeout !in 0..60)
        println("Wrong timeout, insert an int in the range 0-60")
    else {
        println("Trying to connecting to the server...")
        try {
            TablutPlayer(role, name, timeout, address).run()
        } catch(e: java.net.ConnectException)
        {
            println("Cannot connect to server $address!")
        }
    }
}