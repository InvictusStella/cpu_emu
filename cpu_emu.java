import java.util.*;
import java.io.*;

public class cpu_emu {
    private static int ac;
    private static int pc = 0;
    private static int flag = 0;

    private static ArrayList<Integer> pcNum = new ArrayList<>();
    private static ArrayList<String> method = new ArrayList<>();
    private static ArrayList<String> vars = new ArrayList<>();

    private static int[] memory = new int[256];

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String filename = "program.txt";
        ac = 0;

        try {
            readMap(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        exec();
        long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start) + "ms");
    }

    public static void exec() {
        while(!method.get(pc).equals("START")) {
            pc++;
        }
        
        if(method.get(pc).equals("START")) {
            System.out.println("Program started");
            pc++;
        }

        while(!method.get(pc).equals("HALT")) {
            if(method.get(pc).equals("DISP")) {
                disp();
                pc++;
                continue;
            }

            int variable = Integer.parseInt(vars.get(pc));
            String methodName = method.get(pc);
            
            int result = 0;
            boolean jmpCheck = true;
            switch(methodName) {
                case "LOAD":
                    result = load(variable);
                    break;
                case "LOADM":
                    result = loadm(variable);
                    break;
                case "STORE":
                    store(variable);
                    break;
                case "CMPM":
                    cmpm(variable);
                    break;
                case "CJMP":
                    jmpCheck = cjmp(variable);
                    break;
                case "JMP":
                    jmpCheck = jmp(variable);
                    break;
                case "ADD":
                    result = add(variable);
                    break;
                case "ADDM":
                    result = addm(variable);
                    break;
                case "SUBM":
                    result = subm(variable);
                    break;
                case "SUB":
                    result = sub(variable);
                    break;
                case "MUL":
                    result = mul(variable);
                    break;
                case "MULM":
                    result = mulm(variable);
                    break;

            }

            if(result < 0 || result > 255) {
                System.out.println("Error: Result out of bounds");
                System.exit(0);
            }
            if(!jmpCheck) {
                System.out.println("Error: Jump out of bounds");
                System.exit(0);
            }
            pc++;
        }

        System.out.println("Program halted");
        
    }

    public static void readMap(String fileName) throws Exception {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                char ch = parts[0].charAt(0);

                if(ch == '%') 
                    break;

                if (parts.length > 2) {
                    pcNum.add(Integer.parseInt(parts[0]));
                    method.add(parts[1].toUpperCase().trim());
                    vars.add(parts[2].toUpperCase().trim());
                }else {
                    pcNum.add(Integer.parseInt(parts[0]));
                    method.add(parts[1].toUpperCase().trim());
                    vars.add("null");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }

    private static int load(int param) {
        ac = param;
       
        return ac;
    }

    private static int loadm(int param) {
        ac = memory[param];
        
        return ac;
    }

    private static void store(int param) {
        memory[param] = ac;
        
    }

    private static void cmpm(int param) {
        if (ac > memory[param]) {
            flag = 1;
        } else if (ac < memory[param]) {
            flag = -1;
        }
       
    }

    private static boolean cjmp(int param) {
        if(param > pcNum.size()) {
           
            return false;
        }
        
        if (flag > 0) 
            pc = param - 1;
            
        return true;
        
    }

    private static boolean jmp(int param) {
        if(param > pcNum.size()) {
            
            return false;
        }
        
        pc = param - 1;
        
        return true;
    }

    private static int add(int param) {
        ac = ac + param;
       
        return ac;
    }

    private static int addm(int param) {
        ac = ac + memory[param];
       
        return ac;
    }

    private static int subm(int param) {
        ac = ac - memory[param];
        
        return ac;
    }

    private static int sub(int param) {
        ac = ac - param;
       
        return ac;
    }

    private static int mul(int param) {
        ac = ac * param;
        
        return ac;
    }

    private static int mulm(int param) {
        ac = ac * memory[param];
        
        return ac;
    }

    private static void disp() {
        System.out.println(ac);
       
    }
    
}
