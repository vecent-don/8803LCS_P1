import java.util.*;

public class CNFEvalRandom extends CNFEval{
    public CNFEvalRandom(int val) {
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

    @Override
    public Info findCandidate(Map<Integer, Info> map){
        List<Integer> candidate = new ArrayList<>();
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                candidate.add(i);
            }
        }
        Random random = new Random();
        // random pick when tying
        if(candidate.size()>0){
            int index = random.nextInt(candidate.size());
            return map.get(candidate.get(index));
        }
        //random pick
        if(map.size()>0){
            int index = random.nextInt(map.size());
            return map.get(new ArrayList<>(map.keySet()).get(index));
        }
        return null;
    }



}
