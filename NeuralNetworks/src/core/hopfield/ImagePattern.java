package core.hopfield;

import java.awt.image.BufferedImage;

public class ImagePattern implements Pattern {
	private final BufferedImage img;

	public ImagePattern(BufferedImage img) {
		this.img = img;
	}

	@Override
	public int getValueAt(long pos) {
		long pix = pos / 24;
		return getBit(img.getRGB((int) (pix % img.getWidth()), (int) (pix / img.getWidth())), (int) (pos % 24)) <= 0
				? -1
				: 1;
	}

	@Override
	public long size() {
		return ((long) img.getWidth()) * img.getHeight() * 24;
	}

	private int getBit(int val, int bitNo) {
		return (val >> bitNo) & 1;
	}

	public BufferedImage getImage() {
		return img;
	}

	public int getWidth() {
		return img.getWidth();
	}

	public static ImagePattern fromPattern(Pattern p, int width) {
		long size = p.size();
		long pixCount = size / 24;
		int height = (int) Math.ceil((double) (pixCount) / width);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		outerLoop: for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixNo = y * width + x;
				if (pixNo > pixCount)
					break outerLoop;
				int rgb = 0;
				for (int i = 0; i < 24; i++) {
					long id = (long) pixNo * 24 + i;
					if (id > size) {
						rgb = rgb << 1;
						continue;
					}
					rgb = (rgb << 1) | (p.getValueAt(id) < 0 ? 0 : 1);
				}
				img.setRGB(x, y, rgb);
			}
		}
		return new ImagePattern(img);
	}

}
