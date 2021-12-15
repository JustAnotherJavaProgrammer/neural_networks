package core.perceptron;

import java.util.ArrayList;

public class Perceptron {
	private double learningRate = 0.0;
	ArrayList<int[]> patterns = new ArrayList<>();
	double[][] weights = new double[4][4];
	private final TargetFunction func;

	public Perceptron(TargetFunction func) {
		this.func = func;
	}

	public void init(double learningRate) {
		this.learningRate = learningRate;
		for (int i = 0; i < 3; i++) {
			weights[i][3] = Math.random() - 0.5;
		}
	}

	public void addPattern(int[] pattern) {
		patterns.add(pattern);
	}

	public int teach(int iterations, boolean stopWhenPerfect) {
		if(stopWhenPerfect && evaluate() == patterns.size()) {
			return 0;
		}
		for (int i = 0; i < iterations; i++) {
			int patternIndex = (int) (Math.random() * patterns.size());
			int[] pattern = patterns.get(patternIndex);
			int output = run(pattern);
			int correctOutput = func.func(pattern);
			weights[0][3] = calcNewWeight(weights[0][3], pattern[0], output, correctOutput);
			weights[1][3] = calcNewWeight(weights[1][3], pattern[1], output, correctOutput);
			weights[2][3] = calcNewWeight(weights[2][3], 1, output, correctOutput);
			if(stopWhenPerfect && evaluate() == patterns.size()) {
				return i+1;
			}
		}
		return iterations;
	}

	private double calcNewWeight(double oldWeight, int input, int output, int correctOutput) {
		if (output == correctOutput)
			return oldWeight;
		if (output < correctOutput) {
//			System.out.println("Increment");
			return oldWeight + input * learningRate;
		}
//		System.out.println("Decrement");
		return oldWeight - input * learningRate;
	}

	public int run(int[] pattern) {
		int[] net = new int[3];
		for (int i = 0; i < pattern.length; i++) {
			net[i] = pattern[i];
		}
		net[2] = 1;
		double sum = 0;
		for (int i = 0; i < net.length; i++) {
			sum += ((double) net[i]) * weights[i][3];
		}
		if (sum > 0)
			return 1;
		return 0;
	}

	public boolean debugPrint(int[] pattern) {
		int result = run(pattern);
		System.out.print("[ ");
		for (int i = 0; i < pattern.length; i++) {
			System.out.print(pattern[i]);
			System.out.print(", ");
		}
		System.out.print(1);
		System.out.print(" ] => ");
		System.out.print(result);
		System.out.print("\t");
		int correctVal = func.func(pattern);
		System.out.println(result == correctVal);
		return result == correctVal;
	}

	public void debugAll(boolean showWeights) {
		int correct = 0;
		for (int[] pattern : patterns) {
			correct += debugPrint(pattern) ? 1 : 0;
		}
		System.out.println(
				correct + "/" + patterns.size() + " correct (" + (correct / ((double) patterns.size()) * 100) + "%)");
		System.out.println();
		if (showWeights) {
			System.out.print("Weights:");
			for (int i = 0; i < weights.length; i++) {
				System.out.println();
				for (int j = 0; j < weights[i].length; j++) {
					System.out.print(weights[i][j]);
					if (j < weights[i].length - 1)
						System.out.print('\t');
				}
			}
			System.out.println();
			System.out.println("---");
		}
	}
	
	public int evaluate() {
		int correct = 0;
		for (int[] pattern : patterns) {
			correct += run(pattern) == func.func(pattern) ? 1:0;
		}
		return correct;
	}

	public interface TargetFunction {
		public int func(int[] pattern);
	}
}