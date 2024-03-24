import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int n;
    static int m;
    static Point[][] map;
    static Queue<Point> startPoints;
    static List<Point> endPoints;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        //init
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new Point[n][n];
        for(int r = 0; r < n; r++) {
            st = new StringTokenizer(br.readLine());
            for(int c = 0; c < n; c++) {
                map[r][c] = new Point(r, c, Integer.parseInt(st.nextToken()));
            }
        }
        
        startPoints = new ArrayDeque<>();
        startPoints.add(map[n-1][0]);
        startPoints.add(map[n-1][1]);
        startPoints.add(map[n-2][0]);
        startPoints.add(map[n-2][1]);

        //play
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            move(Integer.parseInt(st.nextToken())-1, Integer.parseInt(st.nextToken()));
            add();
            absorb();
            getNewStartPoints();
            // print();
        }

        //print
        int answer = 0;
        for(int r = 0; r < n; r++) {
            for(int c = 0; c < n; c++) {
                answer += map[r][c].value;
            }
        }
        System.out.print(answer);
    
    }

    static void move(int d, int p) {
        int[] dr = {0, -1, -1, -1, 0, 1, 1, 1};
        int[] dc = {1, 1, 0, -1, -1, -1, 0, 1};

        endPoints = new ArrayList<>();
        while(!startPoints.isEmpty()) {
            Point point = startPoints.poll();

            int nr = point.r + dr[d] * p;
            int nc = point.c + dc[d] * p;
            if(nr < 0) {
                nr += n;
            }

            if(nr >= n) {
                nr -= n;
            }

            if(nc < 0) {
                nc += n;
            }
            
            if(nc >= n) {
                nc -= n;
            }

            endPoints.add(map[nr][nc]);
        }
    }

   static void add() {
        for(Point point : endPoints) {
            point.value++;
            point.isUsed = true;
        }
    }

    static void absorb() {
        int[] dr = {1, 1, -1, -1};
        int[] dc = {1, -1, 1, -1};

        for(Point point : endPoints) {
            int r = point.r;
            int c = point.c;

            for(int dir = 0; dir < 4; dir++) {
                int nr = r + dr[dir];
                int nc = c + dc[dir];
                
                if(!(nr < 0 || nr == n || nc < 0 || nc == n) && map[nr][nc].value > 0) {
                    point.value++;
                }
            }
        }
        
        endPoints.clear();
    }

    static void getNewStartPoints() {
        for(int r = 0; r < n; r++) {
            for(int c = 0; c < n; c++) {
                if(map[r][c].isUsed) {
                    map[r][c].isUsed = false;
                    continue;
                }

                if(map[r][c].value >= 2) {
                    map[r][c].value -= 2;
                    startPoints.add(map[r][c]);
                }
            }
        }
    }

    static void print() {
        for(int r = 0; r < n; r++) {
            for(int c = 0; c < n; c++) {
                System.out.print(map[r][c].value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class Point {

    int r;
    int c;
    int value;
    boolean isUsed;

    Point(int r, int c, int value) {
        this.r = r;
        this.c = c;
        this.value = value;
    }

    @Override
    public String toString() {
        return r + " " + c + " " + value;
    }
}