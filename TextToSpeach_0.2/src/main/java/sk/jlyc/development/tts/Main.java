package sk.jlyc.development.tts;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sochaa on 4. 7. 2016.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(reverse("kobyla ma maly bok"));
    }

    public static String reverse(String toReverse){
        char[] toReverseArray = toReverse.toCharArray();
        char[] reversedArray = new char[toReverseArray.length];

        for(int i=toReverseArray.length-1, j=0; i>=0;--i, j++){
            reversedArray[i] = toReverseArray[j];
        }

        return new String(reversedArray);
    }

    public Node Left, Right;

    List<Node> visitedNode = new ArrayList<Node>();
    public boolean notCycled(Node parentNode)
    {
        visitedNode.add(parentNode);
        if(Left==null && Right==null){
            //is leaf
            return true;
        }

        if(Left==parentNode||Right==parentNode){
            //self cycled
            return false;
        }

        if((Left !=null && visitedNode.contains(Left))|| (Right !=null && visitedNode.contains(Right))){
            //cycled tree
            return false;
        }

        if(Left !=null && !(notCycled(Left))){
            return false;
        }
        if(Right !=null && !(notCycled(Right))){
            return false;
        }

        return true;
    }
}
