package core.hopfield;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Hopfield implements Pattern {
	final int[][] weights;
	private byte[] values;

	public Hopfield(int size) {
//		System.out.println(size);
//		long acc = 0;
//		for(int i = 0; i < size-1; i++) {
//			acc+=size-(i+1);
//		}
//		System.out.println(acc);
//		weights = new int[Integer.MAX_VALUE];
		this(new int[size][size]);
	}

	public Hopfield(int[][] weights) {
		System.out.println("Stop right there, criminal scum!");
		this.weights = weights;
		values = new byte[weights.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = (byte) (((int) (Math.random() * 2)) == 0 ? -1 : 1);
		}
	}

	public void setInput(Pattern p) {
		long size = p.size();
		for (int i = 0; i < values.length; i++) {
			if (i >= size) {
				values[i] = -1;
				continue;
			}
			values[i] = (byte) p.getValueAt(i);
		}
	}

	/**
	 * Synchronous activation<br>
	 * All values are updated
	 */
	public void activateSync() {
		byte[] newVals = new byte[values.length];
		for (int j = 0; j < newVals.length; j++) {
			newVals[j] = (byte) calcNext(j);
		}
	}

	/**
	 * Asynchronous activation<br>
	 * One random value is updated per call
	 */
	public void activateAsync() {
		int n = (int) (Math.random() * values.length);
		values[n] = (byte) calcNext(n);
	}

	private int calcNext(int j) {
		int oldVal = values[j];
		long sum = 0;
		for (int i = 0; i < weights.length; i++) {
			sum += oldVal * weights[i][j];
		}
		return sum < 0 ? -1 : 1;
	}

	public void updateWeights(Iterator<Pattern> patterns, boolean discardOldWeights) {
		if (discardOldWeights) {
			// reset all weights
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[i].length; j++) {
					weights[i][j] = 0;
				}
			}
		}
		System.out.println("Updating weights...");
		int counter = 0;
		while (patterns.hasNext()) {
			Pattern p = patterns.next();
			IntStream.range(0, weights.length).parallel().forEach(i -> {
				for (int j = 0; j < weights[i].length; j++) {
					if (j <= i) // weight has already been set
						continue;
					int val = p.getValueAt(i) * p.getValueAt(j);
					weights[i][j] += val;
					weights[j][i] += val;
				}
			});
			counter++;
			System.out.println(counter + " pattern" + (counter == 1 ? "" : "s") + " processed...");
		}
	}

	@Override
	public int getValueAt(long pos) {
		return values[(int) pos];
	}

	@Override
	public long size() {
		return values.length;
	}

	public void outputToStream(OutputStream out) throws IOException {
		DataOutputStream dout = new DataOutputStream(out);
		dout.writeInt(weights.length);
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				dout.writeInt(weights[i][j]);
			}
		}
		dout.flush();
		dout.close();
	}

	public void readWeightsFromStream(InputStream in) throws IOException {
		DataInputStream din = new DataInputStream(in);
		din.readInt();
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = din.readInt();
			}
		}
		din.close();
	}

}
