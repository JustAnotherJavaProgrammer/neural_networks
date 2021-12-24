package core.hopfield;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;


public class ImagePatternDirectory implements Iterator<Pattern> {
//	private final File dir;
	private final File[] files;
	private int i = 0;
	public ImagePatternDirectory(File dir) {
		if(!dir.isDirectory())
			throw new IllegalArgumentException("ImagePatternDirectory can only be constructed with a directory");
//		this.dir = dir;
		files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory())
				return false;
				// Might change in the future, do not refactor to one-liner.
				return true;
			}
		});
	}

	@Override
	public boolean hasNext() {
		return i < files.length;
	}

	@Override
	public ImagePattern next() {
		try {
			return new ImagePattern(ImageIO.read(files[i]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
