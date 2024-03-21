import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {

    static int n;
    static int m;
    static Point[][] map;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // init
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

        //play
        int answer = 0;
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            answer += playGame(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }

        //print
        System.out.print(answer);
    }

    static int playGame(int d, int p) {
        int score = attack(d, p);
        while(true) {
        	arrange(collect());
            int removeScore = remove();
            if(removeScore == 0) {
                break;
            }
            score += removeScore;
        }
        add(collect());
        return score;
    }

    static int attack(int d, int p) {
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};

        int[] arr = new int[4];
        int r = n/2;
        int c = n/2;
        for(int i = 0; i < p; i++) {
            r += dr[d];
            c += dc[d];

            arr[map[r][c].value]++;
            map[r][c].value = 0;
        }
        
        int score = 0;
        for(int i = 0; i < 4; i++) {
        	score += i * arr[i];
        }
        return score;
    }
    
    static Stack<Integer> collect() {
    	Stack<Integer> stack = new Stack<>();
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};
        int r = 0;
        int c = 0;
        int dir = 0;
        boolean[][] visited = new boolean[n][n];
        for(int i = 0; i < n*n; i++) {
            int nr = r + dr[dir];
            int nc = c + dc[dir];

            if(nr < 0 || nr == n || nc < 0 || nc == n || visited[nr][nc]) {
                dir = (dir + 1) % 4;
            }

            if(map[r][c].value != 0) {
            	stack.add(map[r][c].value);
                map[r][c].value = 0;
            }

            visited[r][c] = true;
            r = r + dr[dir];
            c = c + dc[dir];
        }
        return stack;
    }

    static void arrange(Stack<Integer> stack) {
    	int r = n/2;
    	int c = n/2;
    	map[r][c].value = 100;
    	int[] dr = {0, 1, 0, -1};
    	int[] dc = {-1, 0, 1, 0};
    	int dir = 0;
    	
    	while(!stack.isEmpty()) {
    		int nr = r + dr[dir];
    		int nc = c + dc[dir];
    		
    		if(nr == -1 || nc == -1) {
    			break;
    		}
    		
    		if(map[nr][nc].value == 0) {
    			map[nr][nc].value = stack.pop();
    			dir = (dir + 1) % 4;
    		} else {
    			dir = dir - 1;
    			if(dir == -1) {
    				dir = 3;
    			}
    			continue;
    		}
			r = nr;
			c = nc;
    	}
    	
    	map[n/2][n/2].value = 0;
    }

    static int remove() {
    	int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};
        int r = 0;
        int c = 0;
        int dir = 0;
        boolean[][] visited = new boolean[n][n];
        
        List<Point> points = new ArrayList<>();
        int current = map[r][c].value;
        int count = 1;
        int score = 0;
        for(int i = 0; i < n*n; i++) {
        	if(current != map[r][c].value) {
            	if(count >= 4) {
            		for(Point point : points) {
            			point.value = 0;
            		}
            		score += count * current;
            	}
            	count = 1;
            	current = map[r][c].value;
            	points.clear();
            	points.add(map[r][c]);
            } else {
            	count++;
            	points.add(map[r][c]);
            }
            
            visited[r][c] = true;
            r = r + dr[dir];
            c = c + dc[dir];
            
            int nr = r + dr[dir];
            int nc = c + dc[dir];

            if(nr < 0 || nr == n || nc < 0 || nc == n || visited[nr][nc]) {
                dir = (dir + 1) % 4;
            }
        }
        
        return score;
    }

    static void add(Stack<Integer> stack) {
    	if(stack.isEmpty()) {
    		return;
    	}
    	
    	Stack<Integer> tmp = new Stack<>();
    	while(!stack.isEmpty()) {
    		tmp.add(stack.pop());
    	}
    	
    	Stack<Integer> addStack = new Stack<>();
    	
    	int current = tmp.pop();
    	int count = 1;
    	
    	while(!tmp.isEmpty()) {
    		int n = tmp.pop();
    		if(n == current) {
    			count++;
    		} else {
    			addStack.add(current);
    			addStack.add(count);
    			
    			current = n;
    			count = 1;
    		}
    	}
    	addStack.add(current);
		addStack.add(count);
    	arrange(addStack);
    }

    static void print() {
        for(int r = 0; r < n; r++) {
            for(int c = 0; c < n; c++) {
                System.out.print(map[r][c]);
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

    Point(int r, int c, int value) {
        this.r = r;
        this.c = c;
        this.value = value;
    }

    @Override
    public String toString() {
    	return value + " ";
    }
}