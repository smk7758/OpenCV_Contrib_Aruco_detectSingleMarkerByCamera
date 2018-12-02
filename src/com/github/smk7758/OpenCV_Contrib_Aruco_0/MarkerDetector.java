package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

public class MarkerDetector extends ScheduledService<Image> {
	final Dictionary dictionary;

	VideoCapture vc = new VideoCapture();
	int cameraNumber = 0;

	public MarkerDetector(Dictionary dictionary) {
		this.dictionary = dictionary;
		vc.open(cameraNumber);

		this.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				vc.release();
			}
		});
	}

	@Override
	protected Task<Image> createTask() {
		return new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				if (!vc.isOpened()) {
					System.err.println("VC is not opened.");
					this.cancel();
				}

				Mat inputImage = new Mat();

				vc.read(inputImage);

				List<Mat> corners = new ArrayList<>();
				Mat markerIds = new Mat();
				// DetectorParameters parameters = DetectorParameters.create();
				Aruco.detectMarkers(inputImage, dictionary, corners, markerIds);

				Aruco.drawDetectedMarkers(inputImage, corners, markerIds);

				return convertMatToImage(inputImage);
			}
		};
	}

	private Image convertMatToImage(Mat inputImage) {
		MatOfByte byte_mat = new MatOfByte();
		Imgcodecs.imencode(".bmp", inputImage, byte_mat);

		return new Image(new ByteArrayInputStream(byte_mat.toArray()));
	}
}
