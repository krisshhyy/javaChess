import java.util.*;

class test {
  public static  Collection<String> al = new ArrayList<>();
  public static  Collection<String> bl = new ArrayList<>();


public static void main(String args[]) {
    al.add("FUCK");
    bl.add("YOU");
    Collection<String> lists = new ArrayList<>();
    for(String l : al){
        lists.add(l);
    }
    for(String n : bl){
        lists.add(n);
    }
    Iterable<String> result = lists;
    System.out.println(result);

}
}