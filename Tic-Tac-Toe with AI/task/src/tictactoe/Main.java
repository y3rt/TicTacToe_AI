package tictactoe;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
//
//    1,3(1)(1 1)   2,3(2)   3,3(3)
//
//    1,2(4)   2,2(5)   3,2(6)
//
//    1,1(7)   2,1(8)   3,1(9)

    public static void main(String[] args) {
        Boolean exs = false;
        while (!exs) {
            Game game1 = new Game();
            game1.setupGame();
            game1.printBoard();
            game1.playGame();
        }
    }
}
class Game{
    Character[][] gameArr = new Character[3][3];
    Player[] players = new Player[2];
    Player playerOne;
    Player playerTwo;

    public Game() {
        this.gameArr = gameArr;
    }

    public void clearBoard(){
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < 3; j++) {
                gameArr[i][j] = ' ';
            }
        }
    }

    public void setupGame() {
        clearBoard();
        Scanner scan = new Scanner(System.in);
        System.out.print("Input command:");
        String[] arrCmd = scan.nextLine().split(" ");

        if (arrCmd[0].trim().equals("exit")) {
            System. exit(0);
        } else if (arrCmd[0].trim().equals("start")) {
            if (arrCmd.length == 3) {
                playerOne = selectPlayer('X', arrCmd[1]);
                playerTwo = selectPlayer('O', arrCmd[2]);
                players[0] = playerOne;
                players[1] = playerTwo;

            } else {
                System.out.println("Bad parameters! (33)");
                setupGame();
            }
        } else {
            System.out.println("Bad parameters! (37)");
            setupGame();
        }
    }

    public void playGame(){
//        System.out.println(checkWins());
        while(checkWins() == 'N'){
            for (Player gamePlayer: players) {
                String coords = gamePlayer.enterMove(gameArr);
                System.out.println(gamePlayer.playerType + " coords: " + coords);
                if (coords != "") {
                    placeMove(coords, gamePlayer);
                    printBoard();
                    switch (checkWins()) {
                        case 'X':
                        case 'O':
                            System.out.println(checkWins() + " wins\n");
                            break;
                        case 'I':
                            System.out.println("Impossible\n");
                            break;
                        case ' ':
                            System.out.println("Draw\n");
                            break;
                    }
                    if (checkWins() != 'N') {
                        break;
                    }
                }
            }
        }
    }

    private void endGame() {
        setupGame();
    }

    private Player selectPlayer(char playerMark, String playerType) {
        Player rtnPlayer = null;
        switch (playerType.toLowerCase()) {
            case "easy":
                rtnPlayer = new Easy(playerMark);
                break;
            case "medium":
                rtnPlayer = new Medium(playerMark);
                break;
            case "hard":
                rtnPlayer = new Hard(playerMark);
                break;
            case "user":
                rtnPlayer = new User(playerMark);
                break;
        }
        return rtnPlayer;

    }

    public void printBoard() {
        System.out.println("---------");
        for (int i = 2; i >= 0; i--) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(gameArr[i][j] + " ");
            }
            System.out.print("|\n");
        }
        System.out.println("---------");
    }

    public Character checkWins() {
        int blankCount = 0;
        int xCount = 0;
        int oCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char xo = gameArr[i][j];
                switch (xo){
                    case ' ':
                        blankCount++;
                        break;
                    case 'X':
                        xCount++;
                        break;
                    case 'O':
                        oCount++;
                        break;
                    default:
                        System.out.println(xo);
                        break;
                }
            }
        }
//        System.out.println("blankCount: " + blankCount + " xCount: " + xCount + " oCount: " + oCount);

        Character winner = ' ';

        Character threeInARow = ' ';
        int count = 0;

        for (int i = 0; i < 3; i++) {
            if (gameArr[i][0].equals(gameArr[i][1]) && gameArr[i][0].equals(gameArr[i][2]) && !gameArr[i][0].equals(' ')) {
                threeInARow = gameArr[i][0];
                count++;
            }

            if (gameArr[0][i].equals(gameArr[1][i]) && gameArr[0][i].equals(gameArr[2][i]) && !gameArr[0][i].equals(' ')) {
                threeInARow = gameArr[0][i];
                count++;
            }
        }
        if (gameArr[0][0].equals(gameArr[1][1]) && gameArr[0][0].equals(gameArr[2][2]) && !gameArr[0][0].equals(' ')) {
            threeInARow = gameArr[0][0];
            count++;
        }

        if (gameArr[0][2].equals(gameArr[1][1]) && gameArr[0][2].equals(gameArr[2][0]) && !gameArr[0][2].equals(' ')) {
            threeInARow = gameArr[0][2];
            count++;
        }
        if (count <= 1) {
            winner = threeInARow;
        } else {
            winner = 'I';
        }

        if (blankCount > 0 && winner.equals(' ') && (xCount == oCount + 1 || xCount == oCount - 1 || xCount == oCount)) {
            winner = 'N';
        }
        if (xCount != oCount && winner.equals(' ') && (xCount != oCount + 1 && xCount != oCount - 1)) {
            System.out.println("xCount: " + xCount + " oCount: " + oCount + " winner: " + winner+"|");
            winner = 'I';
        }
