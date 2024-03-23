import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static Queue<Sushi> sushies;
    static HashMap<String, Customer> customers;
    static int L;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        sushies = new ArrayDeque<>();
        customers = new HashMap<>();

        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        while(Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            switch(st.nextToken()) {
                case "100":
                    makeSushi(st);
                    break;
                case "200":
                    visitCustomer(st);
                    break;
                default:
                    sb.append(getAnswer(Integer.parseInt(st.nextToken())));
                    break;
            }
        }

        System.out.print(sb);
    }

    static void makeSushi(StringTokenizer st) {
        sushies.add(
                new Sushi(
                        Integer.parseInt(st.nextToken()), 
                        Integer.parseInt(st.nextToken()), 
                        st.nextToken()
                )
        );
    }

    static void visitCustomer(StringTokenizer st) {
        int time = Integer.parseInt(st.nextToken());
        int position = Integer.parseInt(st.nextToken());
        String name = st.nextToken();
        int count = Integer.parseInt(st.nextToken());
        customers.put(name, new Customer(time, position, count));
    }

    static StringBuilder getAnswer(int t) {
        StringBuilder sb = new StringBuilder();
        Queue<Sushi> newSushies = new ArrayDeque<>();

        while(!sushies.isEmpty()) {
            Sushi sushi = sushies.poll();
            
            if(!customers.containsKey(sushi.name)) {
                newSushies.add(sushi);
                continue;
            }

            Customer customer = customers.get(sushi.name);
            
            boolean flag = false;
            int sushiPosition = 0;
            // 손님이 먼저 온 경우
            if(customer.time <= sushi.time) {
                sushiPosition = sushi.position + (t - sushi.time);
                if(sushi.position <= customer.position && customer.position <= sushiPosition) {
                    flag = true;
                } 

                if(sushiPosition >= L) {
                    sushiPosition -= L;
                    if(sushiPosition >= customer.position) {
                        flag = true;
                    }
                }
            } else { // 손님이 나중에 온 경우
                int firstSushiPosition = (sushi.position + (customer.time - sushi.time)) % L;
                sushiPosition = firstSushiPosition + (t - customer.time);
                if(customer.position <= sushiPosition) {
                    flag = true;
                }   
            }

            if(flag) {
                customer.count--;
            } else {
                newSushies.add(sushi);
            }


            if(customer.count == 0) {
                customers.remove(sushi.name);
            }
        }

        // System.out.println(t + " " + newSushies.toString());
        sushies = newSushies;
        sb.append(customers.size())
        .append(" ")
        .append(newSushies.size())
        .append("\n");
        return sb;
    }
}

class Sushi {

    int time;
    int position;
    String name;
    
    Sushi(int time, int position, String name) {
        this.time = time;
        this.position = position;
        this.name = name;
    }

    public String toString() {
        return "sushi: " + name + " " + time + " " + position;
    }
}

class Customer {
    
    int time;
    int position;
    int count;

    Customer(int time, int position, int count) {
        this.time = time;
        this.position = position;
        this.count = count;
    }

    public String toString() {
        return position + " " + count;
    }
    
}