package com.deep.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DataSet {

	public static void loadData(String imageFilePath, String labelFilePath) throws IOException {

		DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(imageFilePath)));
		int magicNumber = dataInputStream.readInt();
		int numberOfImages = dataInputStream.readInt();
		int imageRows = dataInputStream.readInt();
		int imageCols = dataInputStream.readInt();

		System.out.println("magic number is " + magicNumber);
		System.out.println("number of items is " + numberOfImages);
		System.out.println("number of rows is: " + imageRows);
		System.out.println("number of cols is: " + imageCols);

		DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
		int labelMagicNumber = labelInputStream.readInt();
		int numberOfLabels = labelInputStream.readInt();
		int labelRows = labelInputStream.readInt();
		int labelCols = labelInputStream.readInt();

		System.out.println("labels magic number is: " + labelMagicNumber);
		System.out.println("number of labels is: " + numberOfLabels);
		System.out.println("number of rows is: " + labelRows);
		System.out.println("number of cols is: " + labelCols);

		double[][][] images = new double[5][imageRows][imageCols];
		// double[][][] labels = new double[5][labelRows][labelCols];

		for (int i = 0; i < images.length; i++) {
			for (int r = 0; r < imageRows; r++) {
				for (int c = 0; c < imageCols; c++) {
					images[i][r][c] = dataInputStream.readUnsignedByte();
				}
			}
		}

		// for (int i = 0; i < labels.length; i++) {
		// for (int r = 0; r < labelRows; r++) {
		// for (int c = 0; c < labelCols; c++) {
		// labels[i][r][c] = labelInputStream.readUnsignedByte();
		// }
		// }
		// }

		dataInputStream.close();
		// labelInputStream.close();
	}

}