//        System.out.println("winner: " + winner);
        return winner;
    }

    public boolean placeMove(String nextCoords, Player currentPlayer) {
//        System.out.println("nextCoords: "+ nextCoords);
        boolean valid = false;
        int nextCol = -1;
        int nextRow = -1;

        nextCol = Integer.parseInt(nextCoords.substring(0, 1));
        nextRow = Integer.parseInt(nextCoords.substring(2));
        System.out.println("203: "+nextCol+":"+nextRow);
        if ((nextCol < 0 && nextCol > 4) && (nextRow < 0 && nextRow > 4)) {
            System.out.println("Coordinates should be from 1 to 3!");
        } else if (currentPlayer.spaceAvailable(nextCol, nextRow, gameArr)) {
            this.gameArr[nextRow - 1][nextCol - 1] = currentPlayer.playerMark;
            valid = true;
            if (!currentPlayer.playerType.equals("user")) {
                System.out.println("Making move level \"" + currentPlayer.playerType + "\"");
            }

        } else {
            if (currentPlayer.playerType.equals("user")) {
                System.out.println("This cell is occupied! Choose another one!");
            }
            placeMove(currentPlayer.enterMove(gameArr), currentPlayer);
        }
        return valid;
    }

}

class Player{
    char playerMark;
    String playerType;

    public Player(char playerMark) {
        this.playerMark = playerMark;
    }

    public String enterMove(Character[][] gameArr) {
        int nextCol = -1;
        int nextRow = -1;
        boolean valid = false;
        String nextCoords = "";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the coordinates: ");
        nextCoords =  scanner.nextLine();
        nextCol = Integer.parseInt(nextCoords.substring(0, 1));
        nextRow = Integer.parseInt(nextCoords.substring(2));

        Pattern coordsPattern = Pattern.compile("([1-3]\\s[1-3])");

        if (coordsPattern.matcher(nextCoords).find()) {
            valid = true;
        } else {
            if ((nextCol < 0  || nextCol > 4) && (nextRow < 0 ||  nextRow > 4)) {
                System.out.println("Coordinates should be from 1 to 3! (248)");
            } else {
                System.out.println("You should enter numbers!");
            }
        }
        if (!valid) {
            nextCol = -1;
            nextRow = -1;
            nextCoords = "";
            enterMove(gameArr);
        }
        return nextCoords;
    }

    public boolean spaceAvailable(int x, int y, Character[][] gameArr){
        x--;
        y--;
        System.out.println("x: "+ (x) + " y: " + (y));
        if (gameArr[y][x] == ' ') {
            return true;
        }
        return false;
    }

}

class User extends Player{

    public User(char playerMark) {
        super(playerMark);
        this.playerType = "user";
    }
}

class Easy extends Player {

    public Easy(char playerMark) {
        super(playerMark);
        this.playerType = "easy";
    }

    @Override
    public String enterMove(Character[][] gameArr){
        return randomMove(gameArr);
    }

    public String randomMove(Character[][] gameArr) {
        int randomCol = (int) (Math.random() * 3) + 1;
        int randomRow = (int) (Math.random() * 3) + 1;
        if (!spaceAvailable(randomCol, randomRow, gameArr)) {
            randomMove(gameArr);
        }
        return randomCol + " " + randomRow;
    }
}

class Medium extends Easy{

    public Medium(char playerMark) {
        super(playerMark);
        this.playerType = "medium";
    }

    @Override
    public String enterMove(Character[][] gameArr){
        String rtnStr;
        if (winMove(gameArr).equals(" ")) {
            rtnStr = randomMove(gameArr);
        } else {
            rtnStr = winMove(gameArr);
        }
        return rtnStr;
    }

