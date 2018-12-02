package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import org.opencv.aruco.Aruco;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class Controller {
	MarkerDetector markerDetector = null;
	AnimationTimer imageAnimation = null;

	@FXML
	ImageView imageView;

	@FXML
	public void initialize() {
		markerDetector = new MarkerDetector(Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50));

		imageAnimation = new AnimationTimer() {
			@Override
			public void handle(long now) {
				imageView.setImage(markerDetector.getLastValue());
			}
		};

	}

	@FXML
	public void onStartButton() {
		Main.printDebug("startButton");
		imageAnimation.start();
		if (!markerDetector.isRunning()) markerDetector.start();
		// markerDetector.start();
	}

	@FXML
	public void onStopButton() {
		Main.printDebug("stopButton");
		imageAnimation.stop();
		markerDetector.cancel();

		imageView.setImage(null);
	}

	@FXML
	public void onPauseButton() {
		Main.printDebug("pauseButton");
		imageAnimation.stop();
	}
}
