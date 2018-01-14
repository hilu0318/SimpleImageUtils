package kr.hilu0318.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
	
	public static void main(String[] args){
		//BlurMatrix.getBlurMatrix(8);
		//System.out.println(Math.log10(4.5));
		
		String inFilePath = "C:/zzz/20171122_161756.jpg";
		String inFileName = inFilePath.substring(inFilePath.lastIndexOf('/')+1, inFilePath.lastIndexOf('.'));
		
		File infile = new File(inFilePath);
		File outfile = new File("C:/zzz/re_"+inFileName+".jpg");
		
		try {
			ImageFX imgE = new ImageFX(infile);
			
			BufferedImage newImg = imgE.imgBlur(4);
			
			ImageIO.write(newImg, "jpg", outfile);
			
		} catch (IOException e) {
			System.out.println("Not Exist File");
			return;
		} catch (InterruptedException e) {
			System.out.println("Not Exist File");
			return;
		}		
	}
}
