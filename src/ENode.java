import java.util.ArrayList;
import java.util.List;

// This class is specific for the puzzle problem
public class ENode extends Node{

    // mode = true, use key mode
    static boolean mode;

    int k,i,j;
    static int x=5,y=5,z=5;

    static int getK(int k,int i,int j){
        return k*y*z + i*z + j;
    }

    public ENode(int k1, int i1, int j1, boolean value1, boolean isLeaf1, boolean isNegative1, boolean isAnd1){
        super(getK(k1,i1,j1),value1,isLeaf1,isNegative1,isAnd1);
        this.k = k1;
        this.i = i1;
        this.j = j1;
    }
    public ENode(boolean leaf, boolean negative, int k, int i, int j){
        super(leaf,negative,getK(k,i,j));
        this.k=k;
        this.i=i;
        this.j=j;
    }


    public ENode(String leaf, String logic){
        super(false,logic.toLowerCase().equals("or")?false:true);
    }

    public Node copy(){
        if(isLeaf)return new ENode(k,i,j,value,isLeaf,isNegative,isAnd);
        return super.copy();
    }


    @Override
    public String toString(){
        if(isLeaf){
            return (isNegative?"~":"")+"P("+k+","+i+","+j+""+")";
        }
        else {
            return super.toString();
        }
    }

}
