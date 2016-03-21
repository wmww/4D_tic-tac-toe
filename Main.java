
public class Main
{
	public static void main(String[] args)
	{
		TTT4DGame game=new TTT4DGame();
		
		while (!game.playTurnHVH()) {}
		
		System.out.print("\ngame over");
	}
}
