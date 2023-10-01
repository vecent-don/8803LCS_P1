import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CNFEvalOpt extends CNFEval{
    public CNFEvalOpt(int val) {
        super(val);
    }

    @Override
    public boolean assignValue(Node  root, Info info,int depth){
        Node node;
        boolean res=false;
        if(info.pos==0){
            // only try "false"
            values[info.key] = false;
            node = reduceCNF(info.key, root,false);
            if(node.value==false){
                res =  false;
            }else {
                res =  eval(node,depth+1);
            }
        }else if(info.neg==0){
            // only try "true"
            values[info.key] = true;
            node = reduceCNF(info.key, root,true);
            if(node.value==false){
                res =  false;
            }else {
                res = eval(node,depth+1);
            }
        }else{
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
        }
        return res;

    }

    public Info findCandidate(Map<Integer, Info> map){
        // choose based on DPLL 1
        List<Integer> candidate = new ArrayList<>();
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                candidate.add(i);
            }
        }
        // choose based on polarity
        if(candidate.size()>0){
            for(Integer i:candidate){
                if(map.get(i).pos!=0 && map.get(i).neg!=0){
                    return map.get(i);
                }
            }
        }
        if(candidate.size()==0){
            candidate = new ArrayList<>(map.keySet());
        }
        // optimize;
        double max = 0;
        int id = -1;
        for(Integer i:candidate){
            if(map.get(i).score>max){
                max = map.get(i).score;
                id = i;
            }
        }
        if(id==-1)return null;
        return map.get(id);
    }


    public void count(Node node, Map<Integer, Info> map){
        if(node.isLeaf){
            countForLeaf(node,map);
        }else{
            double denominator = Math.pow(node.list.size(),2);
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
                    info.score+= 1.0/(denominator);
                }
            }
        }
    }
}
