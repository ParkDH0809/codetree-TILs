import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Main
 */
public class Main {

    static int answer;
    static int N;
    static int r;
    static int c;
    static int currentSide;
    static int direction;
    static int[][] map;
    static Side[] sides;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        for(int r = 0;  r < N; r++) {
            st = new StringTokenizer(br.readLine());
            for(int c = 0; c < N; c++) {
                map[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        answer = 0;
        sides = new Side[7];
        sides[1] = new Side(5, 3, 2, 4);
        sides[2] = new Side(1, 3, 6, 4);
        sides[3] = new Side(5, 6, 2, 1);
        sides[4] = new Side(5, 1, 2, 6);
        sides[5] = new Side(6, 3, 1, 4);
        sides[6] = new Side(2, 3, 5, 4);
        r = 0;
        c = 0;
        currentSide = 6;
        direction = 1;
        
        while(m-- > 0) {
            play();
        }
        System.out.println(answer);
    }
    
    static void play() {
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        int nr = r + dr[direction];
        int nc = c + dc[direction];

        if(!isRange(nr, nc)) {
            direction = (direction + 2) % 4;
            r += dr[direction];
            c += dc[direction];
        } else {
            r = nr;
            c = nc;
        }
        
        answer += addScore(r, c);
        currentSide = sides[currentSide].close[direction];

        if(map[r][c] > currentSide) {
            direction -= 1;
            if(direction == -1) {
                direction = 3;
            }
        }

        if(map[r][c] < currentSide) {
            direction = (direction + 1) % 4;
        }
    }

    static int addScore(int r, int c) {
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{r, c});
        
        boolean[][] visited = new boolean[N][N];
        visited[r][c] = true;
        int count = 0;
        while(!queue.isEmpty()) {
            int[] current = queue.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nr = current[0] + dr[dir];
                int nc = current[1] + dc[dir];
     
                if(!isRange(nr, nc)) {
                    continue;
                }

                if(visited[nr][nc]) {
                    continue; 
                }

                if(map[nr][nc] != map[r][c]) {
                    continue;
                }

                visited[nr][nc] = true;
                queue.add(new int[]{nr, nc});
            }
            count++;
        }

        return count * map[r][c];
    }

    static boolean isRange(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }

}

class Side {
    
    int[] close;

    Side(int up, int right, int down, int left) {
        close = new int[4];

        close[0] = up;
        close[1] = right;
        close[2] = down;
        close[3] = left;

        // close[0] = down;
        // close[1] = left;
        // close[2] = up;
        // close[3] = right;
    }
}