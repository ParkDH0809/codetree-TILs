import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static Present[] presents;
    static Belt[] belts;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int Q = Integer.parseInt(br.readLine());
        while(Q-- > 0) {
            StringTokenizer st =new StringTokenizer(br.readLine());
            switch(st.nextToken()) {
                case "100":
                    init(st);
                    break;
                case "200":
                    // sb.append(moveAll(st)).append("\n");
                    System.out.println(moveAll(st));
                    break;
                case "300":
                    // sb.append(moveOne(st)).append("\n");
                    System.out.println(moveOne(st));
                    break;
                case "400":
                    // sb.append(divide(st)).append("\n");
                    System.out.println(divide(st));
                    break;
                case "500":
                    // sb.append(getPresentInfo(st)).append("\n");
                    System.out.println(getPresentInfo(st));
                    break;
                case "600":
                    // sb.append(getBeltInfo(st)).append("\n");
                    System.out.println(getBeltInfo(st));
                    break;
            }
            // print();
        }
        System.out.print(sb);
    }

    static void init(StringTokenizer st) {
        int n = Integer.parseInt(st.nextToken());
        belts = new Belt[n+1];
        for(int i = 1; i <= n; i++) {
            belts[i] = new Belt(i);
        }
        
        int m = Integer.parseInt(st.nextToken());
        presents = new Present[m+1];
        for(int i = 1; i <= m; i++) {
            int beltNumber = Integer.parseInt(st.nextToken());
            presents[i] = new Present(i);
            belts[beltNumber].addLast(presents[i]);
        }
    }

    static int moveAll(StringTokenizer st)  {
        int src = Integer.parseInt(st.nextToken());
        int dst = Integer.parseInt(st.nextToken());
        if(belts[src].isEmpty()) {
            return belts[dst].size;
        }

        belts[dst].head.prev = belts[src].tail;
        belts[src].tail.next = belts[dst].head;

        belts[dst].head = belts[src].head;
        belts[dst].size += belts[src].size;

        belts[src].head = belts[src].tail = null;
        belts[src].size = 0;

        return belts[dst].size;
    }

    static int moveOne(StringTokenizer st) {
        int src = Integer.parseInt(st.nextToken());
        int dst = Integer.parseInt(st.nextToken());
        if(belts[src].isEmpty() && belts[dst].isEmpty()) {
            return 0;
        }

        if(belts[src].isEmpty()) {
            belts[src].addFirst(belts[dst].getOne());
            return belts[dst].size;
        }

        if(belts[dst].isEmpty()) {
            belts[dst].addFirst(belts[src].getOne());
            return belts[dst].size;
        }

        Present srcPresent = belts[src].getOne();
        Present dstPresent = belts[dst].getOne();
        
        belts[src].addFirst(dstPresent);
        belts[dst].addFirst(srcPresent);

        return belts[dst].size;
    }

    static int divide(StringTokenizer st) {
        int src = Integer.parseInt(st.nextToken());
        int dst = Integer.parseInt(st.nextToken());
        if(belts[src].size <= 1) {
            return belts[dst].size;
        }

        // Test
        int number = belts[src].size / 2;
        
        Present current = belts[src].head;
        Present firstPresent = current;
        for(int i = 1; i < number; i++) {
            current = current.next;
        }

        // src
        current.next.prev = null;
        belts[src].head = current.next;
        belts[src].size -= number;

        //dst
        belts[dst].head.prev = current;
        current.next = belts[dst].head;
        belts[dst].head = firstPresent;
        belts[dst].size += number;

        return belts[dst].size;
    }

    static int getPresentInfo(StringTokenizer st) {
        int present = Integer.parseInt(st.nextToken());
        return presents[present].getPresentInfo();
    }

    static int getBeltInfo(StringTokenizer st) {
        int belt = Integer.parseInt(st.nextToken());
        return belts[belt].getBeltInfo();
    }

    static void print() {
        System.out.println("beltInfo: ");
        for(int i = 1; i < belts.length; i++) {
            System.out.println(belts[i]);
        }
        System.out.println();
    }
}

class Belt {
    
    Present head;
    Present tail;
    // Present mid;
    int size;
    int id;

    Belt(int id) {
        this.id = id;
        head = tail = null;
        size = 0;
    }

    boolean isEmpty() {
        return size == 0;
    }

    void addFirst(Present present) {
        if(isEmpty()) {
            head = tail = present;
        } else {
            present.next = head;
            head.prev = present;
            head = present;
        }
        size++;
    }

    void addLast(Present present) {
        if(isEmpty()) {
            head = tail = present;
        } else {
            tail.next = present;
            present.prev = tail;
            tail = present;
        }
        size++;
    }

    Present getOne() {
        if(isEmpty()) {
            return null;
        }

        Present present = head;
        head = present.next;
        head.prev = null;
        present.next = null;
        size--;
        return present;
    }

    int getBeltInfo() {
        if(isEmpty()) {
            return -3;
        }

        int a = head.id;
        int b = tail.id;
        int c = size;
        return a + 2*b + 3*c;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Present current = head;
        while(current != null) {
            sb.append(current.id + "-");
            current = current.next;
        }
        return "id: " + id + ", size: " + size + ", " + sb;
    }

}

class Present {

    Present prev;
    Present next;
    int id;

    Present(int id) {
        this.id = id;
    }

    int getPresentInfo() {
        if(prev == null && next == null) {
            return -3;
        }

        if(prev == null) {
            return 2*next.id - 1;
        }

        if(next == null) {
            return prev.id - 2;
        }

        return prev.id + 2*next.id;
    }
    
}