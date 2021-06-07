package it.polito.tdp.PremierLeague.model;

import java.util.*;

public class Simulatore
{
	Model model;

	// input
	private int N; // numero reporter per squadra
	private int X; // min num rep

	// parametri
	private final double PROB_VINCENTE = .5;
	private final double PROB_PERDENTE = .2;

	// mondo
	private Map<Integer, Team> teams;
	private Map<Team, Integer> reporter;
	private PriorityQueue<Match> partite; // eventi

	// output
	private int numPartiteCritiche ;

	// init
	public Simulatore(Model model, int N, int X, List<Team> teams, List<Match> partite)
	{
		this.model = model;

		this.N = N;
		this.X = X;

		this.teams = new HashMap<>(this.model.getTeams());

		this.reporter = new HashMap<>();
		for (Team t : teams)
			this.reporter.put(t, N);

		this.partite = new PriorityQueue<>(partite);
	}

	public void run()
	{
		numPartiteCritiche = 0;

		while (!this.partite.isEmpty())
		{
			Match m = this.partite.poll();

			// ricollocazione
			Team vincente = null;
			Team perdente = null;

			// controlla chi ha vinto
			int result = m.getReaultOfTeamHome();
			if (result > 0)
			{
				vincente = this.teams.get(m.getTeamHomeID());
				perdente = this.teams.get(m.getTeamAwayID());
			}
			else if (result < 0)
			{
				vincente = this.teams.get(m.getTeamAwayID());
				perdente = this.teams.get(m.getTeamHomeID());
			}

			if(this.reporter.get(vincente) != null && this.reporter.get(perdente) != null)
			{
				// ricollocazione per vincente
				this.ricollocazioneVincente(vincente);

				// ricollocazione per perdente
				this.ricollocazionePerdente(perdente);

				// controllo partite critiche
				if (this.reporter.get(vincente) < this.X || this.reporter.get(perdente) < this.X) 
					this.numPartiteCritiche++; 
			}

			System.out.println("\n\nMAPPA: " + this.reporter);
		}

		System.out.println(this.numPartiteCritiche);
	}

	private void ricollocazioneVincente(Team vincente)
	{
		//		if(this.reporter.get(vincente) != null)
		if (this.reporter.get(vincente) > 0)
		{
			double prob = Math.random();
			if (prob < this.PROB_VINCENTE)
			{
				// tolgo da vincente e lo riassegno a una squadra migliore
				this.reporter.put(vincente, this.reporter.get(vincente) - 1);

				List<Team> migliori = new ArrayList<>(this.model.getBattuteDa(vincente).keySet());

				if(!migliori.isEmpty())
				{
					int indexTeam = (int) (Math.random() * migliori.size());
					Team blasonato = migliori.get(indexTeam);
					if (blasonato != null) 
						this.reporter.put(blasonato, this.reporter.get(blasonato) + 1);
					else throw new IllegalArgumentException("CIAO HENRI");
				}
			}
		}
	}

	private void ricollocazionePerdente(Team perdente)
	{
		//		if(this.reporter.get(perdente) != null)
		if (this.reporter.get(perdente) > 0)
		{
			double prob = Math.random();
			if (prob < this.PROB_PERDENTE)
			{
				// tolgo da perdente e lo riassegno a una squadra peggiore
				int presenti = this.reporter.get(perdente); // num reporter presenti
				int daTogliere = (int) (Math.random() * presenti + 1);
				this.reporter.put(perdente, this.reporter.get(perdente) - daTogliere);


				List<Team> peggiori = new ArrayList<>(this.model.getHannoBattuto(perdente).keySet());

				if(!peggiori.isEmpty())
				{
					int indexTeam = (int) (Math.random() * peggiori.size());
					Team provincialotto = peggiori.get(indexTeam);
					if (provincialotto != null) 
						this.reporter.put(provincialotto, this.reporter.get(provincialotto) + daTogliere);
					else throw new IllegalArgumentException("CIAO HENRI");
				}
			}
		}
	}
}
