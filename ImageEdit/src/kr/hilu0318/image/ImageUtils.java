package kr.hilu0318.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

class ImageUtils {
	
	public static BufferedImage changeRotate(File in) throws IOException{
		BufferedImage img = ImageIO.read(in);
		BufferedImage temp = null;
		
		try {
			Metadata meta = ImageMetadataReader.readMetadata(in);
			Directory directory = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if(directory == null)
				return img;
			
			int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			/*
			 * 1 -> 그대로
			 * 2 -> 좌우대칭
			 * 3 -> 180도 회전
			 * 4 -> 180도 회전 및 좌우대칭
			 * 5 -> 90도 회전 및 좌우대칭
			 * 6 -> 90도 회전
			 * 7 -> 270도 회전 및 좌우대칭
			 * 8 -> 270도 회전
			 */
			switch(orientation){
				case 3:{
					temp = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
					Graphics2D g = temp.createGraphics();
					g.rotate(Math.toRadians(180.0), temp.getWidth()/2, temp.getHeight()/2);
					g.translate((temp.getWidth() - img.getWidth())/2, (temp.getHeight() - img.getHeight())/2);
					g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
					g.dispose();
					break;
				}
				case 6:{
					temp = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
					Graphics2D g = temp.createGraphics();
					g.rotate(Math.toRadians(90.0), temp.getWidth()/2, temp.getHeight()/2);
					g.translate((temp.getWidth() - img.getWidth())/2, (temp.getHeight() - img.getHeight())/2);
					g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
					g.dispose();
					break;
				}
				case 8:{
					temp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
					Graphics2D g = temp.createGraphics();
					g.rotate(Math.toRadians(270.0), temp.getWidth()/2, temp.getHeight()/2);
					g.translate((temp.getWidth() - img.getWidth())/2, (temp.getHeight() - img.getHeight())/2);
					g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
					g.dispose();
					break;
				}
				default:{
					temp = img;
				}
			}	
			return temp;
		} catch (ImageProcessingException | IOException | MetadataException e1) {
			return img;
		}
	}
	
}
