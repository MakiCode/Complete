package src.Card;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class EightPuzzleLoader {
	private static URL url;
	private static URL lastUrl;
	BufferedImage image;

	public EightPuzzleLoader() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			handleWindows();
		} else {
			handleMacLinux();
		}
	}

	private void handleWindows() {
		File file = new File(System.getProperty("user.home") + File.separator
				+ "EightPuzzle");
		try {
			if (file.mkdir()) {
				url = getClass().getResource("image.png");
				BufferedImage img = ImageIO.read(url);
				ImageIO.write(img, "png", new File(file.getPath()
						+ File.separator + "image.png"));
				Runtime.getRuntime().exec("attrib +H " + file.getPath());
				url = new URL("file://" + file.getPath() + File.separator + "image.png");
			} else {
				url = new URL("file://" + file.getPath() + File.separator + "image.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMacLinux() {
		File file = new File(System.getProperty("user.home") + "/.EightPuzzle");
		try {
			if (file.mkdir()) {
				url = getClass().getResource("image.png");
				BufferedImage img = ImageIO.read(url);
				ImageIO.write(img, "png", new File(file.getPath()
						+ "/image.png"));
				url = new URL("file://" + file.getPath() + "/image.png");
			} else {
				url = new URL("file://" + file.getPath() + "/image.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Image load(URL url) {
		try {
			lastUrl = url;
			return ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveImage(BufferedImage img) {
		try {
			ImageIO.write(img, "png", new File(url.getFile()));
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	public Image loadImage(URL url2) {
		return load(url2);
	}

	public URL getLastUrl() {
		return lastUrl;
	}

	public URL getDefaultURL() {
		return url;
	}
}