    public String winMove(Character[][] gameArr){
        String gameString = arrToString(gameArr);

        Pattern xPat31 = Pattern.compile("(^\\D{6}X{2}\\s)|(^\\D{2}\\s\\D{2}X\\D{2}X)|(^\\D{2}\\s[^0]X\\DX\\D{2})");
        Pattern oPat31 = Pattern.compile("(^\\D{6}O{2}\\s)|(^\\D{2}\\s\\D{2}O\\D{2}O)|(^\\D{2}\\s[^0]O\\DO\\D{2})");

        Pattern xPat23 = Pattern.compile("(^\\D{6}X\\sX)|(^\\DX\\D{2}X\\D{2}\\s)");
        Pattern oPat23 = Pattern.compile("(^\\D{6}O\\sO)|(^\\DO\\D{2}O\\D{2}\\s)");

        Pattern xPat13 = Pattern.compile("(^\\D{6}\\sX{2})|(^\\s\\D{2}X\\D{2}X\\D{2})|(^X\\D{2}\\s\\D{2}X\\D{2})|(^X\\D{2}X\\D{2}\\s\\D{2})|(^\\D{2}X\\DX\\D\\s\\D{2})");
        Pattern oPat13 = Pattern.compile("(^\\D{6}\\sO{2})|(^\\s\\D{2}O\\D{2}O\\D{2})|(^O\\D{2}\\s\\D{2}O\\D{2})|(^O\\D{2}O\\D{2}\\s\\D{2})|(^\\D{2}O\\DO\\D\\s\\D{2})");

        Pattern xPat32 = Pattern.compile("(^\\D{3}X{2}\\s\\D{3})|(^\\D{2}X\\D{2}\\s\\D{2}X)");
        Pattern oPat32 = Pattern.compile("(^\\D{3}O{2}\\s\\D{3})|(^\\D{2}O\\D{2}\\s\\D{2}O)");

        Pattern xPat22 = Pattern.compile("(^\\D{3}X\\sX\\D{3})|(^\\DX\\D{2}\\s\\D{2}X\\D)|(^X\\D{3}\\s\\D{3}X)|(^\\D{2}X\\D\\s\\DX\\D{2})");
        Pattern oPat22 = Pattern.compile("(^\\D{3}O\\sO\\D{3})|(^\\DO\\D{2}\\s\\D{2}O\\D)|(^O\\D{3}\\s\\D{3}O)|(^\\D{2}O\\D\\s\\DO\\D{2})");

        Pattern xPat12 = Pattern.compile("(^\\D{3}\\sX{2}\\D{3})|(^X\\D{2}\\s\\D{2}X\\D{2})");
        Pattern oPat12 = Pattern.compile("(^\\D{3}\\sO{2}\\D{3})|(^O\\D{2}\\s\\D{2}O\\D{2})");

        Pattern xPat33 = Pattern.compile("(^\\D{6}X{2}\\s)|(^\\D{2}X\\D{2}X\\D{2}\\s)|(^X\\D{3}X\\D{3}\\s)");
        Pattern oPat33 = Pattern.compile("(^\\D{6}O{2}\\s)|(^\\D{2}O\\D{2}O\\D{2}\\s)|(^O\\D{3}O\\D{3}\\s)");

        Pattern xPat21 = Pattern.compile("(^X\\sX\\D{6})|(^\\D\\s\\D{2}X\\D{2}X\\D)");
        Pattern oPat21 = Pattern.compile("(^O\\sO\\D{6})|(^\\D\\s\\D{2}O\\D{2}O\\D)");

        Pattern xPat11 = Pattern.compile("(^\\D{6}\\sX{2})|(^X\\D{2}X\\D{2}\\s\\D{2})|(^\\D{2}X\\DX\\D\\s\\D{2})");
        Pattern oPat11 = Pattern.compile("(^\\D{6}\\sO{2})|(^O\\D{2}O\\D{2}\\s\\D{2})|(^\\D{2}O\\DO\\D\\s\\D{2})");

        String rtnStr = null;
        if ((xPat33.matcher(gameString).find() || oPat33.matcher(gameString).find()) && rtnStr == null) {
//                3,3
            rtnStr = "3 3";
        } else if ((xPat23.matcher(gameString).find() || oPat23.matcher(gameString).find()) && rtnStr == null) {
            //                2,3
            rtnStr = "2 3";
        } else if ((xPat13.matcher(gameString).find() || oPat13.matcher(gameString).find()) && rtnStr == null) {
            //                1,3
            rtnStr = "1 3";
        } else if ((xPat32.matcher(gameString).find() || oPat32.matcher(gameString).find()) && rtnStr == null) {
            //                3,2
            rtnStr = "3 2";
        } else if ((xPat22.matcher(gameString).find() || oPat22.matcher(gameString).find()) && rtnStr == null) {
            //                2,2
            rtnStr = "2 2";
        } else if ((xPat12.matcher(gameString).find() || oPat12.matcher(gameString).find()) && rtnStr == null) {
            //                1,2
            rtnStr = "1 2";
        } else if ((xPat31.matcher(gameString).find() || oPat31.matcher(gameString).find()) && rtnStr == null) {
            //                3,1
            rtnStr = "3 1";
        } else if ((xPat21.matcher(gameString).find() || oPat21.matcher(gameString).find()) && rtnStr == null) {
            //                2,1
            rtnStr = "2 1";
        } else if ((xPat11.matcher(gameString).find() || oPat11.matcher(gameString).find()) && rtnStr == null) {
//                1,1
            rtnStr = "1 1";
        }
        if (rtnStr == null) {
            rtnStr = " ";
        }
        return rtnStr;
    }

