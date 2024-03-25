import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int N;
    static int[][] map;
    static boolean[] visited;
    static Student[] students;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        map = new int[N][N];
        visited = new boolean[N*N + 1];
        students = new Student[N*N + 1];
        for (int i = 0; i < N*N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int id = Integer.parseInt(st.nextToken());
            int n1 = Integer.parseInt(st.nextToken());
            int n2 = Integer.parseInt(st.nextToken());
            int n3 = Integer.parseInt(st.nextToken());
            int n4 = Integer.parseInt(st.nextToken());
            students[id] = new Student(id, n1, n2, n3, n4);
            visited[id] = true;

            int count = 0;
            if(visited[n1]) count++;
            if(visited[n2]) count++;
            if(visited[n3]) count++;
            if(visited[n4]) count++;

            place(id, count);
        }

        System.out.print(getAnswer());
    }
    
    static void place(int id, int count) {
        if(count == 0) {
            placeZeroCount(id);
            return;
        }

        Student current = students[id];
        int[] dr = {1, 0, -1, 0};
        int[] dc = {0, 1, 0, -1};

        int maxClose = -1;
        int maxEmpty = -1;
        int endR = -1;
        int endC = -1;
        for(int r = 0; r < N; r++) {
            for(int c = 0; c < N; c++) {

                if(map[r][c] != 0) {
                    continue;
                }

                int empty = 0;
                int close = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nr = r + dr[dir];
                    int nc = c + dc[dir];

                    if(!isRange(nr, nc)) {
                        continue;
                    }

                    if(map[nr][nc] == 0) {
                        empty++;
                        continue;
                    }

                    if(current.isClose(map[nr][nc])) {
                        close++;
                    }
                }

                if(maxClose < close) {
                    endR = r;
                    endC = c;
                    maxClose = close;
                    maxEmpty = empty;
                    continue;
                }

                if(maxClose == close) {
                    if(maxEmpty < empty) {
                        endR = r;
                        endC = c;
                        maxClose = close;
                        maxEmpty = empty;
                        continue;
                    }
                }
            }
        }

        map[endR][endC] = id;
    }

    static void placeZeroCount(int id) {
        int[] dr = {1, 0, -1, 0};
        int[] dc = {0, 1, 0, -1};

        int maxCount = -1;
        int endR = -1;
        int endC = -1;
        for(int r = 0; r < N; r++) {
            for(int c = 0; c < N; c++) {
                
                if(map[r][c] != 0) {
                    continue;
                }

                int count = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nr = r + dr[dir];
                    int nc = c + dc[dir];

                    if(isRange(nr, nc) && map[nr][nc] == 0) {
                        count++;
                    }
                }

                if(count == 4) {
                    map[r][c] = id;
                    return;
                }

                if(maxCount < count) {
                    endR = r;
                    endC = c;
                    maxCount = count;
                }
            }
        }

        map[endR][endC] = id;
    }

    static int getAnswer() {
        int[] score = {0, 1, 10, 100, 1000};
        int[] dr = {1, 0, -1, 0};
        int[] dc = {0, 1, 0, -1};
        int sum = 0;
        for(int r = 0; r < N; r++) {
            for(int c = 0; c < N; c++) {
                Student current = students[map[r][c]];
                int count = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nr = r + dr[dir];
                    int nc = c + dc[dir];

                    if(isRange(nr, nc) && current.isClose(map[nr][nc])) {
                        count++;
                    }
                }
                sum += score[count];
            }
        }
        return sum;
    }

    static boolean isRange(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }
}

class Student {

    int id;
    int[] closeFriends;
    
    Student(int id, int n1, int n2, int n3, int n4) {
        this.id = id;

        closeFriends = new int[4];
        closeFriends[0] = n1;
        closeFriends[1] = n2;
        closeFriends[2] = n3;
        closeFriends[3] = n4;
    }

    boolean isClose(int input) {
        for(int n : closeFriends) {
            if(n == input) {
                return true;
            }
        }
        return false;
    }
}