package com.framework.utils;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

public class VideoRecorderUtils extends ScreenRecorder {

    private static ScreenRecorder screenRecorder;
    private String name;

    public VideoRecorderUtils(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
            Format screenFormat, Format mouseFormat, Format audioFormat, File folder, String name)
            throws Exception {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, folder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        }
        return new File(movieFolder, name + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String methodName) {
        try {
            File file = new File(System.getProperty("user.dir") + "/target/test-recordings/");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureArea = new Rectangle(0, 0, screenSize.width, screenSize.height);

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();

            screenRecorder = new VideoRecorderUtils(gc, captureArea,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                            Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, file, methodName);

            screenRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File stopRecording() {
        try {
            if (screenRecorder != null) {
                screenRecorder.stop();
                return screenRecorder.getCreatedMovieFiles().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}