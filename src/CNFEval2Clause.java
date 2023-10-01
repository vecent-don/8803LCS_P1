import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CNFEval2Clause extends CNFEval{
    public CNFEval2Clause(int val) {
        super(val);
    }

    public boolean assignValue(Node  root, Info info,int depth){
        Node node;
        boolean res=false;
        boolean val = new Random().nextBoolean();
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
    public Info findCandidate(Map<Integer, Info> map){
        List<Integer> candidate = new ArrayList<>();
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                candidate.add(i);
            }
        }
        // choose based on 2-clause value
        if(candidate.size()==0){
            candidate = new ArrayList<>(map.keySet());
        }
        if(candidate.size()>0){
            int id = candidate.get(0);
            for(int i: candidate){
                if(map.get(i).clause>map.get(id).clause){
                    id = i;
                }
            }
            return map.get(id);
        }
        return null;
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
                    if(node.list.size()==2){
                        info.clause++;
                    }
                }
            }
        }
    }



}