    public String arrToString(Character[][] gameArr) {
        String gameString = Arrays.toString(gameArr[0])+Arrays.toString(gameArr[1])+Arrays.toString(gameArr[2]);
        gameString = gameString.replaceAll(",\\s|]|\\[", "");
        return gameString;
    }

}

class Hard extends Medium{

    public Hard(char playerMark) {
        super(playerMark);
        this.playerType = "hard";
    }

    @Override
    public String enterMove(Character[][] gameArr){
        return bestMove(gameArr);

    }

    private String bestMove(Character[][] board) {

        // AI to make its turn
        int bestScore = Integer.MIN_VALUE;
        String move = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Is the spot available?
//                System.out.println("board["+j+"]["+i+"]: " + board[j][i]);
                if (board[j][i] == ' ') {
                    board[j][i] = 'X';
                    int score = minimax(board, 0, false);
                    board[j][i] = ' ';
//                    System.out.println("score: "+ score + " pair: "+ (i+1) + "" + (j+1) + " playerMark: "+playerMark);
                    if (score > bestScore) {
                        bestScore = score;
                        move = (i+1) + " " + (j+1);
                    }
                }
            }
        }
//        System.out.println("3 1:"+ board[0][2] +"|");
//        System.out.println("move: " + move);
        return move;
    }

    private int minimax(Character[][] board, int depth, boolean isMaximizing) {
        if (gameOver(arrToString(board)) != ' ') {
            if (!isMaximizing) {
                int bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        // Is the spot available?
                        if (board[j][i] == ' ') {
                            board[j][i] = 'O';
                            int score = minimax(board, depth + 1, true);
                            board[j][i] = ' ';
                            bestScore = Math.max(score, bestScore);
                        }
                    }
                }
//                System.out.println("bestScore1: " + bestScore);
                return bestScore;
            } else {
                char oppMark;
                if (playerMark == 'X') oppMark = 'O';
                else {
                    oppMark = 'X';
                }
                int bestScore = Integer.MAX_VALUE;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        // Is the spot available?
                        if (board[j][i] == ' ') {
                            board[j][i] = 'X';
                            int score = minimax(board, depth + 1, false);
                            board[j][i] = ' ';
                            bestScore = Math.min(score, bestScore);
                        }
                    }
                }
//                System.out.println("bestScore2: " + bestScore);
                return bestScore;
            }
        } else {
            if (gameOver(arrToString(board)) == playerMark) {
                return 10;
            } else {
                return -10;
            }
        }
    }

    public static char gameOver(String gameString) {
        Pattern xWins = Pattern.compile("(^X{3}\\D{6})|(^\\D{3}X{3}\\D{3})|(^\\D{6}X{3})|(^X\\D{2}X\\D{2}X\\D{2})|(^\\DX\\D{2}X\\D{2}X\\D)|(^\\D{2}X\\D{2}X\\D{2}X)|(^X\\D{3}X\\D{3}X)|(^\\D{2}X\\DX\\DX\\D{2})");
        Pattern oWins = Pattern.compile("(^O{3}\\D{6})|(^\\D{3}O{3}\\D{3})|(^\\D{6}O{3})|(^O\\D{2}O\\D{2}O\\D{2})|(^\\DO\\D{2}O\\D{2}O\\D)|(^\\D{2}O\\D{2}O\\D{2}O)|(^O\\D{3}O\\D{3}O)|(^\\D{2}O\\DO\\DO\\D{2})");

//        System.out.println("X: "+ xWins.matcher(gameString).find());
//        System.out.println("O: "+ oWins.matcher(gameString).find());

        char rtnChar = ' ';
        if (xWins.matcher(gameString).find()) {
            rtnChar = 'X';
        }
        if (oWins.matcher(gameString).find()) {
            rtnChar = 'O';
        }
        return rtnChar;
    }
}
