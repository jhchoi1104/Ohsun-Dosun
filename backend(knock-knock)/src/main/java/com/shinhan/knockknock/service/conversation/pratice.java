package com.shinhan.knockknock.service.conversation;

import java.util.*;
class Solution {
    int[] dr = {1,-1,0,0};
    int[] dc = {0,0,1,-1};
    int rL; int cL;
    boolean[][] visited;

    public boolean exist(char[][] board, String word) {
        rL = board.length;
        cL = board[0].length;
        visited = new boolean[rL][cL];
        int cnt =0;
        for(int r=0;r<rL;r++){
            for(int c=0;c<cL;c++){
                if(board[r][c]==word.charAt(0)) {
                    if(isTrue(board,word, r,c)) return true;
                }
            }
        }
        return false;
    }
    public boolean isTrue(char[][] board, String word, int r, int c) {
        // 첫방문
        visited[r][c] = true;
        Queue<int[]> que = new ArrayDeque<>();
        que.add(new int[]{r,c,0});
        // while
        while(!que.isEmpty()) {
            int[] cur = que.poll();
            for(int i=0;i<4;i++){
                int nr = cur[0]+dr[0];
                int nc = cur[1]+dc[0];
                int nl = cur[2]+1;
                if(isValid(nr,nc,board, nl, word)) {
                    que.add(new int[]{nr,nc,nl});
                    visited[nr][nc] = true;
                    System.out.println(board[nr][nc]);
                    if(board[nr][nl]==word.charAt(word.length()-1)) return true;
                }
            }
        }
        return false;
    }
    public boolean isValid(int nr, int nc, char[][] board, int nl, String word) {
        if(nr>=0&&nr<rL&&nc>=0&&nc<cL&&!visited[nr][nc]) {
            if(board[nr][nc]==word.charAt(nl)) {
                return true;
            }
        }
        return false;
    }
}