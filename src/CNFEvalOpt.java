import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CNFEvalOpt extends CNFEval{
    public CNFEvalOpt(int val) {
        super(val);
    }
    public Info findCandidate(Map<Integer, Info> map){
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                return map.get(i);
            }
        }
        for(Integer i:map.keySet()){
            if(map.get(i).pos==0 || map.get(i).neg==0){
                return map.get(i);
            }
        }
        // optimize;
        double max = 0;
        int id = -1;
        for(Integer i:map.keySet()){
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
            int key  = node.key;
            if(map.containsKey(key)==false) map.put(key,new Info(key));
            Info info = map.get(key);
            if(node.isNegative){
                info.neg++;
            }else{
                info.pos++;
            }
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
                    info.score+= 1.0/(node.list.size());
                }
            }
        }
    }
}
