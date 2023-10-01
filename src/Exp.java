import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Exp {
    // k: should always be 3 in this case
    public static void generateFile(int k, int n, int l, String path) {
        int[][] cnfArray = generateCNF(k, n, l);
        try {
            // Create a FileWriter object
            FileWriter writer = new FileWriter(path);
            String comment = "c " + "k:" + k + " n:" + n + " l" + l;
            writer.write(comment + "\n");
            StringBuilder sb = new StringBuilder();
            sb.append("p cnf ");
            sb.append(n + " ");
            sb.append(l + "\n");
            writer.write(sb.toString());
            for (int i = 0; i < l; i++) {
                for (int j = 0; j < k; j++) {
                    if (j != k - 1) writer.write(cnfArray[i][j] + " ");
                }
                if (i != l - 1) writer.write(" 0\n");
            }
            // Close the FileWriter to release system resources
            writer.close();
            System.out.println("Text has been written to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Node generateNode(int k, int n, int l) {
        int[][] cnfArray = generateCNF(k, n, l);
        Node root = new Node(false, true);
        for (int i = 0; i < l; i++) {
            Node tmp = new Node(false, false);
            for (int j = 0; j < k; j++) {
                tmp.list.add(new Node(true, cnfArray[i][j] < 0 ? true : false, Math.abs(cnfArray[i][j]) - 1));
            }
            root.list.add(tmp);
        }
        return root;
    }

    public static int[][] generateCNF(int k, int n, int l) {
        Random random = new Random();
        Random negative = new Random();
        int[][] res = new int[l][k];
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < k; j++) {
                int key = random.nextInt(n) + 1;
                int sign = negative.nextInt(2) == 0 ? 1 : -1;
                res[i][j] = key * sign;
            }
        }
        return res;
    }

    enum Method {
        random, Cluase, Opt;
    }

    static class Index {
        int terminated;
        int success;

        int failure;
        List<Long> cntList = new ArrayList<>();
        List<Long> sCntList = new ArrayList<>();
        List<Long> fCntList = new ArrayList<>();
        List<Long> timeList = new ArrayList<>();
        long call;

        int cnt;
        double time;
        Method method;

        @Override
        public String toString() {
            return "Method:" + method.toString() + " Terminated:" + terminated + " Success:" + success + " Failure:"+ failure
                    +" AVG Time:" + time/cnt + " Call:"+ call/cnt +" MedianTime:"+(timeList.size()>0?getMedianTime():"0")+(" MedianCnt:"+(cntList.size()>0?getMedianCnt():0))+" successCnt:"+geSuccessMeanCnt() ;
        }

        public long getMedianCnt(){
            if(cntList.size()==0)return 1;
            return cntList.get(cntList.size()/2);
        }
        public long geSuccessMeanCnt(){
            if(sCntList.size()==0)return 1;
            return sCntList.get(sCntList.size()/2);
        }

        public long getMedianTime(){
            if(timeList.size()==0)return 1;
            return timeList.get(timeList.size()/2);
        }
        public Index(Method method){
            this.method = method;
        }
    }

    public static void doExp2() {
        int k = 3;
        int turnNum = 100;
        int[] nCandidates = new int[]{100,150};
        for (double rate = 3.0; rate <= 6.1; rate += 0.2) {
            for (int n : nCandidates) {
                Index[] indices = new Index[]{new Index(Method.random),new Index(Method.Cluase),new Index(Method.Opt)};
                //Index[] indices = new Index[]{new Index(Method.Cluase),new Index(Method.Opt)};
                int l = (int) Math.round(n * rate);
                for (int turn = 0; turn < turnNum; turn++) {
                    Node node = generateNode(k, n, l);
                    CNFEval[] cnfEvals = new CNFEval[]{new CNFEvalRandom(n), new CNFEval2Clause(n), new CNFEvalOpt(n)};
                    //CNFEval[] cnfEvals = new CNFEval[]{new CNFEval2Clause(n), new CNFEvalOpt(n)};
                    for (int indexCNF = 0; indexCNF < cnfEvals.length; indexCNF++) {
                        //System.out.println(turn +" " + indexCNF);
                        long startTime = System.currentTimeMillis();
                        boolean ans = cnfEvals[indexCNF].eval(node, 0);
                        indices[indexCNF].cnt++;
                        indices[indexCNF].call+=cnfEvals[indexCNF].evalCnt;
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        indices[indexCNF].time += elapsedTime;
                        if(cnfEvals[indexCNF].terminated==true){
                            indices[indexCNF].terminated++;
                        }else {
                            if(ans){
                                indices[indexCNF].success++;
                                indices[indexCNF].sCntList.add(cnfEvals[indexCNF].evalCnt);
                            }
                            else{
                                indices[indexCNF].failure++;
                            }
                            indices[indexCNF].timeList.add(elapsedTime);
                            indices[indexCNF].cntList.add(cnfEvals[indexCNF].evalCnt);
                        }
                        indices[indexCNF].timeList.add(elapsedTime);
                        indices[indexCNF].cntList.add(cnfEvals[indexCNF].evalCnt);

                        //System.out.println("Elapsed time in milliseconds: " + elapsedTime);
                    }
                }
                System.out.println("Under L/N = rate:"+String.format("%.1f", rate) + " ,N:"+n);
                for (int i = 0; i < indices.length; i++) {
                    System.out.println(" "+indices[i].toString());
                }
                System.out.println("Probability of satisfiability:"+indices[2].success*1.0/indices[2].cnt);
                System.out.println("Heuristic/Random:"+indices[2].time/indices[0].time+" | "+indices[2].getMedianCnt()*1.0/indices[0].getMedianCnt());
                System.out.println("Heuristic/2Clause:"+indices[2].time/indices[1].time+" | "+indices[2].getMedianCnt()*1.0/indices[1].getMedianCnt());
//                System.out.println("Probability of satisfiability:"+indices[1].success*1.0/indices[1].cnt);
//                System.out.println("Heuristic/2Clause:"+indices[1].time/indices[0].time+" | "+indices[1].getMedianTime()*1.0/indices[0].getMedianTime());
                System.out.println();
            }

        }
    }
    public static void main(String[] args) {
        Exp.doExp2();
    }
}
