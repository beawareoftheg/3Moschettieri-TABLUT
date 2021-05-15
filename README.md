# TablutCompetition
Software for the Tablut Students Competition

## Installation on Ubuntu/Debian 

From console, run these commands to install JDK 8 e ANT:

```
sudo apt update
sudo apt install openjdk-8-jdk -y
sudo apt install ant -y
```

Now, clone the project repository:

```
git clone https://github.com/AGalassi/TablutCompetition.git
```

## Run the Server without Eclipse

The easiest way is to utilize the ANT configuration script from console.
Go into the project folder (the folder with the `build.xml` file):
```
cd TablutCompetition/Tablut
```

Compile the project:

```
ant clean
ant compile
```

The compiled project is in  the `build` folder.
Run the server with:

```
ant server
```

Check the behaviour using the random players in two different console windows:

```
ant randomwhite

ant randomblack
```

At this point, a window with the game state should appear.

To be able to run other classes, change the `build.xml` file and re-compile everything

## Launch TreMoschettieri Player

To launch our player with role white, 60 seconds of timeout and with a local server just use:

```
java -jar out/artifacts/3Moschettieri_TABLUT_jar/3Moschettieri-TABLUT.jar white 60 localhost
```

And for black role:

```
java -jar out/artifacts/3Moschettieri_TABLUT_jar/3Moschettieri-TABLUT.jar white 60 localhost
```
