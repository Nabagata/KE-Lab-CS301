/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author c137165
 */

class Node{
    int leaf;
    HashMap<String,Node> children;
    String class_name;
    
    public Node(String class_name, int leaf){
        this.class_name=class_name;
        this.leaf=leaf;
        if(leaf == 0)
            this.children=new HashMap<String,Node>();
    }
    public float get_value(float count,float no_of_rows){
        if(count == 0){
            return 0;
        }
        float result= (float) -((count/no_of_rows)*(Math.log((count/no_of_rows))/Math.log(2)));
        return result;
    }
    public int gain(ArrayList<ArrayList<String>> hs,ArrayList<ArrayList<String>> dattr,int endindex,float no_of_rows,int no_of_attributes,ArrayList<Integer> attrlist){
        System.out.println("===========Gain starts===============");
        float count_yes=0;
        float count_no=0;
        for(int i=0;i<no_of_rows;i++){
//            System.out.println(hs.get(i).toString());
//            System.out.println(hs.get(i).get(endindex).compareTo("yes"));
            if(hs.get(i).get(endindex).compareTo("yes") == 1){
                count_yes++;
            }
            else{
                count_no++;
            }
        }
//       System.out.println(count_yes);
//       System.out.println(count_no);
       float gain=(float) (((count_yes/no_of_rows)*(this.get_value(count_yes,no_of_rows)))+((count_no/no_of_rows)*this.get_value(count_no, no_of_rows)));
       float max=-9999999;
       System.out.println("Gain: "+ gain);
       int min_index=0;
       for(int i=1;i<no_of_attributes-1;i++){
           if(attrlist.contains(i)){
           float attr_gain=0;
           System.out.println(dattr.get(i));
           
           for(int j=0;j<dattr.get(i).size();j++){
               float total_count=0;
               count_yes=0;
               count_no=0;
               for(int k=0;k<no_of_rows;k++){
//                  System.out.println(hs.get(k).get(i));
                   if(hs.get(k).get(i).toString() == null ? dattr.get(i).get(j).toString() == null : hs.get(k).get(i).toString().equals(dattr.get(i).get(j).toString()))
                   {
//                       System.out.println(dattr.get(i).get(j).toString()+": Got");
                       total_count++;
                       if(hs.get(k).get(endindex).compareTo("yes")== 1){
                           count_yes++;
                       }
                       else{
                           count_no++;
                       }
                   }
               }
               System.out.println("Total count: "+ total_count);
               System.out.println("Yes count: "+ count_yes);
               System.out.println("No count: "+ count_no);
               attr_gain=(float) (attr_gain+((total_count/no_of_rows))*(this.get_value(count_no, total_count)+this.get_value(count_yes, total_count)));
           }
           System.out.println("Attribute gain of "+i+" is "+attr_gain + "Max is : "+ max);
           if(gain-attr_gain > max){
               max=gain-attr_gain;
               min_index=i;
           }
       }
       }
       System.out.println("===========Gain ends===============");
       return min_index;
    }
    
    
    
