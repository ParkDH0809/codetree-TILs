import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N;
    static int[][] map;
    static Robot robot;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        map = new int[N][N];
        for(int r = 0; r < N; r++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int c = 0; c < N; c++) {
                map[r][c] = Integer.parseInt(st.nextToken());
                if(map[r][c] == 9) {
                    robot = new Robot(r, c);
                    map[r][c] = 0;
                    continue;
                }
            }
        }

        int answer = 0;
        Monster monster = null;
        while((monster = search()) != null) {
            answer += monster.distance;
            robot.r = monster.r;
            robot.c = monster.c;
            map[robot.r][robot.c] = 0;
            robot.upExp();
        }

        System.out.print(answer);
    }

    static Monster search() {
        int[] dr = {1, 0, -1, 0};
        int[] dc = {0, 1, 0, -1};

        PriorityQueue<Monster> lowerMonsters = new PriorityQueue<>();
        
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{robot.r, robot.c});
        
        boolean[][] visited = new boolean[N][N];
        visited[robot.r][robot.c] = true;

        boolean searchFlag = false;
        int distance = 1;
        while(!queue.isEmpty()) {
            int size = queue.size();
            while(size -- > 0) {
                int[] point = queue.poll();
                for(int dir = 0; dir < 4; dir++)  {
                    int nr = point[0] + dr[dir];
                    int nc = point[1] + dc[dir];

                    if(!isRange(nr, nc)) {
                        continue;
                    }

                    if(visited[nr][nc]) {
                        continue;
                    }

                    if(map[nr][nc] > robot.level) {
                        continue;
                    }

                    if(map[nr][nc] == 0) {
                        queue.add(new int[]{nr, nc});
                        visited[nr][nc] = true;
                        continue;
                    }

                    if(map[nr][nc] < robot.level) {
                        lowerMonsters.add(new Monster(nr, nc, distance));
                        searchFlag = true;
                    }
                }
            }

            if(searchFlag) {
                return lowerMonsters.poll();
            }

            distance++;
        }
        return null;
    }

    static boolean isRange(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }
}

class Robot {

    int r;
    int c;
    int level;
    int exp;

    Robot(int r, int c) {
        this.r = r;
        this.c = c;
        this.level = 2;
        this.exp = 0;
    }

    void upExp() {
        exp++;
        if(exp == level) {
            level++;
            exp = 0;
        }
    }
}

class Monster implements Comparable<Monster> {

    int r;
    int c;
    int distance;

    Monster(int r, int c, int distance) {
        this.r = r;
        this.c = c;
        this.distance = distance;
    }

    @Override
    public int compareTo(Monster o) {
        if(this.r != o.r) {
            return this.r - o.r;
        }
        return this.c - o.c;
    }
}