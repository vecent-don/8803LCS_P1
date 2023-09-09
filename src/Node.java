import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {
    int key;
    boolean value;
    boolean isLeaf;
    boolean isNegative;
    boolean isAnd;

    public Node(){

    }
    public Node(int key, boolean value1, boolean isLeaf1, boolean isNegative1, boolean isAnd1){
        this.key = key;
        this.value = value1;
        this.isLeaf = isLeaf1;
        this.isNegative = isNegative1;
        this.isAnd = isAnd1;
    }
    public Node(boolean leaf, boolean and){
        isLeaf = leaf;
        isAnd = and;
        if(isAnd){
            value = true;
        }else{
            value = false;
        }
    }

    public Node(boolean leaf, boolean negative,int key){
        isLeaf = leaf;
        isNegative = negative;
        this.key = key;
    }

    List<Node> list = new ArrayList<>();

    public Node copy(){
        if(isLeaf)return new Node(key,value,isLeaf,isNegative,isAnd);
        Node tmp = new Node(key,value,isLeaf,isNegative,isAnd);
        for(Node c: list){
            tmp.list.add(c.copy());
        }
        return tmp;
    }

    // expand the node as typical CNF form;
    public void flat(){
        if(!isLeaf){
            List<Node> arr = new ArrayList<>();
            for(Node c: list){
                c.flat();
                // Par is and
                if(isAnd){
                    if(c.isLeaf){
                        ENode tmpOr = new ENode("Par","or");
                        tmpOr.list.add(c);
                        arr.add(tmpOr);
                    }
                    else if(c.isAnd){
                        // add And-Child' children
                        for(Node cc:c.list){
                            arr.add(cc);
                        }
                    }else{
                        // add Or-Child directly
                        arr.add(c);
                    }
                }else{
                    // par is OR
                    if(c.isLeaf){
                        arr.add(c);
                    }else if(c.isAnd){
                        System.err.println("??? wrong nested");
                    }else{
                        // add Or-Child' children
                        for(Node cc:c.list){
                            arr.add(cc);
                        }
                    }
                }

            }
            list.clear();
            list = arr;
        }
    }


    @Override
    public String toString(){
        if(isLeaf){
            return (isNegative?"~":"")+"P("+key+")";
        }
        else {
            String conjunct = isAnd? " && ":" || ";
            String s = "";
            for (int ii=0;ii<list.size();ii++){
                if(list.get(ii).isLeaf==false && list.get(ii).isAnd==false && isAnd==true) {
                    s += "(" + list.get(ii) + ")\n";
                }else {
                    s += list.get(ii);
                }
                if(ii!=list.size()-1)s+= conjunct;
            }

            return s;
        }
    }
}
