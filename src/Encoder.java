import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Encoder {
    int n,m=0;
    List<String> comments = new ArrayList<>();
    public  Node readFile(String path){

        Node root = new ENode("Par","and");
        try {
            // Create a FileReader object to read the file
            FileReader fileReader = new FileReader(path);

            // Wrap FileReader in a BufferedReader for efficient reading
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read the file line by line
            String line;

            Node node = new ENode("Par","or");

            while ((line = bufferedReader.readLine()) != null) {
                if(line.startsWith("c")){
                    comments.add(line);
                }else if(line.startsWith("p")){
                    String[] ss = line.split(" ");
                    n = Integer.parseInt(ss[2]);
                    m = Integer.parseInt(ss[3]);
                }else {
                    String[] ss = line.split(" ");
                    for(int i=0;i<ss.length;i++){
                        int v = Integer.parseInt(ss[i]);
                        if(v==0){
                            root.list.add(node);
                            node = new ENode("Par","or");
                        }else{
                            if(v<0){
                                v= -v-1;
                                node.list.add(new Node(true,true,v));
                            }else {
                                v-=1;
                                node.list.add(new Node(true,false,v));
                            }
                        }
                    }
                }
            }
            root.list.add(node);
            // Close the BufferedReader and FileReader when done
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    public void writeFile(String path, boolean res, boolean[] values){
        try {
            // Create a FileWriter object
            FileWriter writer = new FileWriter(path);

            for(String c:comments){
                writer.write(c+"\n");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("s cnf ");
            int success = res?1:0;
            sb.append(success+" ");
            sb.append(n+" ");
            sb.append(m+"\n");
            writer.write(sb.toString());
            for(int i=0;i<n;i++){
                writer.write("v "+values[i]+"\n");
            }
            // Close the FileWriter to release system resources
            writer.close();

            System.out.println("Text has been written to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
