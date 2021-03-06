package fi.kaamos.controller;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.kaamos.bean.Player;
import fi.kaamos.bean.PlayerRepository;

@RestController
public class EloratingController {

	@Autowired
	private PlayerRepository playerRepository;

	/**
	 * Adds a new player to the database
	 *
	 * @param Username
	 *            Username of the new player.
	 */

	@RequestMapping(value = "/addplayer", method = RequestMethod.POST)
	public @ResponseBody String addNew(@RequestParam String username) {
		Player player = new Player();
		player.setUsername(username);
		player.setScore(1000);
		player.setPlaycount(0);
		playerRepository.save(player);
		return "Player " + player.getUsername() + " added!";
	}

	/**
	 * Will fetch the two players by their username, checks their score and
	 * counts the the points that will be played.
	 *
	 * @param Winner
	 *            Username of the winner.
	 * @param Loser
	 *            Username of the loser.
	 * @return A message that tells the current score of the winner/loser and
	 *         shows the points it won/lost.
	 */

	@RequestMapping(value = "/game")
	public @ResponseBody String game(@RequestParam String winner, String loser) {

		Player wPlayer = playerRepository.findByUsername(winner);
		Player lPlayer = playerRepository.findByUsername(loser);
		
		wPlayer.setPlaycount(wPlayer.getPlaycount() + 1);
		lPlayer.setPlaycount(lPlayer.getPlaycount() + 1);
		
		// Count the expected winning percentage for both players
		DecimalFormat format = new DecimalFormat("0.00");
		double winnerExpectedPercentage = (1 / (Math.pow(10, (lPlayer.getScore() - wPlayer.getScore()) / 400) + 1));
		winnerExpectedPercentage = Double.valueOf(format.format(winnerExpectedPercentage));
		double loserExpectedPercentage = (1 / (Math.pow(10, (wPlayer.getScore() - lPlayer.getScore()) / 400) + 1));
		loserExpectedPercentage = Double.valueOf(format.format(loserExpectedPercentage));
		
		// Count the actual points
		int kFactor = 15;
		double winnerPoints = Double.valueOf(format.format(kFactor * (1 - winnerExpectedPercentage)));
		double loserPoints = Double.valueOf(format.format(kFactor * (0 - loserExpectedPercentage)));

		wPlayer.setScore(Math.round(wPlayer.getScore() + winnerPoints));
		lPlayer.setScore(Math.round(lPlayer.getScore() + loserPoints));

		playerRepository.save(wPlayer);
		playerRepository.save(lPlayer);

		String newLine = System.getProperty("line.separator");
		String result = wPlayer.getUsername() + " won " + winnerPoints + " points. new score is: " + wPlayer.getScore()
				+ newLine + "---------------------" + newLine + lPlayer.getUsername() + " lost " + loserPoints
				+ " points. new score is: " + lPlayer.getScore() + newLine;
		return result;
	}
}
