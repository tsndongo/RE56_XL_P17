package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class FileExport {

	public static void write(ArrayList<ArrayList <Integer>> scheduling) {
		
		File file = new File("./", "scheduling.txt"); // Writes log in directory at root of the program
		BufferedWriter writer = null;
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("Cannot create file");
			e.printStackTrace();
		}
		
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e1) {
			System.out.println("Cannot create buffered writer");
			e1.printStackTrace();
		}

		for (ArrayList<Integer> listIterator : scheduling)
		{
			try {
				writer.write(listIterator.toString()+ System.getProperty("line.separator"));
			} catch (IOException e1) {
				System.out.println("Cannot write in file");
				e1.printStackTrace();
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot close buffered writer");
			e.printStackTrace();
		}
	}
	
}
