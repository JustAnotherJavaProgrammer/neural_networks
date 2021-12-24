package gui.hopfield;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JFileChooser;

import core.hopfield.Hopfield;
import core.hopfield.ImagePatternDirectory;

public class Main {

	public static void main(String[] args) {
		Hopfield hopfield = new Hopfield(45*55*24);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		chooser.setFileFilter(new FileFilter() {
//			
//			@Override
//			public String getDescription() {
//				return "All directories";
//			}
//			
//			@Override
//			public boolean accept(File f) {
//				return f.isDirectory();
//			}
//		});
		chooser.setAcceptAllFileFilterUsed(false);
		int result = chooser.showDialog(null, "Choose image directory...");
		if(result != JFileChooser.APPROVE_OPTION) {
			System.out.println("Please choose a directory");
			System.exit(0);
		}
		
		hopfield.updateWeights(new ImagePatternDirectory(chooser.getSelectedFile()), true);
		try {
			hopfield.outputToStream(Files.newOutputStream(Paths.get("\\net.mod"), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
