import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int n;
    static int[][] map;
    static Person[] persons;
    static Queue<Person> movePersons;
    static Queue<int[]> closeStores;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        map = new int[n][n];

        int m = Integer.parseInt(st.nextToken());
        persons = new Person[m+1];

        for(int r = 0; r < n; r++) {
            st = new StringTokenizer(br.readLine());
            for(int c = 0; c < n; c++) {
                map[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine());
            persons[i] = new Person(i, 
            Integer.parseInt(st.nextToken())-1, 
            Integer.parseInt(st.nextToken())-1);
        }

        movePersons = new ArrayDeque<>();
        closeStores = new ArrayDeque<>();
        System.out.print(play());
    }    

    static int play() {
        int time = 0;
        while(!movePersons.isEmpty() || time == 0) {
            time++;
            move();
            close();
            add(time);
        }

        return time;
    }

    static void move() {
        int size = movePersons.size();
        while(size-- > 0) {
            moveEachPerson(movePersons.poll());
        }
    }

    static void moveEachPerson(Person person) {
        int[] dr = {-1, 0, 0, 1};
        int[] dc = {0, -1, 1, 0};
        
        for(int i = 0; i < 4; i++) {
            if(person.isStore(person.r + dr[i], person.c + dc[i])) {
                closeStores.add(new int[]{person.storeR, person.storeC});
                return;
            }
        }

        int[] distance = new int[4];
        Arrays.fill(distance, Integer.MAX_VALUE);
        for(int i = 0; i < 4; i++) {
            int r = person.r + dr[i];
            int c = person.c + dc[i];

            if(!isRange(r, c)) {
                continue;
            }

            if(map[r][c] == 2) {
                continue;
            }

            Queue<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{r, c});

            boolean[][] visited = new boolean[n][n];
            boolean flag = false;
            visited[r][c] = true;
            int count = 0;
            while(!queue.isEmpty()) {

                int size = queue.size();
                while(size-- > 0) {
                    int[] point = queue.poll();
                    for(int dir = 0; dir < 4; dir++) {
                        int nr = point[0] + dr[dir];
                        int nc = point[1] + dc[dir];

                        if(!isRange(nr, nc)) {
                            continue;
                        }

                        if(visited[nr][nc]) {
                            continue;
                        }

                        if(map[nr][nc] == 2) {
                            continue;
                        }
                        
                        if(person.isStore(nr, nc)) {
                            flag = true;
                            break;
                        }

                        if(map[nr][nc] == 0 || map[nr][nc] == 1) {
                            queue.add(new int[]{nr, nc});
                            visited[nr][nc] = true;
                        }
                    }
                    
                    if(flag) {
                        break;
                    }
                }

                if(flag) {
                    break;
                }

                count++;
            }

            if(flag) {
                distance[i] = count;
            }
        }
        
        int minCount = Integer.MAX_VALUE;
        int dir = -1;
        for(int i = 0; i < 4; i++) {
            if(minCount > distance[i]) {
                minCount = distance[i];
                dir = i;
            }
        }
        
        if(dir != -1) {
            person.r += dr[dir];
            person.c += dc[dir];
            movePersons.add(person);
        }
    }

    static void close() {
        while(!closeStores.isEmpty()) {
            int[] point = closeStores.poll();
            map[point[0]][point[1]] = 2;
        }
    }

    static void add(int time) {
        if(time >= persons.length) {
            return;
        }
        
        Person person = persons[time];
        int[] startPoint = selectBasecamp(new int[]{person.storeR, person.storeC});
        person.r = startPoint[0];
        person.c = startPoint[1];
        
        map[startPoint[0]][startPoint[1]] = 2;
        movePersons.add(person);
    }

    static int[] selectBasecamp(int[] storePoint) {
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, -1, 0, 1};

        PriorityQueue<int[]> pq = new PriorityQueue<>(
            new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    if(o1[0] != o2[0]) {
                        return o1[0] - o2[0];
                    }
                    return o1[1] - o2[1];
                }
            }
        );
        
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(storePoint);
        
        boolean[][] visited = new boolean[n][n];
        visited[storePoint[0]][storePoint[1]] = true;
        boolean find = false;
        while(!queue.isEmpty()) {
            int size = queue.size();
            while(size-- > 0) {
                int[] point = queue.poll();
                for(int dir = 0; dir < 4; dir++) {
                    int nr = point[0] + dr[dir];
                    int nc = point[1] + dc[dir];

                    if(!isRange(nr, nc)) {
                        continue;
                    }

                    if(visited[nr][nc]) {
                        continue;
                    }

                    if(map[nr][nc] == 2) {
                        continue;
                    }

                    if(map[nr][nc] == 1) {
                        pq.add(new int[]{nr, nc});
                        find = true;
                    }

                    queue.add(new int[]{nr, nc});
                    visited[nr][nc] = true;
                }
            }

            if(find) {
                break;
            }
        }
        return pq.poll();
    }

    static boolean isRange(int r, int c) {
        return r >= 0 && r < n && c >= 0 && c < n;
    }

    static void print() {
        for(int r = 0; r < n; r++) {
            for(int c = 0; c < n; c++) {
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class Person {
    
    int id;
    int r;
    int c;
    int storeR;
    int storeC;

    Person(int id, int storeR, int storeC) {
        this.id = id;
        this.storeR = storeR;
        this.storeC = storeC;
    }

    boolean isStore(int nr, int nc) {
        return nr == storeR && nc == storeC;
    }

    @Override
    public String toString() {
        return "id: " + id + ", " + r + " " + c + " stor: " + storeR + " " + storeC;
    }

}

class Point {
    int r;
    int c;
}