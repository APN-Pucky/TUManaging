package de.neuwirthinformatik.Alexander.TU.TUM.BOT;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Fusion;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.util.Curl;

public class Creator 
{
	
	private Bot b;
	private Curl curl;
	
	/*public Creator(Curl c)
	{
		this.curl = c;
	}*/
	
	/*public Creator(Bot b)
	{
		this.b = b;
		this.curl = b.getCurl();
	}*/
	
	//fuse necessary
	public int couldCreateCardCosts(Card c, Card[] inv, boolean first_call, int[] inv_ids)
	{
		if(first_call && ownsCardV2(c,false,inv_ids))return GlobalData.getSPNeededToMax(c);
		Fusion path = GlobalData.getFusionByID(c.getIDs()[0]);	
		if(path == Fusion.NULL) path = GlobalData.getFusionByID(c.getIDs()[c.getIDs().length-1]); //Double Octane fix
		//System.out.println(c.getName());
		if (path != Fusion.NULL)
		{
			int cost = GlobalData.getSPNeededToMax(c);
			//System.out.println(c.getName() + " = ");
			for(int id : path.getMaterials())
			{
				//System.out.print(" 	" + Data.getCardByID(id) + " + \n");
				if(ownsCard(GlobalData.getCardByID(id),inv))
				{
					//System.out.println("OKKK1");
					remove(GlobalData.getCardByID(id),inv);
					cost += GlobalData.getSPNeededToMax(GlobalData.getCardByID(id));
				}
				else	
				{
					
					if((cost += couldCreateCardCosts(GlobalData.getCardByID(id),inv,false,inv_ids))>0)
					{
						/*for(int iii : d.getFusionByID(d.getCardByID(id).getIDs()[0]).getMaterials())
							{
								System.out.println("OKKK2");
									inv = remove(d.getCardByID(iii),inv);							
							}*/
					}
					else
					{
						return -100000000;
					}
				}
			}
			return cost;
		}
		return -100000000;
	}
	
	//fuse necessary
	public boolean couldCreateCard(Card c, Card[] inv, boolean first_call, int[] inv_ids)
	{
		if(first_call && ownsCardV2(c,false,inv_ids))return true;
		Fusion path = GlobalData.getFusionByID(c.getIDs()[0]);	
		if(path == Fusion.NULL) path = GlobalData.getFusionByID(c.getIDs()[c.getIDs().length-1]); //Double Octane fix
		//System.out.println(c.getName());
		if (path != Fusion.NULL)
		{
			//System.out.println(c.getName() + " = ");
			for(int id : path.getMaterials())
			{
				//System.out.print(" 	" + Data.getCardByID(id) + " + \n");
				if(ownsCard(GlobalData.getCardByID(id),inv))
				{
					//System.out.println("OKKK1");
					remove(GlobalData.getCardByID(id),inv);
				}
				else
				{
					
					if(couldCreateCard(GlobalData.getCardByID(id),inv,false,inv_ids))
					{
						/*for(int iii : d.getFusionByID(d.getCardByID(id).getIDs()[0]).getMaterials())
						{
							System.out.println("OKKK2");
							inv = remove(d.getCardByID(iii),inv);							
						}*/
					}
					else
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public void remove(Card c,Card[]inv)
	{
		for(int i =0; i < inv.length; i++)
		{
			if(c.equals(inv[i]))
			{
				inv[i]=Card.NULL;
				return;
			}
		}
	}
	
	public boolean ownsCard(Card c,Card[] inv)
	{
		for(Card i : inv)
		{
			if(c != null && i !=null && c.equals(i))return true;
		}
		return false;
	}
	
	public boolean ownsCard(int c,int[] inv)
	{
		for(int i : inv)
		{
			if(c == i)return true;
		}
		return false;
	}
	
	/*public void createCard(Card c)
	{
		try{
		Fusion path = Data.getFusionByID(c.getIDs()[0]);	
		if(path == null) path = Data.getFusionByID(c.getIDs()[c.getIDs().length-1]);
		if (path != null)
		{
			for(int id : path.getMaterials())
			{
				createCard(Data.getCardByID(id));
			}
			fuseCards(path);
			maxCard(Data.getCardByID(path.getID()));
		}
		else
		{
			maxCard(c);
		}}catch(Exception e){e.printStackTrace();}
	}*/
	
	public boolean ownsCardV2(Card c, boolean last_included, int[] inv)
	{
		int[] arr = c.getIDs();
		for( int i =0; i < arr.length - (last_included?0:1);i++)
		{
			for(int id : inv) if(id==arr[i])return true;
		}
		return false;
	}
	
	public void createCardV2(Card c, boolean first, int[] inv)
	{
		try{
		Fusion path = GlobalData.getFusionByID(c.getIDs()[0]);	
		if(path == Fusion.NULL) path = GlobalData.getFusionByID(c.getIDs()[c.getIDs().length-1]);
		TUM.log.d("owns : "+ c.getName() + " " + ownsCardV2(c,!first,inv), b.getName(), "Creator");
		if (!ownsCardV2(c,!first,inv)  && path != Fusion.NULL)
		{
			boolean nxt = false;
			if(GlobalData.hasDuplicates(path.getMaterials()))nxt = true;
			for(int i=0;i<path.getMaterials().length;i++)
			{
				createCardV2(GlobalData.getCardByID(path.getMaterials()[i]),(i==0)?false:nxt,inv);
			}
			fuseCards(path);
			maxCard(GlobalData.getCardByID(path.getID()),inv);
		}
		else
		{
			maxCard(c,inv);
		}}catch(Exception e){e.printStackTrace();}
	}
	
	public void fuseCards(Fusion f) throws Exception
	{
		//FuseCard f.getID(); 
		curl.fuseCard(f.getID());
		TUM.log.d("Fused Card: " + GlobalData.getNameByID(f.getID()), b.getName(), "Creator");
		Thread.sleep(500);
	}
	
	public void maxCard(Card c, int[] inv) throws Exception
	{
		if(c.getName().equals("Dominion Shard"))return;
		int[] ids = c.getIDs();
		int i = 0;
		for(int j =0; j < ids.length-1;j++)
		{
			if(GlobalData.getCount(inv, ids[j])>0)i=j;
		}
		for(; i < ids.length-1;i++)
		{
			curl.upgradeCard(ids[i]);
			Thread.sleep(500);
		}
		TUM.log.d("Maxed Card: " + c.getName(), b.getName(), "Creator");
	}
}
