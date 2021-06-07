package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO
{
	public List<Player> listAllPlayers()
	{
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));

				result.add(player);
			}
			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(Map<Integer, Team> teams)
	{
		String sql = "SELECT DISTINCT * "
					+ "FROM teams";

		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{
				if(!teams.containsKey(res.getInt("TeamID")))
				{
					Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
					teams.put(team.getTeamID(), team); 
				}
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Action> listAllActions()
	{
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{

				Action action = new Action(res.getInt("PlayerID"), res.getInt("MatchID"), res.getInt("TeamID"),
						res.getInt("Starts"), res.getInt("Goals"), res.getInt("TimePlayed"), res.getInt("RedCards"),
						res.getInt("YellowCards"), res.getInt("TotalSuccessfulPassesAll"),
						res.getInt("totalUnsuccessfulPassesAll"), res.getInt("Assists"),
						res.getInt("TotalFoulsConceded"), res.getInt("Offsides"));

				result.add(action);
			}
			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Match> listAllMatches()
	{
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
					+ "FROM Matches m, Teams t1, Teams t2 " 
					+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID "
					+ "ORDER BY m.date";
		
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{

				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"),
						res.getInt("m.teamHomeFormation"), res.getInt("m.teamAwayFormation"),
						res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(),
						res.getString("t1.Name"), res.getString("t2.Name"));

				result.add(match);

			}
			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza> getAdiacenze(Map<Integer, Team> teams)
	{
		String sql = "SELECT t1.teamId AS id1, t2.teamId AS id2, (t1.punti-t2.punti) AS peso FROM ( SELECT t1.teamId, SUM(t1.pareggi + t2.vittorie * 3) AS punti FROM ( SELECT t1.teamId, SUM(t1.vittorie + t2.vittorie) AS pareggi FROM  ( SELECT m.TeamHomeID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = 0 GROUP BY m.TeamHomeID ) AS t1, ( SELECT m.TeamAwayID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = 0 GROUP BY m.TeamAwayID ) AS t2 WHERE t1.teamId = t2.teamId GROUP BY t1.teamId ) AS t1, ( SELECT t1.teamId, SUM(t1.vittorie + t2.vittorie) AS vittorie FROM  ( SELECT m.TeamHomeID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = 1 GROUP BY m.TeamHomeID ) AS t1, ( SELECT m.TeamAwayID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = -1 GROUP BY m.TeamAwayID ) AS t2 WHERE t1.teamId = t2.teamId GROUP BY t1.teamId ) AS t2 WHERE t1.teamId = t2.teamId GROUP BY t1.teamId ) AS t1, (SELECT t1.teamId, SUM(t1.pareggi + t2.vittorie * 3) AS punti FROM ( SELECT t1.teamId, SUM(t1.vittorie + t2.vittorie) AS pareggi FROM  ( SELECT m.TeamHomeID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = 0 GROUP BY m.TeamHomeID ) AS t1, ( SELECT m.TeamAwayID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = 0 GROUP BY m.TeamAwayID ) AS t2 WHERE t1.teamId = t2.teamId GROUP BY t1.teamId ) AS t1, ( SELECT t1.teamId, SUM(t1.vittorie + t2.vittorie) AS vittorie FROM  ( SELECT m.TeamHomeID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = 1 GROUP BY m.TeamHomeID ) AS t1, ( SELECT m.TeamAwayID AS teamId, COUNT(m.ResultOfTeamHome) AS vittorie FROM matches AS m WHERE m.ResultOfTeamHome = -1 GROUP BY m.TeamAwayID ) AS t2 WHERE t1.teamId = t2.teamId GROUP BY t1.teamId ) AS t2 WHERE t1.teamId = t2.teamId GROUP BY t1.teamId) AS t2 WHERE t1.teamId < t2.teamId AND t1.punti - t2.punti <> 0 ORDER BY t1.teamId, t2.teamId"; 
		
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{
				Team t1 = teams.get(res.getInt("id1")); 
				Team t2 = teams.get(res.getInt("id2")); 
				Integer diff = res.getInt("peso"); 
				if (t1 != null && t2 != null)
				{
					Adiacenza a = new Adiacenza(t1, t2, diff); 
					result.add(a);
				}
			}
			conn.close();
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
