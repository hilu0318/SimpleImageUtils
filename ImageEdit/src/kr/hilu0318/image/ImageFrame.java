package kr.hilu0318.image;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageFrame {

	private File lt = new File("C:/zzz/Test/lt.png");
	private File rt = new File("C:/zzz/Test/rt.png");
	private File lb = new File("C:/zzz/Test/lb.png");
	private File rb = new File("C:/zzz/Test/rb.png");
	
	private File left = new File("C:/zzz/Test/left.png");
	private File right = new File("C:/zzz/Test/right.png");
	private File top = new File("C:/zzz/Test/top.png");
	private File bottom = new File("C:/zzz/Test/bottom.png");
	
	public void sese() throws IOException{
		BufferedImage b_lt = ImageIO.read(lt);
		BufferedImage b_rt = ImageIO.read(rt);
		BufferedImage b_lb = ImageIO.read(lb);
		BufferedImage b_rb = ImageIO.read(rb);
		
		BufferedImage b_left = ImageIO.read(left);
		BufferedImage b_right = ImageIO.read(right);
		BufferedImage b_top = ImageIO.read(top);
		BufferedImage b_bottom = ImageIO.read(bottom);
		
		if(b_lt == null || b_rt == null || b_lb == null || b_rb == null || b_left == null || b_right == null || b_top == null || b_bottom == null)
			throw new NullPointerException();
		BufferedImage bimg = ImageUtils.changeRotate(new File("ddd"));
		
		int type = (bimg.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB: BufferedImage.TYPE_INT_ARGB);		
		BufferedImage backimg = new BufferedImage(bimg.getWidth() + b_lt.getWidth() + b_rt.getWidth() - 40, bimg.getHeight() + b_lt.getHeight() + b_rt.getHeight() - 40, type);
		
		/* backimg에 그릴 수 있는 Graphics 인스턴스를 꺼내옴. */
		Graphics g = backimg.getGraphics();
		
		/* 배경 이미지에 테두리를 그리는 과정 */
		g.drawImage(b_lt, 0, 0, null);
		g.drawImage(b_rt, backimg.getWidth() - b_rt.getWidth(), 0, null);
		g.drawImage(b_lb, 0, backimg.getHeight() - b_lb.getHeight(), null);
		g.drawImage(b_rb, backimg.getWidth() - b_rb.getWidth(), backimg.getHeight() - b_rb.getHeight(), null);
		
		g.drawImage(b_left, 0, b_lt.getHeight(), b_left.getWidth(), backimg.getHeight() - (b_lt.getHeight()+b_lb.getHeight()), null);
		g.drawImage(b_right, backimg.getWidth() - b_right.getWidth(), b_rt.getHeight(), 
				b_left.getWidth(), backimg.getHeight() - (b_rt.getHeight() + b_rb.getHeight()), null);			
		g.drawImage(b_top, b_lt.getWidth(), 0, backimg.getWidth() - (b_lt.getWidth() + b_rt.getWidth()), b_top.getHeight(), null);
		g.drawImage(b_bottom, b_lb.getWidth(), backimg.getHeight() - b_bottom.getHeight(), 
				backimg.getWidth() - (b_lb.getWidth() + b_rb.getWidth()), b_bottom.getHeight(), null);
		/* 테두리가 그려진 그림 위에 원본 그림 그리기 */
		g.drawImage(bimg, b_lt.getWidth()-20, b_lt.getHeight()-20, null);
		g.dispose();
		
		/* 아래부터는 그림을 디스크에 기록하기 */
		
	}	
}
