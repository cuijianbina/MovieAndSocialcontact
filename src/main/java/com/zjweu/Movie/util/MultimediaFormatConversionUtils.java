package com.zjweu.Movie.util;
/**
 * 视频转码工具类
 * @author Administrator
 *
 */

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.VideoAttributes;

public class MultimediaFormatConversionUtils {

	// private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Description:音视频格式转换
	 *
	 * @param sourcePath
	 * @param targetPath
	 * @param audioEncoder
	 * @param videoEncoder
	 * @param targetFormat
	 * @author cui.jianbin
	 * @date 2019年4月16日 上午11:02:24
	 * @version 1.0
	 */
	public static void convertMultimediaFormat(File source, File target, String audioEncoder, String videoEncoder,
			String targetFormat) {
		try {

			// File source = new File(sourcePath);
			// File target = new File(targetPath);
			/* 设置音频属性 */
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec(audioEncoder); // 设置编码器 libmp3lame 或 flac
			audio.setBitRate(new Integer(400000)); // 设置比特率
													// 数字越大声音越接近原声，转换时间越久（亲测这个数值比较好）
			audio.setChannels(new Integer(1));// 设置声音频道
			// audio.setSamplingRate(new Integer(22050)); 设置节录率
			/* 设置视频属性 */
			VideoAttributes video = new VideoAttributes();
			video.setCodec(videoEncoder); // 设置编码器 msmpeg4v2
			video.setBitRate(new Integer(1600000)); // 数字越大画面越清晰，转换时间越久（亲测这个数值比较好）
			video.setFrameRate(new Integer(15));// 设置帧率
			// video.setSize(new VideoSize(400, 300)); 设置大小
			/* 设置编码属性 */
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat(targetFormat); // mp4 需要转换成的格式
			attrs.setAudioAttributes(audio);
			attrs.setVideoAttributes(video);
			/* 执行转码 */
			Encoder encoder = new Encoder();
			encoder.encode(source, target, attrs);
			// logger.info("转码成功！！！！！！！");
		} catch (IllegalArgumentException e) {
			// logger.error("转码失败！（IllegalArgumentException）");
			e.printStackTrace();
		} catch (InputFormatException e) {
			// logger.error("转码失败！（InputFormatException）");
			e.printStackTrace();
		} catch (EncoderException e) {
			// logger.error("转码失败！（EncoderException）");
			e.printStackTrace();
		}
	}

	public static void convertVideo(File source, File target, String videoEncoder, String targetFormat)
			throws Exception {
		VideoAttributes video = new VideoAttributes();
		video.setCodec(videoEncoder); // 设置编码器 msmpeg4v2 , libx264
		video.setBitRate(new Integer(1600000)); // 数字越大画面越清晰，转换时间越久（亲测这个数值比较好）
		video.setFrameRate(new Integer(15));// 设置帧率
		// video.setSize(new VideoSize(400, 300)); 设置大小
		/* 设置编码属性 */
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat(targetFormat); // mp4 需要转换成的格式
		attrs.setVideoAttributes(video);
		/* 执行转码 */
		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs);
		System.out.println("haola");
	}

//	public static void main(String[] args) throws Exception {
//		MultimediaFormatConversionUtils m = new MultimediaFormatConversionUtils();
//		String sourcePath = "E:\\video\\24.02_多线程(死锁问题概述和使用).avi";
//		String targetPath = "E:\\video\\155531738655790.01796004902738.mp4";
//		m.convertVideo(new File(sourcePath), new File(targetPath), "msmpeg4v2", "mp4");
//	}
}
