package de.neuwirthinformatik.Alexander.TU.TUM;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class BotOverviewPanel extends JPanel
{
	Bot[] bs;
	JTable table;
	DefaultTableModel model ;
	
	public BotOverviewPanel()
	{
		super(new GridLayout(1,0));
		this.bs = GlobalBotData.bots;
		
		String[] columnNames = {
				"Name",
                "Guild",
                "Money",
                "WB",
                "Salvage",
                "Fund"
                };
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][5];
		int j=0;
		for(int i = 0; i < bs.length;i++)
		{
			if(bs[i] !=null)
			{
				data[j] = new Object[]{bs[i].getName(), bs[i].getGuild(), new Integer(bs[i].getMoney()), new Integer(bs[i].getWB()),new Integer(bs[i].getSalvage()), new Integer((bs[i].getFund()))};
				j++;
			}
		}
		
		
		model = new DefaultTableModel(data,columnNames) {
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    case 3:
                        return Integer.class;
                    default:
                        return Integer.class;
                }
            }
        };
        
        
       
		
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		
		
		
		table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
	    table.setFillsViewportHeight(true);
	    table.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseClicked(java.awt.event.MouseEvent evt) {
	            int row = table.rowAtPoint(evt.getPoint());
	            int col = table.columnAtPoint(evt.getPoint());
	            
	            //new Thread(new Runnable(){public void run(){ new ViewBot(getBotByName((String)table.getValueAt(row, 0)));}}).start();
	            
	        }});
	    
		JScrollPane scrollPane = new JScrollPane(table);
		
		
		

		JPanel p = new JPanel();
		p.add(GUI.buttonAsync("Select all decks",() -> table.selectAll()));
		
		p.add(GUI.buttonAsync("Unselect all decks", () -> table.clearSelection()));
		
		p.add(GUI.buttonAsync("Update Overview", () -> update()));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab("Managing", null, new JScrollPane(new BotManagingPanel()),
                "Manage Bots");
		if(Permissions.has("overview_simming")) {
			tabbedPane.addTab("Simming", null, new JScrollPane(new BotSimPanel()),
			                  "Start Sims");
		}
		if(Permissions.has("overview_botting")) {
			tabbedPane.addTab("Botting", null, new JScrollPane(new BotControlPanel()),
	                "Start Bots");
		}
		
		
		JSplitPane splitPaneRight= new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                p, tabbedPane);
		splitPaneRight.setOneTouchExpandable(true);
		splitPaneRight.setDividerLocation(100);

		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPane, splitPaneRight);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(500);
		
        //Add the scroll pane to this panel.
        this.add(splitPane);
		
	}
	
	public void update()
	{
		String[] columnNames = {"Name",
                "Guild",
                "Money",
                "WB",
                "Salvage",
                "Fund"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][5];
		
		TUM.pp.forEach("Updating Overview", bs, (b,j) ->
		{
			b.update();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(b.getMoney()), new Integer(b.getWB()),new Integer(b.getSalvage()), new Integer((b.getFund()))};
			return b.getName();
		});
		
		
		 model.setDataVector(data, columnNames);
	     model.fireTableDataChanged();
	}
	
	public Bot[] getSelectedBots()
	{
		int[] selection = table.getSelectedRows();
		Bot[] bots = new Bot[selection.length]; 
		for(int i =0;i< selection.length;i++)
		{
			bots[i] = GlobalBotData.getBotByName((String)table.getValueAt(selection[i], 0));
		}
		return bots;
	}
	
	
	
	
}
