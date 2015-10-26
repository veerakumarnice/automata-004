/* 
 * CYK algorithm for Context Free Language
 * Author: Chenguang Zhu
 * CS154, Stanford University
 */

import java.io.*;

public class CYK{
	static int maxProductionNum = 100; //max number of productions
	static int VarNum = 4;
	
	int [][] production = new int[maxProductionNum+1][3];
	//Prouductions in Chomsky Normal Form (CNF)
	//production[i][0] is the number for the variable (0~3, 0: S 1: A, 2: B, 3: C)
	//If this production is A->BC (two variables), then production[i][1] and production[i][2] will contain the numbers for these two variables
	//If this production is A->a (a single terminal), then production[i][1] will contain the number for the terminal (0 or 1, 0: a, 1: b), production[i][2]=-1
	
	boolean [][][] X; 
	//X[i][j][s]=true if and only if variable s (0~3, 0: S 1: A, 2: B, 3: C) is in X_ij defined in CYK
	//Suppose the length of string to be processed is L, then 0<=i<=j<L
	
	//check whether (a,b,c) exists in production
	boolean existProd(int a,int b,int c)
	{
		int i;
		for (i=0; i<production.length; ++i)
			if ((production[i][0]==a)&&(production[i][1]==b)&&(production[i][2]==c))
				return true;
		return false;
	}
	
	
	// CYK algorithm
	// Calculate the array X
	// w is the string to be processed
	void calcCYK(int [] w) throws IOException
	{
		int L = w.length;
		X=new boolean [L][L][VarNum];
		//Fill in your program here
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (int k = 0; k < L; k++) {
            
         //   br.readLine();
                if (k == 0) {
                    for (int i = 0 ; i < L; i++) {    
                        for (int m = 0; m < 4; m++) {
                            X[i][i][m] = existProd(m,w[i],-1);
                        }
                    }
                    continue;
                }
            for (int i = 0; (i + k) < L;  i++) {
             //   System.out.println("Entering i " + i);
                    for (int temp = 0 ; temp < k; temp++) {
                        for (int a = 0; a < 4; a++) {
                            if (X[i][i+temp][a]) {
              //                  System.out.println("entered for a = " + a);
                                for (int b = 0; b < 4; b++) {                        
                                    if (X[i+temp+1][i+k][b]) {
                //                        System.out.println("entered for b = " + b);
                                        for (int m = 0; m < 4; m++) {
                                            //X[i][i+k][m] = existProd(m,a,b);
                                            if (existProd(m,a,b)) {
                  //                              System.out.println("for i= " +i+" j = "+(i+k)+" production present for m = " + m);
                                                X[i][i+k][m] = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
            
        }        
        //br.close();
    }
	
	public String Start(String filename)
	{
		String result="";
		//read data case line by line from file
		try{
			FileInputStream fstream = new FileInputStream(filename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str; //the string read in
			
			//example on Page 8 of lecture 15_CFL5
			production=new int[7][3];
			production[0][0]=0; production[0][1]=1; production[0][2]=2;  //S->AB
			production[1][0]=1; production[1][1]=2; production[1][2]=3;  //A->BC
			production[2][0]=1; production[2][1]=0; production[2][2]=-1; //A->a
			production[3][0]=2; production[3][1]=1; production[3][2]=3;  //B->AC
			production[4][0]=2; production[4][1]=1; production[4][2]=-1; //B->b
			production[5][0]=3; production[5][1]=0; production[5][2]=-1; //C->a
			production[6][0]=3; production[6][1]=1; production[6][2]=-1; //C->b
			
			result="";
			//Read File Line By Line
			while ((str = br.readLine()) != null) {
				
				System.out.println ("Processing "+str+"...");
				int len=str.length();
				int [] w= new int[len];
				for (int i=0; i<len; ++i)
					w[i]=str.charAt(i)-'a';  //convert 'a' to 0 and 'b' to 1
				//Use CYK algorithm to calculate X
				calcCYK(w);
				//Get/print the full table X
				for (int step=len-1; step>=0; --step)
				{
					for (int i=0; i<len-step; ++i)
					{
						int j=i+step;
						for (int k=0; k<VarNum; ++k)
							if (X[i][j][k])
							{
								result=result+k;
							}
						result=result+" ";
					}
					result=result+"\n";
				}
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			result=result+"error\n";
			System.err.println("Error: " + e.getLocalizedMessage());
		}
		return result;
	}
	
	public static void main(String args[]) throws Exception {
		new CYK().Start("testCYK.in");
	}
}
