import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CNFEvalRandom extends CNFEval{
    public CNFEvalRandom(int val) {
        super(val);
    }


    public Info findCandidate(Map<Integer, Info> map){
        List<Integer> candidate = new ArrayList<>();
        for(Integer i:map.keySet()){
            if(map.get(i).minOccur==1){
                candidate.add(i);
            }
        }
        //random pick
        for(Integer i:candidate){
            return map.get(i);
        }
        //random pick
        for(int i:map.keySet()){
            return map.get(i);
        }
        return null;
    }


//    public boolean eval(Node root,int depth){
//        path.add(root);
//        Map<Integer,Info> map = new HashMap<>();
//        count(root,map);
//        if(map.size()==0){
//            System.out.println(depth);
//            return true;
//        }
//        Info info =  findCandidate(map);
//        if(info==null){
//            System.out.println(depth);
//            return true;
//        }
//
//        Node node;
//        boolean res=false;
//        values[info.key] = true;
//        node = reduceCNF(info.key, root,true);
//        if(node.value ==true && eval(node,depth+1)){
//            res = true;
//        }else {
//            values[info.key] = false;
//            node = reduceCNF(info.key, root,false);
//            if(node.value==false){
//                res = false;
//            }else{
//                res =  eval(node,depth+1);
//            }
//        }
//        if(res==false)path.remove(path.size()-1);
//        return res;
//    }
}
