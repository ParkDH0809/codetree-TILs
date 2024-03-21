import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Point{
    int count;
    int value;
    public Point(int value,int count){
        this.value = value;
        this.count = count;
    }
}

public class Main {
    public static int[] dy = {0,1,0,-1};
    public static int[] dx = {1,0,-1,0};

    public static int[][] map;
    public static int n,m;
    public static ArrayDeque<Point> stack;
    public static int ans;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][n];

        for(int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<n;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<m;i++){
            st = new StringTokenizer(br.readLine());
            int dir = Integer.parseInt(st.nextToken());
            int length = Integer.parseInt(st.nextToken());
            simulate(dir,length);
        }
        System.out.println(ans);
    }

    private static void simulate(int dir, int length) {
        //dir 방향으로 레이저를 쏴서 맵을 지우고, 중간에서 달팽이 탐색하며 큐에 쌓고,
        int i = n/2;
        int j = n/2;
        stack = new ArrayDeque<>();
        for(int k=1;k<=length;k++){
            int ni = i+dy[dir]*k;
            int nj = j+dx[dir]*k;
            ans += map[ni][nj];
            map[ni][nj] = -1;
        }

        //아래로 1, 오른쪽으로 0, 위로 3, 왼쪽 2
        Queue<Integer> q = new LinkedList<>();
        //각 껍질마다 4회전 해야함.
        int[] temp2 = {1,0,3,2};

        //달팽이 탐색
        i = n/2;
        j = n/2;
        for(int k=1;k<=n/2;k++){
            j--;
            for(int t=0;t<4;t++){
                int cdir = temp2[t];
                while(true){
                    if(map[i][j] == 0) break;
                    if(map[i][j] != -1) {
                        stack.addLast(new Point(map[i][j],1));
                    }
                    map[i][j] = 0;
                    i += dy[cdir];
                    j += dx[cdir];
                    if(Math.abs(i+dy[cdir]-n/2) > k || Math.abs(j+dx[cdir]-n/2) > k) {
                        break;
                    }
                }
                if(map[i][j] == 0) break;
            }
            if(map[i][j] == 0) break;
            if(map[i][j] != -1)stack.addLast(new Point(map[i][j],1));
            map[i][j] = 0;
        }            
        
        //합치기
        boolean flag = true;
        while(flag) {
            flag = false;
            ArrayDeque<Point> tempq = new ArrayDeque<>();
            //우선 뭉치고
            while(!stack.isEmpty()) {
                Point cur = stack.pollFirst();
                if(tempq.size() == 0) {
                    tempq.addLast(cur);
                }else {
                    //같을때
                    if(tempq.peekLast().value == cur.value) {
                        tempq.peekLast().count += cur.count;
                    }
                    //다를 때
                    else {
                        tempq.addLast(cur);
                    }
                }
            }
            
            //뭉친 temp에서 살아남은애들만 빼주고
            ArrayDeque<Point> tempq2 = new ArrayDeque<>();
            while(!tempq.isEmpty()) {
                Point cur = tempq.pollFirst();
                if(cur.count < 4) {
                    tempq2.addLast(cur);
                }else {
                    flag = true;
                    ans += cur.count*cur.value;
                }
            }
            stack = tempq2;
        } 
        

        Queue<Integer> pushq = new LinkedList<>();

        while(!stack.isEmpty()){
            Point cur = stack.pollFirst();
            pushq.add(cur.count);
            pushq.add(cur.value);
            if(pushq.size() == n*n-1) break;
        }

        //달팽이 탐색
        i = n/2;
        j = n/2;
        for(int k=1;k<=n/2;k++){
            if(pushq.size() == 0) break;
            j--;

            for(int t=0;t<4;t++){
                int cdir = temp2[t];
                while(true){
                    if(pushq.size() == 0) break;
                    map[i][j] = pushq.poll();
                    i += dy[cdir];
                    j += dx[cdir];
                    if(Math.abs(i+dy[cdir]-n/2) > k || Math.abs(j+dx[cdir]-n/2) > k){
                        break;
                    }
                }
            }
            if(pushq.size() == 0) break;
            map[i][j] = pushq.poll();
        }
    }

}