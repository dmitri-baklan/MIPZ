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
        if(checkHorizontalWinCondition(board, board[i][j], i, j)
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
        if(diagonalBeginingLineCondition(board, current, xPos, yPos, true)) {
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
        if(diagonalBeginingLineCondition(board, current, xPos, yPos, false)) {
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

    private boolean checkHorizontalWinCondition(Integer[][] board, int current, int xPos, int yPos) {
        int counter = 1;
        if(straightLineBeginingCondition(board, current, xPos, yPos, true)) {
            for(int i = yPos + 1; i < board.length; i++) {
                if(board[i] != current) {
                    break;
                }
                counter++;
            }
        }
        return counter == winLine;
    }

    private boolean checkVerticalWinCondition(Integer[][] board, int current, int xPos, int yPos) {
        int counter = 1;
        if(straightLineBeginingCondition(board, current, xPos, yPos, false)) {
            for(int i = xPos + 1; i < board.length; i++) {
                if(board[i][yPos] != current) {
                    break;
                }
                counter++;
            }
        }
        return counter == winLine;
    }

    private boolean diagonalLineBeginingCondition(Integer[][] board, int current, int xPos, int yPos, boolean isDescending) {
        int xPoseComparing = isDescending ? 0 : board.length - 1;
        int xPoseValueComparing = isDescending ? -1 : 0;
        return (xPos == xPoseComparing || yPos == 0) || (board[xPos + xPoseValueComparing][yPos - 1] != current);
    }

    private boolean straightLineBeginingCondition(Integer[][] board, int current, int xPos, int yPos, boolean isHorizontal) {
        int posToCompare = isHorizontal ? yPos : xPos;
        int xPosIndexCheck = isHorizontal ? 0 : 1;
        int yPosIndexCheck = isHorizontal ? 1 : 0;
        return (posToCompare == 0) || (board[xPos - xPosValueCheck][yPos - yPosIndexCheck] != current);
    }


}
