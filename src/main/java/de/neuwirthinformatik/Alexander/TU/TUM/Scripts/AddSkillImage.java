package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;

public class AddSkillImage {
	public static void main(String[] args) throws Exception
	{
		GlobalData.init();
		Image cur = ImageIO.read(TUM.class.getResource("/resources/"+"skills0"+".png"));
		//Image hunt = ImageIO.read(TUM.class.getResource("/resources/"+"hunt"+".png"));
		//Image fortify = ImageIO.read(TUM.class.getResource("/resources/"+"fortify"+".png"));
		Image disease = ImageIO.read(TUM.class.getResource("/resources/"+"disease"+".png"));
		
		int w = cur.getWidth(null);
		int h = cur.getHeight(null);
		BufferedImage img =
				  new BufferedImage(w, h,
				                    BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.drawImage(cur, 0, 0, w, h, 0, 0, w,h,null);
		//g.drawImage(fortify,16,96,16+16,96+16, 0,0,fortify.getWidth(null),fortify.getHeight(null),null);
		//g.drawImage(hunt,16+16,96,16+16+16,96+16, 0,0,hunt.getWidth(null),hunt.getHeight(null),null);
		//g.drawImage(mark,16+32+16,96,16+16+32+16,96+16, 0,0,scavenge.getWidth(null),scavenge.getHeight(null),null);
		g.drawImage(disease,16+32+16+16,96,16+16+32+16+16,96+16, 0,0,disease.getWidth(null),disease.getHeight(null),null);
		ImageIO.write(img, "png", new File("skills1.png"));

		
		ImageIcon iicon = new ImageIcon(img);
		JFrame jframe = new JFrame();
		jframe.setLayout(new FlowLayout());
		jframe.setSize(400, 600);
		JLabel lbl = new JLabel();
		lbl.setIcon(iicon);
		jframe.add(lbl);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
