package de.neuwirthinformatik.Alexander.TU.TUM.BOT;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Fusion;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.util.Curl;

public class CreatorV2 
{
	private Bot b;
	private Curl curl;
	
	public CreatorV2(Bot b)
	{
		this.b = b;
		this.curl = b.getCurl();
	}
	
	public void restoreCard(CardInstance r) throws Exception
	{
		curl.restore(r.getID());
		TUM.log.d("Restored Card: " + r, b.getName(), "Creator");
		Thread.sleep(500);
	}
	
	public void fuseCard(Fusion f) throws Exception
	{
		//FuseCard f.getID(); 
		curl.fuseCard(f.getID());
		TUM.log.d("Fused Card: " + GlobalData.getNameAndLevelByID(f.getID()), b.getName(), "Creator");
		Thread.sleep(500);
	}
	public void maxCard(CardInstance start, CardInstance goal) throws Exception
	{
		if(TUM.settings.ASSERT && !start.getCard().equals(goal.getCard())) throw new NullPointerException("Can't Max a card to another card type");
		int[] ids = start.getIDs();
		int i = start.getLevel()-1;
		for(; i < goal.getLevel()-1;i++)
		{
			curl.upgradeCard(ids[i]);
			Thread.sleep(500);
		}
		TUM.log.d("Maxed Card " + start + " to " + goal, b.getName(), "Creator");
	}
	
	public void createCard(Card c, int[] inv, int[] restore)
	{
		createCardInternal(GlobalData.getCardInstanceById(c.getHighestID()),GlobalData.getCardInstancesFromIDs(inv),GlobalData.getCardInstancesFromIDs(restore));
	}
	
	public void createCard(CardInstance goal, CardInstance[] inv, CardInstance[] restore)
	{
		inv = inv.clone();
		restore = restore.clone();
		createCardInternal(goal,inv,restore);
	}
	
