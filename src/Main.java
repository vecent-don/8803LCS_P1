import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    String[] nationality = new String[]{"Brit","Swede","Dane","Norwegian","German"};
    String[] color = new String[]{"red","green","white","yellow","blue"};

    String[] pets = new String[]{"dog","bird","cat","horse","fish"};
    String[] drink = new String[]{"tea","coffee","milk","beer","water"};
    String[] cigars = new String[]{"Pall Mall","Dunhill","Blend","Bluemaster","Prince"};

    String[][] ss = new String[][]{nationality,color,pets,drink,cigars};

    int x=5,y=5,z=5;

//    // runtime value info
//    boolean[][][] records = new boolean[x][y][z]; // kth type, ith value put in the jth loc
//    //  runtime visiting info
//    boolean[][][] vis = new boolean[x][y][z]; // kth type, ith value put in the jth loc

    public Node buildBasic(){
        // exclusive
        Node res = new ENode("Par","and");

        for(int k=0;k<x;k++){
            Node basic = new ENode("Par","and");
            // for every loc, it must contain values and contain at most 1 value;
            for(int j=0;j<z;j++){
                // must contain value;
                Node mustContain = new ENode("Par","or");
                for(int i=0;i<y;i++){
                    mustContain.list.add(new ENode(true,false,k,i,j));
                }
                basic.list.add(mustContain);

                // must contain at most 1 value
                Node mustUnique = new ENode("Par","and");
                for(int i1=0;i1<y;i1++){
                    for (int i2=i1+1;i2<y;i2++){
                        // mutually exclusive
                        Node a = new ENode(true,true,k,i1,j);
                        Node b = new ENode(true,true,k,i2,j);
                        Node unique = new ENode("Par","or");
                        unique.list.add(a);
                        unique.list.add(b);
                        mustUnique.list.add(unique);
                    }
                }
                basic.list.add(mustUnique);
            }
            // for every value, it must be put at 1 loc and be put at most 1 loc
            for(int i=0;i<y;i++){
                // must contain value;
                Node mustContain = new ENode("Par","or");
                for(int j=0;j<z;j++){
                    mustContain.list.add(new ENode(true,false,k,i,j));
                }
                basic.list.add(mustContain);

                // must contain at most 1 value
                Node mustUnique = new ENode("Par","and");
                for(int j1=0;j1<z;j1++){
                    for (int j2=j1+1;j2<z;j2++){
                        // mutually exclusive
                        Node a = new ENode(true,true,k,i,j1);
                        Node b = new ENode(true,true,k,i,j2);
                        Node unique = new ENode("Par","or");
                        unique.list.add(a);
                        unique.list.add(b);
                        mustUnique.list.add(unique);
                    }
                }
                basic.list.add(mustUnique);
            }
            res.list.add(basic);
        }
        return res;
    }

    public Node occupySpecificLoc(int k, int i, int j){
        return new ENode(true,false,k,i,j);
    }

    public Node onLeft(int k1, int i1, int k2, int i2){
        // && (a_j->(b_j+1)
        Node res = new ENode("Par","and");
        for(int j=0;j<z-1;j++){
            ENode aL = new ENode(true,true,k1,i1,j);
            ENode bL = new ENode(true,false,k2,i2,j+1);
            ENode left =new ENode("Par","or");
            left.list.add(aL);
            left.list.add(bL);
            res.list.add(left);
        }
        ENode aL = new ENode(true,true,k1,i1,z-1);
        res.list.add(aL);
        return res;
    }
    public Node nextEachOther(int k1, int i1, int k2, int i2){
        // && (a_j->(b_j+1 || b_j-1)
        Node res = new ENode("Par","and");
        for(int j=0;j<z;j++){
            Node aL = new ENode(true,true,k1,i1,j);
            Node b = new ENode("Par","or");
            for(int j1=j-1;j1<z && j1<=j+1;j1+=2){
                if(j1>=0 && j1<z){
                    Node bL = new ENode(true,false,k2,i2,j1);
                    b.list.add(bL);
                }
            }
            Node left =new ENode("Par","or");
            left.list.add(aL);
            left.list.add(b);
            res.list.add(left);
        }
        return res;
    }
    public Node occupySameLoc(int k1, int i1, int k2, int i2){
        // && (a_j->b_j)
        Node res = new ENode("Par","and");
        // i1->i2 is enough
        for(int j=0;j<z;j++){
            //
            Node aL = new ENode(true,true,k1,i1,j);
            Node bL = new ENode(true,false,k2,i2,j);
            Node left =new ENode("Par","or");
            left.list.add(aL);
            left.list.add(bL);
            res.list.add(left);
        }
        return res;
    }
    // find k,i
    public int[]  getType(String s){
        for(int k=0;k<x;k++){
            for (int i=0;i<y;i++){
                if(ss[k][i].equals(s)){
                    return  new int[]{k,i};
                }
            }
        }
        System.err.println("wrong input");
        System.out.println("wrong input");
        return null;
    }
    public Node construct(){
        Node res = new ENode("Par","and");

        Node basic = buildBasic();

        int[] t1, t2;
        // Brit lives in the red house.
        t1 = getType("Brit");
        t2 = getType( "red");
        Node h1 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The Swede keeps dogs as pets.
        t1 = getType("Swede");
        t2 = getType("dog");
        Node h2 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The Dane drinks tea.
        t1 = getType("Dane");
        t2 = getType("tea");
        Node h3 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The green house is on the left of the white house.
        t1 = getType("green");
        t2 = getType("white");
        Node h4 = onLeft(t1[0],t1[1],t2[0],t2[1]);

        //The green house’s owner drinks coffee.
        t1 = getType("green");
        t2 = getType("coffee");
        Node h5 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The person who smokes Pall Mall rears birds.
        t1 = getType("Pall Mall");
        t2 = getType("bird");
        Node h6 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The owner of the yellow house smokes Dunhill
        t1 = getType("yellow");
        t2 = getType("Dunhill");
        Node h7 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The man living in the center house drinks milk.
        t1 = getType("milk");
        Node h8 = occupySpecificLoc(t1[0],t1[1],2);

        //The Norwegian lives in the first house.
        t1 = getType("Norwegian");
        Node h9 = occupySpecificLoc(t1[0],t1[1],0);

        //The man who smokes Blends lives next to the one who keeps cats
        t1 = getType("Blend");
        t2 = getType("cat");
        Node h10 = nextEachOther(t1[0],t1[1],t2[0],t2[1]);

        //The man who keeps the horse lives next to the man who smokes Dunhill.
        t1 = getType("horse");
        t2 = getType("Dunhill");
        Node h11 = nextEachOther(t1[0],t1[1],t2[0],t2[1]);

        //The owner who smokes Bluemasters drinks beer.
        t1 = getType("Bluemaster");
        t2 = getType("beer");
        Node h12 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The German smokes Prince.
        t1 = getType("German");
        t2 = getType("Prince");
        Node h13 = occupySameLoc(t1[0],t1[1],t2[0],t2[1]);

        //The Norwegian lives next to the blue house.
        t1 = getType("Norwegian");
        t2 = getType("blue");
        Node h14 = nextEachOther(t1[0],t1[1],t2[0],t2[1]);

        //The man who smokes Blends has a neighbor who drinks water.
        t1 = getType("Blend");
        t2 = getType("water");
        Node h15 = nextEachOther(t1[0],t1[1],t2[0],t2[1]);

        res.list.add(basic);
        res.list.add(h1);
        res.list.add(h2);
        res.list.add(h3);
        res.list.add(h4);
        res.list.add(h5);
        res.list.add(h6);
        res.list.add(h7);
        res.list.add(h8);
        res.list.add(h9);
        res.list.add(h10);
        res.list.add(h11);
        res.list.add(h12);
        res.list.add(h13);
        res.list.add(h14);
        res.list.add(h15);

        return res;
    }

    public void print(){
        for(int k=0;k<x;k++){
            System.out.print("[");
            for(int i=0;i<y;i++){
                System.out.print(ss[k][i]+", ");
            }
            System.out.println("],");
        }
    }
    public void writeFile(String path, Node node){
        try {
            // Create a FileWriter object
            FileWriter writer = new FileWriter(path);
            writer.write("c Einstein's puzzle encoding\n");

            StringBuilder sb = new StringBuilder();
            sb.append("p cnf 125 "+node.list.size()+"\n");
            writer.write(sb.toString());

            for(int i=0;i<node.list.size();i++){
                Node or = node.list.get(i);
                for(int j=0;j<or.list.size();j++){
                    Node c = or.list.get(j);
                    writer.write((c.key+1)*(c.isNegative?-1:1)+"");
                    if(j!=or.list.size()-1){
                        writer.write(" ");
                    }
                }
                if(i!=node.list.size()-1){writer.write(" 0\n");}
            }

            writer.close();

            System.out.println("Text has been written to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printPuzzle(CNFEval cnfEval){
        String[][] res = new String[x][z];
        for(int k=0;k<x;k++){
            for(int i=0;i<y;i++){
                for(int j=0;j<z;j++){
                    if(cnfEval.values[ENode.getK(k,i,j)]){
                        System.out.println("P("+k+","+i+","+j+""+")");
                        res[k][j] = ss[k][i];
                    }
                }
            }
        }
        for(int k=0;k<x;k++){
            for (int j=0;j<z;j++){
                System.out.print(res[k][j]+"\t");
            }
            System.out.print("\n");
        }
    }

    public boolean evaluate(){
        Map<Integer, CNFEval.Info> map = new HashMap<>();
        Node node = construct();
        //flat
        node.flat();
        //init count
        CNFEval cnfEval = new CNFEval(x*y*z);
        //calculate
        boolean ans = cnfEval.eval(node,0);
        System.out.println(ans);
        printPuzzle(cnfEval);
        return ans;
    }
    public void constructCNFFormat(){
        Map<Integer, CNFEval.Info> map = new HashMap<>();
        Node node = construct();
        node.flat();
        writeFile("/Users/yufengsu/Downloads/lecture/Archive/CS 2110/assignment/hw1_code/LCS1/src/resource/input.txt",node);

    }
    public static void main(String[] args) {

       Main main = new Main();
       main.constructCNFFormat();
       Encoder encoder = new Encoder();
       Node root = encoder.readFile("/Users/yufengsu/Downloads/lecture/Archive/CS 2110/assignment/hw1_code/LCS1/src/resource/input.txt");
       CNFEval cnfEval = new CNFEval(encoder.n);
       boolean res = cnfEval.eval(root,0);
       for(int i=0;i< cnfEval.n;i++){
           System.out.print(cnfEval.values[i]+" ");
       }
        System.out.println();
       main.printPuzzle(cnfEval);
       encoder.writeFile("/Users/yufengsu/Downloads/lecture/Archive/CS 2110/assignment/hw1_code/LCS1/src/resource/output.txt",res, cnfEval.values);

    }
}