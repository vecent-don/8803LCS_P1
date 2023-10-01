
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CNFEval {
    class Info implements Comparable{
        int pos;
        int neg;

        int clause;
        int key;

        int minOccur = Integer.MAX_VALUE;

        double score = 0;

        public Info(int key){
            this.key = key;
        }
        public int rule1(Info b){
            if(minOccur==1 && b.minOccur==1){
                return 0;
            }else if(minOccur==1){
                return -1;
            }else if(b.minOccur==1){
                return 1;
            }else {
                return 0;
            }
        }
        public int rule2(Info b){
            if((pos==0 || neg==0) && (b.pos==0 || b.neg==0)){
                return 0;
            }else if(pos==0 || neg==0){
                return -1;
            }else if(b.pos==0 || b.neg==0){
                return 1;
            }else {
                return 0;
            }
        }

        @Override
        public int compareTo(Object o) {
            Info b  =(Info) o;
            if( rule1(b)!=0){
                return rule1(b);
            }
            if( rule2(b)!=0){
                return rule2(b);
            }
            return minOccur-b.minOccur;
        }

        @Override
        public String toString(){
            int j = key%5;
            int i = (key - j)/5%5;
            int k = key/25;
            return "P("+ k +","+ i +","+ j;
        }
    }

    int n;

    boolean[] values;
    List<Node> path  = new ArrayList<>();

    public CNFEval(int val){
        n = val;
        values = new boolean[n];
    }



    public Info findCandidate(Map<Integer, Info> map){
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                return map.get(i);
            }
        }
        //
        for(Integer i:map.keySet()){
            if(map.get(i).pos==0 || map.get(i).neg==0){
                return map.get(i);
            }
        }
        // optimize;
        for(Integer i:map.keySet()){
            return map.get(i);
        }
        return null;
    }

    public void countForLeaf(Node node, Map<Integer, Info> map){
        int key  = node.key;
        if(map.containsKey(key)==false) map.put(key,new Info(key));
        Info info = map.get(key);
        if(node.isNegative){
            info.neg++;
        }else{
            info.pos++;
        }
    }

    public void count(Node node, Map<Integer, Info> map){
        if(node.isLeaf){
            countForLeaf(node,map);
        }else{
            for(Node c: node.list){
                count(c,map);
                if(!node.isAnd){
                    int key  = c.key;
                    if(map.containsKey(key)==false){
                        System.err.println("impossible structure");
                        map.put(key,new Info(key));
                    }
                    Info info = map.get(key);
                    info.minOccur = Math.min(info.minOccur, node.list.size());
                }
            }
        }
    }

    public boolean assignValue(Node  root, Info info,int depth){
        Node node;
        boolean res=false;
        boolean val = (info.pos>info.neg)? true:false;
        values[info.key] = val;
        node = reduceCNF(info.key, root,val);
        if(node.value ==true && eval(node,depth+1)){
            res = true;
        }else {
            values[info.key] =!val;
            node = reduceCNF(info.key, root,!val);
            if(node.value==false){
                res = false;
            }else{
                res =  eval(node,depth+1);
            }
        }
        return res;

    }

    long evalCnt = 0;
    boolean terminated = false;
    public boolean eval(Node root,int depth){
        // track info
        evalCnt++;
        if(evalCnt>=Integer.MAX_VALUE/(4096*4)){
            terminated = true;
            //System.out.println("have to terminated");
            return false;
        }
        path.add(root);

        // count and then find proposition to be eliminated  in this recur
        Map<Integer,Info> map = new HashMap<>();
        count(root,map);
        if(map.size()==0){
            //System.out.println(depth);
            return true;
        }
        Info info =  findCandidate(map);
        if(info==null){
            System.err.println("impossible cases of info");
            return true;
        }

        boolean res = assignValue(root,info,depth);
        if(res==false)path.remove(path.size()-1);
        return res;

    }

    // only handle perfect 3-layer form
    public Node reduceCNF(int key, Node root, boolean val){
        Node node = root.copy();
        List<Node> andArr = new ArrayList<>();
        // update value, update list
        for(Node or: node.list){
            List<Node> arr = new ArrayList<>();
            for(Node c: or.list){
                if(key==c.key){
                    boolean value = c.isNegative? (!val):val;
                    or.value = or.value || value;
                }else{
                    arr.add(c);
                }
            }

            if(or.value==true){
                //remove
            }else if(arr.size()>0){
                or.list = arr;
                andArr.add(or);
            }else{
                node.value = node.value && or.value;
            }
        }
        node.list = andArr;
        return node;
    }

}
