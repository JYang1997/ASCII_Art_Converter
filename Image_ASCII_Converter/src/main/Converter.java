package main;
/**
 * @description
 * Program Converter
 * 			this program convert image (regular image type that can be handled by ImageIO. ex: PNG, JPEG, JPG)
 * 	This program include a default ASCII list for conversion, if otherwise, alternative ASCII list can be also
 * 	be used as conversion ASCII list. This program also included a gratify method which allow program to convert 
 * 	image into black-white images before been convert into ASCII art. note: this process is essential because the
 * 	ASCII conversion method is based on grey scale. 
 * 
 * 	@author: Junyao Yang
 * 	@date 01/03/2017
 */


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import javax.imageio.ImageIO;

public class Converter {
	
	
	//max color
	private final int MAXCOLOR = 255;

	//default list 
	private final Character[] DEFAULTLIST = {'@','&','#','%','*','+','=',':','-','.',' '};
	//private static final Character[] DEFAULTLIST ={'$','@','B','%','8','&','W','M','#','*','o','a','h','k','b','d','p','q','w','m','Z','O',
	//'0','Q','L','C','J','U','Y','X','z','c','v','u','n','x','r','j','f','t','/','\\','|','(',')','1','{','}','[',']','?','-','_',
	//		'+','~','<','>','i','!','l','I',';',':','"','^','`','\'','.',' '}; 
	
	//------------------------------------------------------------data------------------------------------------------------------------------
	//alternative ASCIIList provided by user
	
	private Character[] manList = null;
	
	private BufferedImage image = null;
	
	private Character[][] ASCII_Art = null;
	
	private int scaleFactor = 0;
	
	//------------------------------------------------------------------------------------------------------------------------------------------
	
	//----------------------------------------------------------getter-&-setter-----------------------------------------------------------------
	
	public void setASCIIList(Character[] List)
	{
		this.manList = List;
	}
	
	public Character[] getASCIIList()
	{
		return this.manList;
	}
	
	public void setImage(BufferedImage image)
	{
		this.image = image;
	}
	
	public BufferedImage getImage()
	{
		return this.image;
	}
	
	public void setASCIIArt(Character[][] ASCII_Art)
	{
		this.ASCII_Art = ASCII_Art;
	}
	
	public Character[][] getASCIIArt()
	{
		return this.ASCII_Art;
	}
	
	public void setScale(int scale)
	{
		this.scaleFactor = scale;
	}
	
	public int getScale()
	{
		return this.scaleFactor;
	}
	
	
	
	//--------------------------------------------------------method---------------------------------------------------------------------------
	
	
	/**
	 * read in input image file, return either image or null;
	 * check ImageIO.read for detail
	 * NULL will be return.
	 * @param Filename
	 * @return BufferedImage or NULL
	 * @throws IOException 
	 */
	public BufferedImage loadImage(String filename) throws IOException 
	{	
	
		BufferedImage image = ImageIO.read(new File(filename));
		
		return image;
			
	}
	
	/**
	 * read in a alternative ASCII list, to replace default ASCIIList from Converter
	 * return list will contain all character from input file.
	 * @param filename
	 * @return Character[] ASCII list
	 * @throws IOException
	 */
	public Character[] loadASCIIList(String filename) throws IOException
	{
		File in = new File(filename);
		BufferedReader rd = new BufferedReader(new FileReader(in));
		
		ArrayList<Character> temp = new ArrayList<Character>();
		
		int size = 0;
		int c;
		while((c = rd.read()) != -1)
		{
			temp.add((char)c);
			size++;
		}
		
		rd.close();
		
		//if size of list greater than color range than use default list
		if ( size > this.MAXCOLOR)	return null;
		
		Character[] list = new Character[size];
		
		for (int i = 0; i < size; i++)
			{ list[i] = temp.get(i); }
		
		
		return list;
		
	}
	
	
	 /**
	  * Convert an image to grayscale using the following formula:
	  * intensity = 0.2989 * red + 0.5870 * green + 0.1140 * blue
	  * new rgb( intensity, intensity, intensity );
	  * 
	  * @param image
	  * @return
	  */
	public BufferedImage grayifyImage(BufferedImage image) {
	
		if (image == null) return image;

		int height = image.getHeight();
		int width = image.getWidth();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color pixelColor = new Color(image.getRGB(j, i));

				double pixelRed = pixelColor.getRed() * .2989;
				double pixelGreen = pixelColor.getGreen() * .5870;
				double pixelBlue = pixelColor.getBlue() * .1140;
				int lum = (int) (pixelRed + pixelGreen + pixelBlue);
				int pixelColor1 = new Color(lum, lum, lum).getRGB();
				image.setRGB(j, i, pixelColor1);
			}
		}
		
		return image;
		
	}
	
	
	/**
	 * Convert given bufferedImage to a corresponding 2d character list.
	 * input:
	 * buffered image, precision, and asciiList
	 * 
	 * precision is a integer value range from 1 to smaller value between width and height of the image
	 * precision value determine the unit of pixel square used for conversion, 
	 * precision = 1, each pixel representing one ASCII value.
	 * precision = 10, starting from (0,0) every pixel square with width 10, representing one ASCII char
	 * the ASCII value of corresponding square is determine by the color of center pixel of that square.
	 * 
	 * asciiList is a list of characters, starting from index 0, to index length-1, characters divided grey 
	 * scale from black to white.
	 * 
	 * @param image
	 * @param percision
	 * @param ascList
	 * @return
	 */
	public Character[][] convertASC(BufferedImage image, int precision, Character[] ascList)
	{
		if(ascList == null)
		{	ascList = this.DEFAULTLIST; }
		
		int listLength = ascList.length;
		
		if (listLength > MAXCOLOR) 
			throw new IllegalArgumentException("Invalid ASCII list: Input character more than max color range.");
	
		if (precision > image.getHeight() || precision > image.getWidth())
		{	precision = (image.getHeight() < image.getWidth()) ? image.getHeight() : image.getWidth(); }
		
		if (precision == 0) return null;
		
		//width and height of character list.
		int height= image.getHeight() / precision;
		int width = image.getWidth() / precision;
		
		//2D character array that store ASCII art.
		Character[][] imgascii = new Character[height][width]; 
		
		//grey scale sub range that  corresponding to each character.
		double greyUnit = (double) MAXCOLOR / listLength;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				//center pixel
				Color pixelColor = new Color(image.getRGB((precision/2) + precision * j, (precision/2) + precision * i));
				double avgColor = (pixelColor.getBlue() +  pixelColor.getGreen() + pixelColor.getRed()) / 3;
				imgascii[i][j] = ascList[(int)(avgColor / greyUnit)];
						
			}
		}
		
		return imgascii;
		
	}
	

	
	/**
	 * save a 2d character list to a file.
	 * if either character list or input fileName is null, then no operation performed
	 * @param imgascii
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void saveASCII(Character[][] imgascii, String fileName) throws FileNotFoundException
	{
		if (imgascii == null || fileName == null) return;
		
		File out = new File(fileName); 
		
		PrintWriter output = new PrintWriter(out);
		
		for (int i = 0; i < imgascii.length; i++)
		{
			for (int j = 0; j < imgascii[i].length; j++)
			{
				output.printf("%c",imgascii[i][j]);
			}
			output.println();
		}
		
		output.close();
		
	}
	
	
}
