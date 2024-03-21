import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static int[] dy = {0,1,0,-1};
    public static int[] dx = {1,0,-1,0};

    public static int[][] map;
    public static int n,m;
    public static Stack<Integer> stack;
    public static int count;
    public static Stack<Integer> countst;
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
        stack = new Stack<>();
        countst = new Stack<>();
        count = 0;
        for(int k=1;k<=length;k++){
            int ni = i+dy[dir]*k;
            int nj = j+dx[dir]*k;
            ans += map[ni][nj];
            map[ni][nj] = 0;
        }

        //달팽이 탐색
        i = n/2;
        j = n/2;

        //아래로 1, 오른쪽으로 0, 위로 3, 왼쪽 2
        Queue<Integer> q = new LinkedList<>();
        //각 껍질마다 4회전 해야함.
        int[] tempArr = {1,0,3,2};
        for(int k=1;k<=n/2;k++){
            j--;
            for(int l=0;l<4;l++){
                int cdir = tempArr[l];
                while(true){
                    if(map[i][j] != 0) {
                        addTOStack(map[i][j]);
                    }
                    i += dy[cdir];
                    j += dx[cdir];
                    if(Math.abs(i+dy[cdir]-n/2) > k || Math.abs(j+dx[cdir]-n/2) > k){
                        break;
                    }
                }
            }
            if(map[i][j] != 0) addTOStack(map[i][j]);
        }
        addTOStack(0);

        Stack<Integer> temp = new Stack<>();
        while(!stack.isEmpty()){
            temp.add(stack.pop());
        }

        int prev = -1;
        int count2 = -1;
        ArrayList<Integer> templis = new ArrayList<>();
        ArrayList<Integer> templisC = new ArrayList<>();
        while(!temp.isEmpty()){
            int cur = temp.pop();
            if(prev == cur){
                count2++;
            }else{
                if(prev != -1){
                    templis.add(prev);
                    templisC.add(count2);
                }
                count2 = 1;
                prev = cur;
            }
        }
        if(prev != -1){
            templis.add(prev);
            templisC.add(count2);
        }

        ArrayList<Integer> newlis = new ArrayList<>();
        for(int l=0;l<templis.size();l++){
            newlis.add(templisC.get(l));
            newlis.add(templis.get(l));
        }

        i = n/2;
        j = n/2;

        map = new int[n][n];

        int idx = 0;
        for(int k=1;k<=n/2;k++){
            if(idx == newlis.size()) break;
            j--;

            for(int l=0;l<4;l++){
                int cdir = tempArr[l];
                while(true){
                    if(idx == newlis.size()) break;
                    if(map[i][j] != 0) {
                        map[i][j] = newlis.get(idx++);
                    }
                    i += dy[cdir];
                    j += dx[cdir];
                    if(Math.abs(i+dy[cdir]-n/2) > k || Math.abs(j+dx[cdir]-n/2) > k){
                        break;
                    }
                }
            }

            if(idx == newlis.size()) break;
            map[i][j] = newlis.get(idx++);
        }
    }

    public static void addTOStack(int value){
        if(countst.isEmpty()) count = 0;
        else{
            count = countst.peek();
        }

        if(!stack.isEmpty() && stack.peek() == value){
            count++;
            stack.push(value);
            countst.push(count);
        }else{
            if(count >= 4){
                int temp = stack.peek();
                while(!stack.isEmpty() && stack.peek() == temp){
                    stack.pop();
                    countst.pop();
                    ans += temp;
                }
                if(value!=0)
                addTOStack(value);
            }else{
                if(value==0)return;
                count = 1;
                stack.push(value);
                countst.push(count);
            }
        }
    }
}