package com.deep.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageKit {

	static String IMAGE_URL = "E:/";

	public static void main(String[] args) throws IOException {
		// imageCuvCut("D:/chengdongliang.jpg", "D:/cut", 5, 5);
		// binaryImage("D:/chengdongliang.jpg","D:/chengdongliang_1.jpg");
		// imageToBinary("D:/chengdongliang.jpg");
		// lineCut("D:/chengdongliang.jpg", "D:/cut", 5, 5);
		img2slice("E:/chengdongliang.jpg", "E:/imgs", 32, 32);
		// rgb2Img(getInput2D("D:/chengdongliang.jpg"));
		// getInput("D:/chengdongliang.jpg");
		// getInput("D:/cut/45_77.jpg");
	}

	public static double[][][][] getInput() {
		String[] TEST_SET_32 = Config.TEST_SET_32;
		double[][][][] input = new double[TEST_SET_32.length][][][];
		for (int i = 0; i < TEST_SET_32.length; i++) {
			input[i] = img2rgb(IMAGE_URL + "imgs/" + TEST_SET_32[i] + ".jpg");
		}
		return input;
	}

	public static double[][][] getLineInput() {
		String[] TEST_SET_32 = Config.TEST_SET_32;
		double[][][] input = new double[TEST_SET_32.length][][];
		for (int i = 0; i < TEST_SET_32.length; i++) {
			input[i] = img2lines(IMAGE_URL + "imgs/" + TEST_SET_32[i] + ".jpg");
		}
		return input;
	}

	public static double[] img2line(String src) {
		try {
			src = IMAGE_URL + src;
			BufferedImage image = ImageIO.read(new File(src));
			int width = image.getWidth();
			int height = image.getHeight();
			double[] M = new double[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					double a = (pixel & 0xff000000) >> 24;// 屏蔽低位，并移位到最低位
					double r = (pixel & 0xff0000) >> 16;
					double g = (pixel & 0xff00) >> 8;
					double b = (pixel & 0xff);
					M[y * width + x] = (r + g + b) / (3 * 255);
				}
			}
			return M;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static double[][] img2lines(String src) {
		try {
			BufferedImage image = ImageIO.read(new File(src));
			int width = image.getWidth();
			int height = image.getHeight();
			double[][] M = new double[width * height][1];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					double a = (pixel & 0xff000000) >> 24;// 屏蔽低位，并移位到最低位
					double r = (pixel & 0xff0000) >> 16;
					double g = (pixel & 0xff00) >> 8;
					double b = (pixel & 0xff);
					M[y * width + x][0] = (r + g + b) / (3 * 255);
				}
			}
			return M;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static double[][] img2linergb(String src) {
		try {
			BufferedImage image = ImageIO.read(new File(src));
			int width = image.getWidth();
			int height = image.getHeight();
			double[] R = new double[width * height];
			double[] G = new double[width * height];
			double[] B = new double[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					double a = (pixel & 0xff000000) >> 24;// 屏蔽低位，并移位到最低位
					double r = (pixel & 0xff0000) >> 16;
					double g = (pixel & 0xff00) >> 8;
					double b = (pixel & 0xff);
					R[y * width + x] = (r / 255);
					G[y * width + x] = (g / 255);
					B[y * width + x] = (b / 255);
				}
			}
			return new double[][] { R, G, B };
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static void rgb2img(double[][][] input) {
		try {
			String dir = "D:/cnn.test.img/";
			int width = input[0][0].length;
			int height = input[0].length;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			File file = new File(dir);
			if (file.exists())
				file.delete();
			file.mkdirs();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int r = ((int) (input[0][y][x] * 255)) << 16;
					int g = ((int) (input[1][y][x] * 255)) << 8;
					int b = ((int) (input[2][y][x] * 255));
					image.setRGB(x, y, r + g + b);
				}
			}
			ImageIO.write(image, "JPEG", new File(dir + System.currentTimeMillis() + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void mrgb2img(double[][] input, String name) {
		try {
			String dir = "D:/cnn.test.img/";
			int width = input[0].length;
			int height = input.length;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			File file = new File(dir);
			if (file.exists())
				file.delete();
			file.mkdirs();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int r = ((int) (input[y][x] * 255)) << 16;
					int g = ((int) (input[y][x] * 255)) << 8;
					int b = ((int) (input[y][x] * 255));
					image.setRGB(x, y, r + g + b);
				}
			}
			ImageIO.write(image, "JPEG", new File(dir + name));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static double[][][] img2rgb(String src) {
		try {
			BufferedImage image = ImageIO.read(new File(src));
			int width = image.getWidth();
			int height = image.getHeight();
			double[][] R = new double[height][width];
			double[][] G = new double[height][width];
			double[][] B = new double[height][width];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					double r = (pixel & 0xff0000) >> 16;
					double g = (pixel & 0xff00) >> 8;
					double b = (pixel & 0xff);
					R[y][x] = r / 255;
					G[y][x] = g / 255;
					B[y][x] = b / 255;
				}
			}
			return new double[][][] { R, G, B };
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static double[] img2line(BufferedImage image) {
		try {
			int width = image.getWidth();
			int height = image.getHeight();
			double[] array = new double[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					double a = (pixel & 0xff000000) >> 24;// 屏蔽低位，并移位到最低位
					double r = (pixel & 0xff0000) >> 16;
					double g = (pixel & 0xff00) >> 8;
					double b = (pixel & 0xff);
					array[y * width + x] = (r + g + b) / (3 * 255);
				}
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static double[][][] img2line(String source, int width, int height) {
		try {
			String dir = IMAGE_URL;
			BufferedImage buffer = ImageIO.read(new File(source));
			int cols = buffer.getWidth() - width + 1;
			int rows = buffer.getHeight() - height + 1;
			BufferedImage image = null;
			double[][][] images = new double[rows][cols][width * height];
			File file = new File(dir);
			if (file.exists())
				file.delete();
			file.mkdirs();
			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					image = buffer.getSubimage(x, y, width, height);
					file = new File(dir + y + "_" + x + ".jpg");
					ImageIO.write(image, "JPEG", file);
					images[y][x] = img2line(ImageIO.read(file));
				}
			}
			return images;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void img2slice(String source, String target, int width, int height) {
		try {
			BufferedImage bi = ImageIO.read(new File(source));
			double cols = bi.getWidth() - width + 1;
			double rows = bi.getHeight() - height + 1;
			File file = new File(target);
			BufferedImage image = null;
			if (file.exists())
				file.delete();
			file.mkdirs();
			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					image = bi.getSubimage(x, y, width, height);
					file = new File(target + "/" + y + "_" + x + ".jpg");
					ImageIO.write(image, "JPEG", file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copy(String fileName, String target) {
		try {
			fileName = IMAGE_URL + fileName;
			File file = new File(fileName);
			BufferedImage image = ImageIO.read(file);
			File outFile = new File(target);
			if (!outFile.exists())
				outFile.mkdirs();
			ImageIO.write(image, "JPEG", new File(target + file.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}