package org.example.service;

import org.example.entity.TestResult;

import java.util.List;
import java.util.stream.Collectors;

public class RenjuWinChecker {
    int winLine = 5;

    public List<TestResult> checkRenjuTests(List<Integer[][]> testCases) {
        return tesCases.stream().map(s -> checkWinner(s)).collect(Collectors.toList());
    }

    public TestResult checkWinner(Integer[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0) {
                    continue;
                }
                setUpWinnerData(board, i, j);
            }
        }
        return testResult;
    }

    private TestResult setUpWinnerData(Integer[][] board, int i, int j) {
        if(checkHorizontalWinCondition(board[i], board[i][j], j)
                || checkVerticalWinCondition(board, board[i][j], i, j)
                || checkDiagonalAscentWinCondition(board, board[i][j], i, j)
                || checkDiagonalDescentWinCondition(board, board[i][j], i, j)) {
            TestResult testResult = new TestResult();
            testResult.setWinner(board[i][j]);
            testResult.setHorizontalPos(i);
            testResult.setVerticalPos(j);
            return testResult;
        }
    }

    private boolean checkDiagonalDescentWinCondition(Integer[][] board, Integer current, int xPos, int yPos) {
        int counter = 1;
        if((xPos == 0 || yPos == 0) || (board[xPos - 1][yPos - 1] != current)) {
            int j = yPos + 1;
            for(int i = xPos + 1; i < board.length && j < board[i].length; i++) {
                if(board[i][j] != current) {
                    break;
                }
                counter++;
                j++;
            }
        }
        return counter == winLine;
    }

    private boolean checkDiagonalAscentWinCondition(Integer[][] board, Integer current, int xPos, int yPos) {
        int counter = 1;
        if((xPos == board.length - 1 || yPos == 0) || (board[xPos + 1][yPos - 1] != current)) {
            int j = yPos + 1;
            for(int i = xPos - 1; i >= 0 && j < board[i].length; i--) {
                if(board[i][j] != current) {
                    break;
                }
                counter++;
                j++;
            }
        }
        return counter == winLine;
    }

    public boolean checkHorizontalWinCondition(Integer[] board, int current, int yPos) {
        int counter = 1;
        if((yPos == 0) || (board[yPos - 1] != current)) {
            for(int i = yPos + 1; i < board.length; i++) {
                if(board[i] != current) {
                    break;
                }
                counter++;
            }
        }
        return counter == winLine;
    }

    public boolean checkVerticalWinCondition(Integer[][] board, int current, int xPos, int yPos) {
        int counter = 1;
        if((xPos == 0) || (board[xPos - 1][yPos] != current)) {
            for(int i = xPos + 1; i < board.length; i++) {
                if(board[i][yPos] != current) {
                    break;
                }
                counter++;
            }
        }
        return counter == winLine;
    }
}
