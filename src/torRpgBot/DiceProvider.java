package torRpgBot;

import java.util.Random;

public class DiceProvider implements TorDiceInterface {
	
	private static Random rand = new Random();

	@Override
	public int rolld6() {
		return rand.nextInt(6) + 1;
	}

	@Override
	public int rolld12() {
		return rand.nextInt(12) + 1;
	}

}
