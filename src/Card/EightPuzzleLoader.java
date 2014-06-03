package src.Card;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class EightPuzzleLoader {
	private URL url;
	private static URL lastUrl;
	private URL formatURL;
	BufferedImage image;
	private File file;
	private static URL metaDataURL;
	private static boolean showWarning;
	private boolean showOpeningMessage;
	private static URL highScoreURL;
	private int highscore1;
	private int highscore2;
	private int highscore3;

	// TODO: Change metadata to use properties
	// TODO: add javadoc comments 
	public EightPuzzleLoader() {
		file = new File(System.getProperty("user.home") + File.separator
				+ ".EightPuzzle");
		try {
			if (file.mkdir()) {
				makeImageFile();

				makeFormatFile();

				makeMetaFile();

				makeHighScoreFile();
			} else {

				// Read metaData. If it doesn't exist make it
				metaDataURL = new URL("file:///" + file.getPath()
						+ File.separator + "metadata.txt");
				if (!new File(metaDataURL.getFile()).exists()) {
					makeMetaFile();
				} else {
					readMeta();
				}

				formatURL = new URL("file:///" + file.getPath()
						+ File.separator + "imageFormat.txt");

				if (!new File(formatURL.getFile()).exists()) {
					makeFormatFile();
					url = new URL("file:///" + file.getPath() + File.separator
							+ "image.png");
				} else {
					readFormatFile();
				}

				if (!new File(url.getFile()).exists()) {
					makeImageFile();
				}

				highScoreURL = new URL("file:///" + file.getPath()
						+ File.separator + "highScores.txt");

				if (!new File(highScoreURL.getFile()).exists()) {
					makeHighScoreFile();
				} else {
					readHighScoreFile();
			 	}
				lastUrl = url;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readMeta() throws FileNotFoundException {
		Scanner metaScanner = new Scanner(new File(metaDataURL.getFile()));
		showOpeningMessage = metaScanner.nextBoolean();
		showWarning = metaScanner.nextBoolean();
		metaScanner.close();
	}

	private void readFormatFile() throws FileNotFoundException,
			MalformedURLException, IOException {
		BufferedReader formatReader = new BufferedReader(new FileReader(
				new File(formatURL.getFile())));

		url = new URL("file:///" + file.getPath() + File.separator + "image."
				+ formatReader.readLine());

		formatReader.close();
	}

	private void makeImageFile() throws IOException {
		url = getClass().getResource("image.png");
		BufferedImage img = ImageIO.read(url);
		String path = file.getPath() + File.separator + "image.png";

		ImageIO.write(img, "png", new File(path));
		url = new URL("file:///" + file.getPath() + File.separator
				+ "image.png");
	}

	private void makeFormatFile() throws IOException {
		formatURL = new URL("file:///" + file.getPath() + File.separator
				+ "imageFormat.txt");
		BufferedWriter formatWriter = new BufferedWriter(new FileWriter(
				formatURL.getFile()));
		formatWriter.write("png");
		formatWriter.close();
	}

	private void makeMetaFile() throws IOException {
		metaDataURL = new URL("file:///" + file.getPath() + File.separator
				+ "metadata.txt");
		BufferedWriter metaWriter = new BufferedWriter(new FileWriter(
				metaDataURL.getFile()));
		metaWriter.write("true true");
		metaWriter.close();
		showOpeningMessage = true;
		showWarning = true;
	}

	private void makeHighScoreFile() throws IOException {
		highScoreURL = new URL("file:///" + file.getPath() + File.separator
				+ "highScores.txt");
		PrintWriter printWriter = new PrintWriter(highScoreURL.getFile());
		printWriter.write("1:" + encode("0") + "\n2:" + encode("0") + "\n3:"
				+ encode("0"));
		printWriter.close();
		highscore1 = 0;
		highscore2 = 0;
		highscore3 = 0;
	}

	private void readHighScoreFile() throws IOException {
		highScoreURL = new URL("file:///" + file.getPath() + File.separator
				+ "highScores.txt");
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(highScoreURL.getFile())));
		highscore1 = Integer.parseInt(decode(properties.getProperty("1")));
		highscore2 = Integer.parseInt(decode(properties.getProperty("2")));
		highscore3 = Integer.parseInt(decode(properties.getProperty("3")));
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

	public boolean saveImageLastImg() {
		try {
			new File(url.getFile()).delete();

			url = new URL("file:///" + file.getPath() + File.separator
					+ "image." + getLastFormatName());

			ImageIO.write((RenderedImage) load(getLastUrl()),
					getLastFormatName(), new File(url.getFile()));

			PrintWriter writer = new PrintWriter(new File(formatURL.getFile()));
			writer.print(getLastFormatName());
			writer.close();

			return true;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * Returns a 4 character type name (.png, .jpg etc.)
	 * 
	 * @return
	 */
	private static String getLastFormatName() {
		return lastUrl.toString().substring(lastUrl.toString().length() - 3);
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

	public boolean showOpeningMessage() {
		return showOpeningMessage;
	}

	public boolean showWarning() {
		return showWarning;
	}

	public static void writeMetaData(boolean showScrambleMessage,
			boolean showOpeningMessage) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(metaDataURL.getFile()));
			writer.write(showOpeningMessage + " ");
			writer.write(showScrambleMessage + " ");
			writer.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	public static void writeHighScores(int hs1, int hs2, int hs3) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(highScoreURL.getFile()));
			writer.write("1:" + encode("" + hs1) + "\n");
			writer.write("2:" + encode("" + hs2) + "\n");
			writer.write("3:" + encode("" + hs3) + "\n");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getHighscore1() {
		return highscore1;
	}

	public int getHighscore2() {
		return highscore2;
	}

	public int getHighscore3() {
		return highscore3;
	}

	// This is minor encoding. All the people likely to use this will probably
	// not be able to hack it
	private static String encode(String str) {
		return DatatypeConverter.printBase64Binary(str.getBytes());

	}

	private static String decode(String str) {
		return new String(DatatypeConverter.parseBase64Binary(str));
	}
}
