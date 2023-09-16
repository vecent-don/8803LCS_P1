import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CNFEval2Clause extends CNFEval{
    public CNFEval2Clause(int val) {
        super(val);
    }

    public Info findCandidate(Map<Integer, Info> map){
        List<Integer> candidate = new ArrayList<>();
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                candidate.add(i);
            }
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
        for(int i:map.keySet()){
            return map.get(i);
        }
        return null;
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
                    if(node.list.size()==2){
                        info.clause++;
                    }
                }
            }
        }
    }

}