	private void createCardInternal(CardInstance goal, CardInstance[] inv, CardInstance[] restore)
	{
		try
		{
			Fusion path = GlobalData.getFusionByID(goal.getIDs()[0]);	
			if(path == Fusion.NULL) path = GlobalData.getFusionByID(goal.getIDs()[goal.getIDs().length-1]);
			CreatorV2Return cr = couldUpgradeLowerTo(goal, inv, restore);
			//System.out.println(cr);
			
			if(cr.possible)
			{
				if(cr.restore_cards.length == 1)
				{
					restoreCard(cr.cards[0]);
					remove(cr.cards[0], restore); //ASSERT
				}
				else
				{
					remove(cr.cards[0],inv); //ASSERT
				}
				maxCard(cr.cards[0], goal);
			}
			else if(path == Fusion.NULL)
			{
				if(TUM.settings.ASSERT)TUM.log.e("Card " + goal +" can't be leveled or fused", "Creator", b.getName());
				//if(TUM.settings.ASSERT)throw new NullPointerException("Card can't be leveled or fused");
			}
			else
			{
				//fuse			
				for(int i=0;i<path.getMaterials().length;i++)
				{
					CardInstance c = GlobalData.getCardInstanceById(path.getMaterials()[i]);
					if(ownsCardInstance(c,inv))
						remove(c,inv);
					else
						createCardInternal(c,inv,restore);	
				}
				fuseCard(path);
				maxCard(GlobalData.getCardInstanceById(path.getID()), goal);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public CreatorV2Return couldCreateCard(Card c, int[] inv, int[] restore)
	{
		return couldCreateCard(GlobalData.getCardInstanceById(c.getHighestID()),GlobalData.getCardInstancesFromIDs(inv),GlobalData.getCardInstancesFromIDs(restore));
	}
	public CreatorV2Return couldCreateCard(CardInstance goal, CardInstance[] inv, CardInstance[] restore)
	{
		inv = inv.clone();
		restore = restore.clone();
		return couldCreateCardInternal(goal,inv,restore);
	}
	
	private CreatorV2Return couldCreateCardInternal(CardInstance goal, CardInstance[] inv, CardInstance[] restore)
	{
		CreatorV2Return tmp_ret = couldUpgradeLowerTo(goal,inv,restore);
		if(tmp_ret.possible)
		{
			if(!remove(tmp_ret.cards[0],inv) && !remove(tmp_ret.cards[0],restore) && TUM.settings.ASSERT)throw new NullPointerException("Card should be owned, but is not in Inventory or Restore");
			return new CreatorV2Return(true,tmp_ret.sp_cost, new CardInstance[]{}, tmp_ret.restore_cards);
		}
		Fusion path = GlobalData.getFusionByID(goal.getIDs()[0]);
		if(path == Fusion.NULL) path = GlobalData.getFusionByID(goal.getIDs()[goal.getIDs().length-1]);
		if(path != Fusion.NULL)
		{
			CardInstance fusion_product = GlobalData.getCardInstanceById(path.getID());
			int cost = GlobalData.getSPNeededToLevelTo(fusion_product,goal);
			CreatorV2Return ret = new CreatorV2Return(true,cost);
			//Fusion path
			for(int id : path.getMaterials())
			{
				CardInstance c = GlobalData.getCardInstanceById(id);
				if(ownsCardInstance(c,inv))
				{
					remove(c,inv);
				}
				else
				{
					CreatorV2Return next_ret = couldCreateCardInternal(c,inv,restore);
					ret = ret.combine(next_ret);
					/*if(!next_ret.possible)
					{
						return new CreatorV2Return(false,-1,CardInstance.NULL);
					}
					cost += next_ret.sp_cost;*/
				}
			}
			//fix this
			return ret;
		}
		return new CreatorV2Return(false,-1,goal);
	}
	
	//public boolean ownsCard(CardInstance c, Card[] inv, Card[] restore)
	
	public boolean ownsCardInstance(CardInstance goal, CardInstance[] inv)
	{
		return GlobalData.contains(inv, goal);
	}
	
	public boolean remove(CardInstance rm, CardInstance[] inv)
	{
		for(int i = 0; i < inv.length;i++){
			if(inv[i].equals(rm))
			{
				inv[i] = CardInstance.NULL;
				return true;
			}
		}
		return false;
	}
	
	public CreatorV2Return couldUpgradeLowerTo(CardInstance goal, CardInstance[] inv, CardInstance[] restore)
	{
		int costs = 0;
		CardInstance c = goal;
		for(int level = goal.getLevel()-2; level >= 0;level--)
		{
			int id = goal.getIDs()[level];
			c= GlobalData.getCardInstanceById(id,goal.getCard());
			//System.out.println(c.toString());
			costs += GlobalData.getUpgradeCosts(c);
			if(GlobalData.contains(inv, c))
			{
				return new CreatorV2Return(true,costs,c);
			}
		}
		//c == goal.getLowestID()
		if(GlobalData.contains(restore,c)) return new CreatorV2Return(true,costs+GlobalData.getRestoreCosts(c),new CardInstance[]{c},new CardInstance[]{c});
		return new CreatorV2Return(false,-1);
	}
	
	public static class CreatorV2Return
	{
		public final int sp_cost;
		public final boolean possible;
		public final CardInstance[] cards; //either missing card(s) or could create max card(s)
		public final CardInstance[] restore_cards;
		
		public CreatorV2Return( boolean p,int sp, CardInstance[] m, CardInstance[] r)
		{
			sp_cost = sp;
			possible = p;
			cards = m;
			restore_cards = r;
		}
		
		public CreatorV2Return( boolean p,int sp, CardInstance[] m)
		{
			this(p,sp,m,new CardInstance[]{});
		}
		
		public CreatorV2Return(boolean p,int sp,  CardInstance m)
		{
			this(p,sp,new CardInstance[]{m});
		}
		
		public CreatorV2Return(boolean p,int sp)
		{
			this(p,sp,new CardInstance[]{});
		}
		
		public CreatorV2Return combine(CreatorV2Return a)
		{
			return new CreatorV2Return(a.possible && possible, a.sp_cost+sp_cost, GlobalData.concat(cards,a.cards),GlobalData.concat(restore_cards,a.restore_cards));
		}
		
		public String toString() { return "[" + possible + "," + sp_cost + "]: Card: " + GlobalData.getDeckString(cards) + " Restore: " + GlobalData.getDeckString(restore_cards);}
	}
}
