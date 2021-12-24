package gui.perceptron;

import core.perceptron.Perceptron;

public class Main {
	public static void main(String[] args) {
		Perceptron perceptron = new Perceptron(new Perceptron.TargetFunction() {
			@Override
			public int func(int[] pattern) {
				return pattern[0] == 1 && pattern[1] == 1 ? 1 : 0;
			}
		});

		perceptron.addPattern(new int[] { 0, 0 });
		perceptron.addPattern(new int[] { 1, 0 });
		perceptron.addPattern(new int[] { 0, 1 });
		perceptron.addPattern(new int[] { 1, 1 });

		for (double i = 0.0001; i <= 5.0; i += i > 1 ? 0.01 : 0.0001) {
			double acc = 0;
			for (int j = 0; j < 10000; j++) {
				perceptron.init(i);
				acc += perceptron.teach(1000000, true);
			}
			System.out.println(i + "\t" + acc / 10000.0);
		}
	}
}
