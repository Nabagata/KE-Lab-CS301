
package dic;

import java.util.*;
import java.io.*;

public class DIC
{    
    public static void main(String[] args)
    {
        DIC nd = new DIC();
        nd.init();
    }
    //for potential applet implementation
    public void init()
    {
        DICCalculation dp = new DICCalculation();
        dp.dicProcess();
    }
}


class DICCalculation
{
    int numItems; 
    int numTransactions;
    int M;
    int numRead;
    int dbRun=0; 
    double minSup;
    String transaFile="transa.txt"; 
    String configFile="config.txt";
    String outputFile="dic-output.txt";
    Vector<Candidate> candidates = new Vector<Candidate>(); 
    String oneVal[]; 
    String itemSep = " ";

    class Candidate
    {
        int count;
        int startNum;
        int dbRun; 
        int itemsetNum; 
        String itemset; 
        boolean circle; 
        boolean solid; 
        public Candidate(int count, String itemset, int dbRun, int startNum, int itemsetNum)
        {
            this.count = count;
            this.itemset = itemset;
            this.circle = true;
            this.solid = false;
            this.startNum = startNum;
            this.dbRun = dbRun;
            this.itemsetNum = itemsetNum;
        }
    }
    public void dicProcess()
    {
        FileInputStream file_in;
        BufferedReader data_in;
        Date d;
        Candidate cand; 
        int latticeLayer=0;
        long start, end; 

        getConfig();

        System.out.println("DIC algorithm has started.\n");
        d = new Date();
        start = d.getTime();

       
        for(int i=1; i<=numItems; i++)
        {
            cand = new Candidate(0, Integer.toString(i), 0, numTransactions, 1);
            candidates.add(cand);
        }

        do
        {
            dbRun++; 
            try
            {
                numRead=0;
                file_in = new FileInputStream(transaFile);
                data_in = new BufferedReader(new InputStreamReader(file_in));

                
                while(numRead<numTransactions)
                {
                    loadTransactions(data_in);
//                    
                    updateCandidates();
                }
//               
                latticeLayer++;
                display(latticeLayer);
                
                removeItemsets(latticeLayer);
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }while(dashFound()); //keep looping until there are no more dashed candidates

        //display any leftover lattice Layers
        while(candidates.size()>0 && latticeLayer<numItems)
        {
            latticeLayer++;

            display(latticeLayer);
            removeItemsets(latticeLayer);
        }
        

    }
    

    public static String getInput()
    {
        String input="";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            input = reader.readLine();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return input;
    }

    
    private void getConfig()
    {
        FileWriter fw;
        BufferedWriter file_out;

       
            
        try
        {
             FileInputStream file_in = new FileInputStream(configFile);
             BufferedReader data_in = new BufferedReader(new InputStreamReader(file_in));
             //number of items
             numItems=Integer.valueOf(data_in.readLine()).intValue();

             //number of transactions
             numTransactions=Integer.valueOf(data_in.readLine()).intValue();

             //minsup
             minSup=(Double.valueOf(data_in.readLine()).doubleValue());

             //get the M value
             M=(Integer.valueOf(data_in.readLine()).intValue());

             //output config info to the user
             System.out.print("\nInput configuration: "+numItems+" items, "+numTransactions+" transactions, ");
             System.out.println("minsup = "+minSup+"%");
             System.out.println();
             minSup/=100.0;

            oneVal = new String[numItems];
                for(int i=0; i<oneVal.length; i++)
                    oneVal[i]="1";

            //create the output file
            fw= new FileWriter(outputFile);
            file_out = new BufferedWriter(fw);
            //put the number of transactions into the output file
            file_out.write(numTransactions + "\n");
            file_out.write(numItems + "\n******\n");
            file_out.close();
        }
        //if there is an error, print the message
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

 
    private boolean dashFound()
    {
        for(int c=0; c<candidates.size(); c++)
            if(!candidates.get(c).solid)
                return true; 
        return false;
    }

    private void loadTransactions(BufferedReader data_in)
    {
        StringTokenizer st; 
        boolean match; 
        boolean trans[] = new boolean[numItems]; 
        String input="f"; 
        
        try
        {
            //load transactions and compare to existing candidates of the n-itemsets
            for(int t=0; t<M; t++)
            {
                //System.out.println("Got here " + numRead + " times");
                input = data_in.readLine(); //get a transaction from the file
                if(input==null)
                    break;

                //System.err.println(input);
                //increase the number of transactions read
                numRead++;
                //create the new tokenizer
                st = new StringTokenizer(input, itemSep);
                
                //populate array for this transaction
                for(int i=0; i<numItems; i++)
                    trans[i]=(st.nextToken().compareToIgnoreCase(oneVal[i])==0);
                
                //for each Candidate
                for(int c=0; c<candidates.size(); c++)
                {
                    match = false;
                    //if the Candidate still needs to be confirmed
                    if(!candidates.get(c).solid)
                    {
                        st = new StringTokenizer(candidates.get(c).itemset); //tokenize the Candidate to get the items out
                        //check to see if each item is present in transaction
                        while(st.hasMoreTokens())
                        {
                            match=(trans[Integer.valueOf(st.nextToken())-1]);
                            if(!match)
                                break;
                        }
                        //if all of the items were in the transaction, increment count
                        if(match)
                            candidates.get(c).count++;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    
    private void updateCandidates()
    {
        
        for(int c=0; c<candidates.size(); c++)
        {
            
            if(!candidates.get(c).solid && candidates.get(c).startNum==numRead && candidates.get(c).dbRun==dbRun-1)
            {
                candidates.get(c).solid=true;
            }
            
            if(candidates.get(c).circle && candidates.get(c).count/(double)numTransactions >= minSup)
            {
                candidates.get(c).circle=false;
                makeNewCandidates(candidates.get(c));
            }
        }
    }

    
    private boolean isDuplicateCandidate(String cand)
    {
        //check each Candidate to see if cand is the same. If it is, return true.
        for(int c=0; c<candidates.size(); c++)
            if(candidates.get(c).itemset.compareToIgnoreCase(cand)==0)
                return true;
        //if nothing matches, return false
        return false;
    }

 
    private void makeNewCandidates(Candidate cand)
    {
        String newCandidateString; //the string representation of the new Candidate
        Candidate tempCand, secondCand; //temporary Candidate and Candidate being used with cand to create new Candidate
        StringTokenizer st1, st2; //string tokenizers
        boolean itemsInSet[]; //items in a set, used to sort Candidate orders and ensure there are not duplicates

        for(int c=0; c<candidates.size(); c++)
        {
            newCandidateString = new String(); //empty the Candidate string
            secondCand = candidates.get(c); //assign the Candidate being combined with cand
            //if secondCand is in the same lattice level as cand, and secondCand is a square
            if(secondCand.itemsetNum == cand.itemsetNum && !secondCand.circle)
            {
               // System.out.println("Cand1 itemsetNum: " + cand.itemsetNum + " secondCand itemsetNum: " + secondCand.itemsetNum);
                //set up tokenizers
                st1 = new StringTokenizer(cand.itemset);
                st2 = new StringTokenizer(secondCand.itemset);

                //sort the two items in the two candidates so that the itemsets come out in numerical order
                //ie: cand:'1 3', secondCand:'1 2' becomes '1 2 3' not '1 3 2'
                itemsInSet = new boolean[numItems];
                while(st1.hasMoreTokens())
                {
                    itemsInSet[Integer.valueOf(st1.nextToken())-1]=true;
                    itemsInSet[Integer.valueOf(st2.nextToken())-1]=true;
                }
                //create the Candidate string and trim it
                for(int i=0; i<numItems; i++)
                    if(itemsInSet[i])
                        newCandidateString += Integer.valueOf(i+1).toString() + " ";
                
                newCandidateString = newCandidateString.trim();

                //recreate the first tokenizers so that st1.countTokens() returns the correct result
                st1 = new StringTokenizer(cand.itemset);
                //tokenize the new Candidate string
                st2 = new StringTokenizer(newCandidateString);
                //if the Candidate is not a duplicate of an existing Candidate and only one new item is added to make the new Candidate, add it to the Candidate vector
                if(!isDuplicateCandidate(newCandidateString) && st2.countTokens()==st1.countTokens()+1)
                {
                    tempCand = new Candidate(0, newCandidateString, dbRun, numRead, cand.itemsetNum+1);
//                    System.out.println("The new Candidate is '" + tempCand.itemset + "' with itemsetNum of " + tempCand.itemsetNum);
                    candidates.add(tempCand);
                }
            }
        }
    }
    
  
    public void display(int n)
    {
        //Output file
        FileWriter fw;
        BufferedWriter file_out;
        Candidate cand; //temporary holder for a Candidate
        Vector<String> itemsets = new Vector<String>(); //holder, for display purposes, of the itemset string representations
        boolean setExists = false; //if the set exists or not (aesthetics to prevent empty sets
        //open output file
        try
        {
            fw= new FileWriter(outputFile, true);
            file_out = new BufferedWriter(fw);
            //check each Candidate
            for(int c=0; c<candidates.size(); c++)
            {
                cand = candidates.get(c);
    //            System.err.println("Candidate: " + cand.dbRun + " actual: " + dbRun + " solid: " + cand.solid + " circle: " + cand.circle);
                //if it is in the n-itemset and is confirmed frequent
                if(cand.itemsetNum==n && cand.solid && !cand.circle)
                {
                    setExists=true; //there is at least one itemset
                    itemsets.add(cand.itemset); // add the item to the string vector to be displayed
                    //add to output file
                    file_out.write(cand.itemset + "," + (double)cand.count/numTransactions + "\n");
                }
            }
            file_out.write("-\n");
            //if there is at least one itemset, display the itemsets
            if(setExists)
            {
                System.out.println("Frequent " + n + "-itemsets:");
                System.out.println(itemsets);
                itemsets.clear();
            }
            file_out.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

   
    private void removeItemsets(int n)
    {
        //check each Candidate to see if its in the itemset to delete, if so, delete it
        for(int c=candidates.size()-1; c>=0; c--)
            if(candidates.get(c).itemsetNum<=n)
                candidates.remove(c);
    }
}
