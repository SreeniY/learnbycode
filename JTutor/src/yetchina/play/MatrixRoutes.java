package yetchina.play;

public class MatrixRoutes{
	static char[][] data = {{'A', 'B', 'C', 'D'}, { 'E', 'F', 'G', 'H'}, {'I', 'J', 'K', 'L'}, {'M', 'N', 'O', 'P'}};
	static int c1=0;
		public void SetMatrix(char[][] inData ){
			data = inData;
		}
		public static void findPaths(char stChar, char endChar){
			char startChar = stChar;
			char destinationChar = endChar;
			int[] startCharPos = findCharPosition(data,startChar);
			int[] destinationCharPos = findCharPosition(data,destinationChar);
			int totalPaths = 0;
			
			int i=0,j=0;
			int x=0,y=0;
			totalPaths = findPossiblePath(i,j,destinationCharPos[0],destinationCharPos[1]);
			System.out.println("Total paths between "+stChar+" and "+endChar+" are = "+totalPaths);
		}
		static int findPossiblePath(int st1,int st2,int d1,int d2){
			int c=0;
			//int l1=d1;
			//int l2=d2;
			for(int l1=d1;l1>=st1;l1--){
				c+=findD1P(st1,st2,d1,d2,l1);
			}
			for(int l2=d2-1;l2>st2;l2--){
				c+=findD2P(st1,st2,d1,d2,l2);
			}
			// System.out.println("Total paths between "++" are = "+c);
			//find(st1,st2,d1,d2,l1,l2);
			return c;	
		}
		static int findD1P(int st1,int st2,int d1,int d2,int l1){
			System.out.print("Visiting Nodes:");
			int i=st1,j=st2;
			//for(int i=st1,j=st2;j<=l2;j++){
				//System.out.print("("+i+j+")");
				while(i<l1){					
					System.out.print("("+i+j+")");
					i++;	
				}
				if(l1!=d1||j<d2){
					while(j<d2){
						System.out.print("("+i+j+")");
						j++;
					}
						
				}
				if(i<=d1){
					while(i<=d1){
						System.out.print("("+i+j+")");
						i++;
					}
				}
				System.out.println();
			//}
			return 1;
			
		}
		static int findD2P(int st1,int st2,int d1,int d2,int l2){
			int i=0,j=0;
			System.out.print("Visiting Nodes:");
			while(j<l2){					
				System.out.print("("+i+j+")");
				j++;	
			}
			if(l2!=d2||i<d1){
				while(i<d1){
					System.out.print("("+i+j+")");
					i++;
				}
					
			}
			if(j<=d2){
				while(j<=d2){
					System.out.print("("+i+j+")");
					j++;
				}
			}
			System.out.println();
			return 1;
		}
		static int find(int st1,int st2,int d1,int d2,int l1,int l2){
			//int c=0;
			//System.out.println("Starting values st1 , st2 = "+st1+","+st2+" d1, d2 = "+d1+","+d2+" L1, L2 = "+l1+","+l2);
			System.out.print("Visiting Nodes:");
			int i=st1,j=st2;
			//for(int i=st1,j=st2;j<=l2;j++){
				//System.out.print("("+i+j+")");
				while(i<d1){					
					System.out.print("("+i+j+")");
					i++;	
				}
				if(l1!=d1||j<d2){
					while(j<d2){
						System.out.print("("+i+j+")");
						j++;
					}
						
				}
				if(i<=d1){
					while(i<=d1){
						System.out.print("("+i+j+")");
						i++;
					}
				}
				
				//System.out.print("("+i+j+")");
			//}
			System.out.println();
			System.out.print("Visiting Nodes:");
			l1--;
			for(i=st1,j=st2;j<=l2;j++){
				//System.out.print("("+i+j+")");
				while(i<l1){					
					System.out.print("("+i+j+")");
					i++;	
				}
				if(l1!=d1||j<d2){
					while(j<d2){
						System.out.print("("+i+j+")");
						j++;
					}
						
				}
				if(i<=d1){
					while(i<=d1){
						System.out.print("("+i+j+")");
						i++;
					}
				}
				
			}
			l1--;
			//System.out.println("l1= "+l1);
			System.out.println();
			if(l1>=0){
				System.out.print("Visiting Nodes:");
				for(i=st1,j=st2;j<=l2;j++){
					//System.out.print("("+i+j+")");
					while(i<l1){					
						System.out.print("("+i+j+")");
						i++;	
					}
					if(l1!=d1||j<d2){
						while(j<d2){
							System.out.print("("+i+j+")");
							j++;
						}
							
					}
					if(i<=d1){
						while(i<=d1){
							System.out.print("("+i+j+")");
							i++;
						}
					}
					
				}
			}
			l2--;
			//System.out.println("l1= "+l1);
			System.out.println();
			if(l2>=0){
				System.out.print("Visiting Nodes:");
				for(i=st1,j=st2;i<=l1;j++){
					//System.out.print("("+i+j+")");
					while(j<l2){					
						System.out.print("("+i+j+")");
						j++;	
					}
					if(l2!=d2||i<d1){
						while(i<d1){
							System.out.print("("+i+j+")");
							i++;
						}
							
					}
					if(j<=d2){
						while(j<=d2){
							System.out.print("("+i+j+")");
							j++;
						}
					}
					
				}
			}
			l2--;
			//System.out.println("l1= "+l1);
			System.out.println();
			if(l2>=0){
				System.out.print("Visiting Nodes:");
				for(i=st1,j=st2;i<=l1;j++){
					//System.out.print("("+i+j+")");
					while(j<l2){					
						System.out.print("("+i+j+")");
						j++;	
					}
					if(l2!=d2||i<d1){
						while(i<d1){
							System.out.print("("+i+j+")");
							i++;
						}
							
					}
					if(j<=d2){
						while(j<=d2){
							System.out.print("("+i+j+")");
							j++;
						}
					}
					
				}
			}
			System.out.println("l1="+l1+" l2="+l2);
			if(l1==0&&l2==0){
				System.out.print("Visiting Nodes:");
				for(i=st1,j=st2;i<=d1||j<=d2;i++,j++){
					//System.out.print("("+i+j+")");
					while(i<=d1&&j<=d2){
						System.out.print("("+i+j+")");
						i++;
						if(i<=d1)System.out.print("("+i+j+")");
						j++;
					}
				}
			}
			/*if(l2!=0){
				for(int i=st1,j=st2;j<=l2;i++,j++){
					//while(i<d1){
					if(i<d1){
						System.out.print("("+i+j+")");
						i++;
						if(j==d2)
							break;
						else{
							for(;j<d2;j++)
								System.out.print("("+i+","+j+")");						
						}
					}
					System.out.print("("+i+j+")");
				}
			}*/
			//System.out.println("Completed one path......................");
			System.out.println();
			
			l2--;
			//System.out.println("l2= "+l2+"d2= "+d2);
			System.out.print("Visiting Nodes:    ");
			if(l2!=0){
				//for(int i=st1,j=st2;j<=l2;j++){
				for(i=st1,j=st2;j<=l2;j++){
					while(i<d1){
						System.out.print("("+i+j+")");
						i++;
						if(j==d2)
							break;
						else{
							for(;j<d2;j++)
								System.out.print("("+i+","+j+")");						
						}
					}
					System.out.print("("+i+j+")");
				}
			}
			System.out.println();
			
			l2--;
			//System.out.println("l2= "+l2+"d2= "+d2);
			System.out.print("Visiting vertical Node: ");
			if(l2!=0){
				for(i=st1,j=st2;j<=l2;j++){
					while(i!=d1){
						System.out.print("("+i+j+")");
						i++;
						if(j==l2)
							break;
						else{
							for(;j<d2;j++)
								System.out.print("("+i+","+j+")");						
						}
					}
					System.out.print("("+i+j+")");
				}
			}
			System.out.println();
			
			//else{
				l1--;
				System.out.print("Visiting Nodes:");
				if(l1!=0){
					for(i=st1,j=st2;i<l1;i++){
						while(j!=d2){
							System.out.print("("+i+j+")");
							j++;
							if(i==l1)
								break;
							else{
								for(;i<d1;i++)
									System.out.print("("+i+j+")");
							}
						}
						System.out.print("( "+i+j+")");
					}
				}	
			//}
			return c1;
		}
		public static int[] findCharPosition(char[][] inMatrix, char inLetter){
			int[] charPos = {0,0};
		
			for(int i=0;i<inMatrix.length;i++){	    	
		    	for( int j=0; j<inMatrix[0].length;j++){	    		

		    		if(inMatrix[i][j]== inLetter ){
		    			charPos[0] = i;
		    			charPos[1] = j;
		    			//System.out.print(charPos[0]+" "+charPos[1]);
		    			break;
		    		}
		    		//System.out.print(inMatrix[i][j]+" ");
		    	}
			}
		return charPos;
		}
		public static void displayMatrix(char[][] inMatrix){
			for(int i=0;i<inMatrix.length;i++){	    	
		    	for( int j=0; j<inMatrix[0].length;j++){	    		
		    		System.out.print(inMatrix[i][j]+" ");
		    	}
		    	System.out.println("");	    		
		    }	    
		}
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			//displayMatrix(data);
			//Test1
			//findPaths('A', 'O');
			
			//Test2
			findPaths ('B', 'D');
		}
	}



