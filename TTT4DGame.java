import java.util.Scanner;

public class TTT4DGame
{
	private int[][][][] board;
	private char[] players;
	private int[] wins;
	int playerNum;
	private boolean middleInvalid;
	
	public TTT4DGame()
	{
		board=new int[3][3][3][3];
		
		char[] temp={' ', 'X', 'O'};
		setPlayers(temp);
		
		middleInvalid=true;
	}
	
	public void setPlayers(char[] in)
	{
		players=in;
		playerNum=players.length;
		wins=new int[playerNum];
		checkForWins();
	}
	
	//plays through a human vs. human turn
	//returns if to quit
	public boolean playTurnHVH()
	{
		Scanner reader=new Scanner(System.in);
		
		for (int i=1; i<playerNum; i++)
		{
			String error=null;
			
			do
			{
				System.out.println("\n\n\n\n\n\n\n_________________________________\n");
				System.out.println(toString());
				if (error!=null)
					System.out.println("ERROR: "+error+"\n");
				System.out.print(players[i]+" move: ");
				String in=reader.next();
				if (in.equals("quit"))
					return true;
				System.out.println();
				int[] cords=new int[4];
				error=processInputString(in, cords);
				if (error==null)
					error=move(i, cords);
			}
			while (error!=null);
		}
		
		return false;
	}
	
	public String processInputString(String in, int[] out)
	{
		if (in.length()==4)
		{
			for (int i=0; i<4; i++)
			{
				int c=in.charAt(i);
				
				if (c>='1' && c<='3')
				{
					c=c-'1';
				}
				else if (c>='a' && c<='c')
				{
					c=c-'a';
				}
				else
					return "'"+(char)c+"' character invalid";
				
				out[i]=c;
			}
		}
		else if (in.length()==2)
		{
			for (int i=0; i<2; i++)
			{
				int c=in.charAt(i);
				
				if (c>='1' && c<='9')
				{
					c=c-'1';
				}
				else if (c>='a' && c<='z')
				{
					switch (c)
					{
						
					case 'z':
						c=0;
						break;

					case 'x':
						c=1;
						break;

					case 'c':
						c=2;
						break;

					case 'a':
						c=3;
						break;

					case 's':
						c=4;
						break;

					case 'd':
						c=5;
						break;

					case 'q':
						c=6;
						break;

					case 'w':
						c=7;
						break;

					case 'e':
						c=8;
						break;
					}
				}
				else
					return "'"+(char)c+"' character invalid";
				
				out[i*2]=c%3;
				out[i*2+1]=2-c/3;
			}
		}
		else
			return "length of input invalid";
		
		for (int i : out)
		{
			if (i<0 || i>2)
				return "invalid input";
		}
		
		return null;
	}
	
	public String move(int player, int[] c)
	{
		if (c.length!=4)
			return "called move() with wrong cord array size";
		else
			return move(player, c[0], c[1], c[2], c[3]);
	}
	
	public String move(int player, int x, int y, int z, int w)
	{
		if (player<1)
		{
			return"tried to move with player 0 or negitive, which is invalid";
		}
		
		if (player>playerNum)
		{
			return "tried to move with player number heigher then number of players";
		}
		
		if (middleInvalid && x==1 && y==1 && z==1 && w==1)
		{
			return "tried to move into middle vox, which is not a valid move";
		}
		
		if (board[x][y][z][w]==0)
			board[x][y][z][w]=player;
		else
			return "tried to move in occupied location";
		
		return null;
	}
	
	public void checkForWins()
	{
		int x, y, z, w;
		
		for (int i=0; i<playerNum; i++)
			wins[i]=0;
		
		for (x=0; x<4; x++)
		{
			for (y=0; y<(x==3?5:4); y++)
			{
				for (z=0; z<(x==3||y==3?5:4); z++)
				{
					for (w=(x==3||y==3||z==3?0:3); w<(x==3||y==3||z==3?5:4); w++)
					{
						if (!middleInvalid || !(x==1 || x>2) || !(y==1 || y>2) || !(z==1 || z>2) || !(w==1 || w>2))
						{
							int ans=getIfRowWin(x, y, z, w);
							
							if (ans!=0)
								wins[ans]++;
						}
					}
				}
			}
		}
	}
	
	int getIfRowWin(int x, int y, int z, int w)
	{
		int[] in=new int[4];
		int[] cords=new int[4];
		int fwd, bkwd;
		
		in[0]=x;
		in[1]=y;
		in[2]=z;
		in[3]=w;
		
		fwd=0;
		bkwd=2;
		
		for (int i=0; i<4; i++)
			cords[i]=in[i]==3?fwd:in[i]==4?bkwd:in[i];
		
		int player=board[cords[0]][cords[1]][cords[2]][cords[3]];
		
		if (player==0)
			return 0;
		
		fwd=1;
		bkwd=1;
		
		for (int i=0; i<4; i++)
			cords[i]=in[i]==3?fwd:in[i]==4?bkwd:in[i];
		
		if (player!=board[cords[0]][cords[1]][cords[2]][cords[3]])
			return 0;
		
		fwd=2;
		bkwd=0;
		
		for (int i=0; i<4; i++)
			cords[i]=in[i]==3?fwd:in[i]==4?bkwd:in[i];
		
		if (player!=board[cords[0]][cords[1]][cords[2]][cords[3]])
			return 0;
		
		return player;
	}
	
	public String toString()
	{
		String out="";
		
		int x, y, z, w;
		
		checkForWins();
		
		out+="     <--------X-------->\n";
		out+="     <-Z->  <-Z->  <-Z->\n";
		
		for (y=0; y<3; y++)
		{
			for (w=0; w<3; w++)
			{
				if (y==1 && w==1)
					out+="Y ";
				else if (y==0 && w==0)
					out+="^ ";
				else if (y==2 && w==2)
					out+="v ";
				else
					out+="| ";
				
				if (w==0)
					out+="^  ";
				else if (w==2)
					out+="v  ";
				else
					out+="W  ";
				
				//if (w==1)
				//	out+="W  ";
				//else
				//	out+="|  ";
				
				for (x=0; x<3; x++)
				{
					for (z=0; z<3; z++)
					{
						if (middleInvalid && x==1 && y==1 && z==1 && w==1)
							out+="#";
						else
							out+=players[board[x][y][z][w]];
						
						if (z<2)
							out+="|";
					}
					
					if (x<2)
						out+="  ";
				}
				
				if (w<2)
				{
					out+="\n| |  -+-+-  -+-+-  -+-+-";
				}
				
				out+="\n";
			}
			
			if (y<2)
				out+="|\n";
		}
		
		out+="\n";
		
		for (int i=1; i<playerNum; i++)
		{
			out+=players[i]+": "+wins[i]+(i==playerNum-1?"\n":"   ");
		}
		
		return out;
	}
}
