package aplication.game;

public class OXEngine implements OXInterface{
	int numberOfField;
	public int step = 0;

	char[] OXTable = new char[9];

	public Player player;
	public Result result;

	@Override
	public void setField(int numberOfField) { 

		this.numberOfField = numberOfField;

		if (player == Player.playerO) 
			OXTable[numberOfField] = 'O';
		else if (player == Player.playerX)
			OXTable[numberOfField] = 'X';

	}

	@Override
	public void checkResult() {

		if (checkTable() == 1) {
			result = Result.winX;

		} else if (checkTable() == 2) {
			result = Result.winO;

		} else if (step == 9 && checkTable() == 0) {
			result = Result.draw;

		}

	}

	@Override
	public void Init() {
		for (int i = 0; i <= 8; i++) {
			OXTable[i] = '\0';
		}

		step = 0;
		player = Player.playerNone;
		result = Result.none;
		player = Math.random() < 0.5 ? Player.playerO : Player.playerX;

	}

	@Override
	public int blockField(int number) {

		if (OXTable[number] == '\0')
			return 1;
		else
			return 0;
	}

	public void changePlayer() {

		if (player == Player.playerO)
			player = Player.playerX;
		else if (player == Player.playerX)
			player = Player.playerO;
	}

	public int checkTable() {

		if (OXTable[0] == 'X' && OXTable[1] == 'X' && OXTable[2] == 'X'
				|| OXTable[3] == 'X' && OXTable[4] == 'X' && OXTable[5] == 'X'
				|| OXTable[6] == 'X' && OXTable[7] == 'X' && OXTable[8] == 'X'
				|| OXTable[0] == 'X' && OXTable[3] == 'X' && OXTable[6] == 'X'
				|| OXTable[1] == 'X' && OXTable[4] == 'X' && OXTable[7] == 'X'
				|| OXTable[2] == 'X' && OXTable[5] == 'X' && OXTable[8] == 'X'
				|| OXTable[0] == 'X' && OXTable[4] == 'X' && OXTable[8] == 'X'
				|| OXTable[2] == 'X' && OXTable[4] == 'X' && OXTable[6] == 'X') {

			return 1; 

		}
		else if (OXTable[0] == 'O' && OXTable[1] == 'O' && OXTable[2] == 'O'
				|| OXTable[3] == 'O' && OXTable[4] == 'O' && OXTable[5] == 'O'
				|| OXTable[6] == 'O' && OXTable[7] == 'O' && OXTable[8] == 'O'
				|| OXTable[0] == 'O' && OXTable[3] == 'O' && OXTable[6] == 'O'
				|| OXTable[1] == 'O' && OXTable[4] == 'O' && OXTable[7] == 'O'
				|| OXTable[2] == 'O' && OXTable[5] == 'O' && OXTable[8] == 'O'
				|| OXTable[0] == 'O' && OXTable[4] == 'O' && OXTable[8] == 'O'
				|| OXTable[2] == 'O' && OXTable[4] == 'O' && OXTable[6] == 'O') {

			return 2;

		} else
			return 0; 
	}
	

}
