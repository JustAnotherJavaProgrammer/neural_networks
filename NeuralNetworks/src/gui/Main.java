package gui;

import core.perceptron.Perceptron;

public class Main {
	public static void main(String[] args) {
		Perceptron perceptron = new Perceptron(new Perceptron.TargetFunction() {
			@Override
			public int func(int[] pattern) {
				return pattern[0] == 1 && pattern[1] == 1 ? 1 : 0;
			}
		});

		perceptron.init(0.001);

		perceptron.addPattern(new int[] { 0, 0 });
		perceptron.addPattern(new int[] { 1, 0 });
		perceptron.addPattern(new int[] { 0, 1 });
		perceptron.addPattern(new int[] { 1, 1 });
		
		perceptron.debugAll(false);
		
		perceptron.teach(10000);
		
		perceptron.debugAll(false);
	}
}
