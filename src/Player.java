import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */

    int xy;
    Deadline deadLine;

    public GameState play(final GameState gameState, final Deadline deadline) {

        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        //this.deadLine = deadline;
        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        int depth = 8;
        xy = gameState.getNextPlayer();

        //int player = gameState.getNextPlayer();

        double bestScore = 0.0;
        int bestIndex = 0;

        for (int i = 0; i < nextStates.size(); i++) {
            //System.err.println("dsa");
            double score = alphabeta(nextStates.get(i), depth, alpha, beta, 2);


            if (score > bestScore){
                bestIndex = i;
                bestScore = score;
            }
        }

        //System.err.println(nextStates.elementAt(bestIndex).toMessage());

        return nextStates.elementAt(bestIndex);
    }

    double alphabeta(GameState gameState, int depth, double alpha, double beta, int player){
        double v = 0.0;
        //System.err.println("deadline: " + this.deadLine.timeUntil());
        //System.err.println(gameState.getNextPlayer());

        depth--;



        Vector<GameState> nxtStates = new Vector<GameState>();
        gameState.findPossibleMoves(nxtStates);

        if (depth == 0 || nxtStates.size() == 0) {
            v = newEval(gameState, depth);
        }

        else if (player == 1) {
                //System.err.println("A");
                v = -100000.0;
                for (int i = 0; i < nxtStates.size(); i++) {
                    v = Math.max(v, alphabeta(nxtStates.elementAt(i), depth, alpha, beta, 2));
                    alpha = Math.max(alpha, v);
                    if (beta <= alpha){
                        break;
                    }
                }
            }
        else if (player == 2){
            //System.err.println("B");
            v = 100000.0;
            for (int i = 0; i < nxtStates.size(); i++) {
                v = Math.min(v, alphabeta(nxtStates.elementAt(i), depth, alpha, beta, 1));
                beta = Math.min(beta, v);
                if (beta <= alpha){
                    break;
                }
            }
        }

        return v;
    }

    int newEval(GameState g, int depth){

        if (g.isEOG()){
            if (g.isXWin()){
                return 10 + depth;
            }else if (g.isOWin()){
                return -10 - depth;
            }
        }
        return 0;
    }


    int evalFunc2(GameState gameState, int player){
        int boardSize = gameState.BOARD_SIZE;
        int xscore = 0;
        int yscore = 0;

        //System.err.println("xy: " + xy);
        // rows and cols
        for (int x = 1; x <= 2; x++) {
            int score = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (gameState.at(i+j) == x){
                        score += 2;
                        if (i == 0 || i == 3){
                            if(j == 0 || j == 3){
                                score++;
                            }
                        }else if (i == 1 || i == 2){
                            if (j == 1 || j == 2){
                                if (gameState.at(boardSize*i + j) == 1){
                                    score++;
                                }
                            }
                        }
                    }
                }
            }
            if (x == 1){
                xscore = score;
            }else if (x == 2){
                yscore = 2;
            }
        }

        //System.err.println(score);

        int plusMinus = player == 1 ? 1 : -1;

        return (xscore - yscore) * plusMinus;

    }

    int evalFunc(GameState gameState){
        //System.err.println(this.xy);
        int xscore = 0;
        int yscore = 0;

        int boardSize = gameState.BOARD_SIZE;

        for (int x = 1; x <= 1; x++) {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    //System.err.println("pos: " + gameState.at(boardSize*i + j));
                    if (gameState.at(boardSize*i + j) != 0){


                        if(i == 0 || i == boardSize - 1){
                            if(j == 0 || j == (boardSize - 1)){
                                if (gameState.at(boardSize*i + j) == 1){
                                    xscore += 3;
                                }else {
                                    yscore += 3;
                                }
                            }
                        }else if (i == 1 || i == 2){
                            if (j == 1 || j == 2){
                                if (gameState.at(boardSize*i + j) == 1){
                                    xscore += 3;
                                }else {
                                    yscore += 3;
                                }
                            }
                        }else {
                            if (gameState.at(boardSize*i + j) == 1){
                                xscore += 2;
                            }else {
                                yscore += 2;
                            }
                        }
                    }
                }
            }
        }

        //System.err.println("x: " + xscore);
        //System.err.println("y: " + yscore);

        //int plusMinus = xy == 1 ? 1 : -1;
        //System.err.println((xscore - yscore) * plusMinus);

        return xscore;
    }
}
