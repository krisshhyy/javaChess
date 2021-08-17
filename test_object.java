import java.io.*;
public class test_object implements Serializable {

    int a ;
    String s;
    test_object(int a,  String s){
        this.a = a;
        this.s = s;
    }

    public int getInt(){
        return this.a;
    }

    public String getString(){
        return this.s;
    }

}