    public void form_tree(ArrayList<ArrayList<String>> hs,ArrayList<ArrayList<String>> dattr,int endindex,float no_of_rows,int no_of_attributes,ArrayList<Integer> attrlist,int level,Node n){
        if(attrlist.isEmpty() == true)
        {
            System.out.println("Empty attr list");
            n.class_name="yes";
            n.leaf=1;
            return;
            
        }
        for(int i=0;i<level;i++)
            System.out.print("\t");
        System.out.println("In level"+ level);
        System.out.println(attrlist.toString());
        int split_index=n.gain(hs, dattr, endindex, no_of_rows, no_of_attributes,attrlist);
        attrlist.remove(new Integer(split_index));
        int count_yes=0;
        int count_no=0;
        System.out.println("Data set: "+ hs.toString());
        System.out.println("Split index: "+split_index);
        System.out.println("No of rows: "+no_of_rows);
        for(int i=0;i<dattr.get(split_index).size();i++){
            ArrayList<ArrayList<String>> dh=new ArrayList<ArrayList<String>>();
            for(int j=0;j<no_of_rows;j++){
//                System.out.println(hs.get(j).get(split_index));
                if(hs.get(j).get(split_index).toString().equals(dattr.get(split_index).get(i))){
                    dh.add(hs.get(j));
//                    System.out.println("Got one");
                    if(hs.get(j).get(endindex).compareTo("yes") == 1)
                        count_yes++;
                    else{
                        count_no++;
                    }
                }
            }   
                System.out.println("Count of yes: "+count_yes);
                System.out.println("Count of no : "+count_no);
                if(count_yes == 0)
                {
                    System.out.println("Leaf node and is no");
                    n.children.put(dattr.get(split_index).get(i), new Node("no",1));
                }
                else if(count_no == 0){
                    System.out.println("Leaf node yes");
                    n.children.put(dattr.get(split_index).get(i), new Node("yes",1));
                }
                else{
                    System.out.println("Not a leaf not finding children for "+dattr.get(split_index).get(i));
                    Node n2=new Node(String.valueOf(split_index),0);
                    n.children.put(dattr.get(split_index).get(i),n2);
                    form_tree(dh,dattr,endindex,dh.size(),no_of_attributes,attrlist,level+1,n2);
                    System.out.println("Back to level"+ level);
                    count_yes=0;
                    count_no=0;
                }
            
        }
        
        
        
    }
    
    public void view_tree(int level,Node n){
        for(int i=0;i<level;i++)
            System.out.print("\t");
        System.out.print(n.class_name+" : "+n.leaf);
        if(this.leaf == 0){
            if(n.children != null){
            for(int i=0;i<n.children.size();i++){
                System.out.println();
                Iterator it = n.children.entrySet().iterator();
                while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey());
                if(pair.getValue()!=null)
                    view_tree(level+1, (Node) pair.getValue());
                System.out.println();
                it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }
        }
    }
    
}
public class decisiontree {
     static ArrayList<ArrayList<String>> hs;
     static ArrayList<ArrayList<String>> dattr;
     public static void main(String[] args) throws IOException{
         int no_of_attributes=0;
         int no_of_records=0;
         FileReader fw=new FileReader("dtdata.txt");
         char arr[]=new char[1000000];
         fw.read(arr);
         String st=new String(arr);
         StringTokenizer s=new StringTokenizer(st);
         int count=0;
         int flag=0;
         hs=new ArrayList<ArrayList<String>>();
         dattr=new ArrayList<ArrayList<String>>();
         ArrayList<String> single=new ArrayList<String>();
         while(s.hasMoreTokens()){
             ArrayList<String> temp2=new ArrayList<String>();
             String temp=s.nextToken("\n");
//             System.out.println(temp);
            if(temp.contains("-1"))
                break;
             StringTokenizer s2=new StringTokenizer(temp);
             while(s2.hasMoreTokens()){
                 String temp3=s2.nextToken(" ");
                     single.add(temp3);
                 try{
                     dattr.get(count);
                 }
                 catch(java.lang.IndexOutOfBoundsException e){
                     dattr.add(new ArrayList<String>());
                 }
                 if(!dattr.get(count).contains(temp3))
                     dattr.get(count).add(temp3);
//                 System.out.println(temp3);
                 temp2.add(temp3);
                 count=count+1;
             
            }
             if(flag ==0)
                no_of_attributes=count;
             flag=1;
             if(flag==1)
                 count=0;
             
             hs.add(temp2);
             no_of_records++;
         }
         
         System.out.println(hs.toString());
         System.out.println(dattr.toString());
         System.out.println("No of attributes: "+ no_of_attributes + ": No of records: "+no_of_records);
         ArrayList<Integer> attrlist=new ArrayList<Integer>();
         for(int i=1;i<5;i++)
             attrlist.add(i);
         //Found each attribute
         System.out.println(attrlist.toString());
         Node n=new Node("start",0);
//         System.out.println(n.gain(hs, dattr, no_of_attributes-1, no_of_records, no_of_attributes,attrlist));
        n.form_tree(hs, dattr, no_of_attributes-1, no_of_records, no_of_attributes, attrlist, 0, n);
        System.out.println("============View tree=============");
        n.view_tree(0, n);
        System.out.println("============Done==================");
         
     }
}
