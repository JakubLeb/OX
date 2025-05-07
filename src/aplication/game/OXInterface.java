package aplication.game;

public interface OXInterface {
	
	enum Player{
		playerX,playerO,playerNone
	}
	
	enum Result{
		winX,winO,draw,none
	}
	void setField(int number);
	
	int blockField(int number);
	
	void checkResult();
	
	void Init();
}
