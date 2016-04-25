import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by JLyc on 4/23/2016.
 */
public class TreeCollection<T> implements Iterable{
    private T root;
    private ArrayList<TreeCollection<T>> childes = new ArrayList<>();
    private Iterator<TreeCollection<T>> iterator = childes.iterator();
    private boolean value;

    public TreeCollection(T value) {
        this.root = value;
    }

    public void addChield(TreeCollection<T> value){
        childes.add(value);
    }

    public TreeCollection<T> nextChild(){
        return iterator.next();
    }

    public boolean hasChileds(){
        if(childes.size()>0){
            return true;
        }
        return false;
    }

    public int getChildCount(){
        return childes.size();
    }

    public TreeCollection<T> getChild(T child) {
        if(childes.contains(child));
        {
            return childes.get(childes.indexOf(child));
        }
    }

    public static void main(String[] args) {
        TreeCollection<String> newTree = new TreeCollection<>("root");
        System.out.println(newTree.hasChileds());
        newTree.addChield(new TreeCollection<>("child 1"));
        System.out.println(newTree.hasChileds());
        newTree.addChield(new TreeCollection<>("child 2"));
//        newTree.getChild("child 1").addChield("subchild");
        newTree.nextChild();
        TreeCollection<String> child = newTree.nextChild();
        child.addChield(new TreeCollection<>("subchild 2"));
        traverseTree(newTree);
    }

    public static void traverseTree(TreeCollection<String> tree) {
        TreeCollection<String> iter = (TreeCollection<String>) tree.iterator();
        while (iter.hasChileds()) {
            TreeCollection<String> child = iter.nextChild();
            if (child.hasChileds()) {
                traverseTree(child);
            } else {
                System.out.println(child.getValue());
            }
        }

    }

    @Override
    public Iterator iterator() {
        return iterator();
    }

    public T getValue() {
        return root;
    }
}
