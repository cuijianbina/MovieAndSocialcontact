package com.zjweu.Movie.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.util.CMyString;
import com.zjweu.Movie.util.MultimediaFormatConversionUtils;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
import net.sf.json.JSONObject;

@CrossOrigin
@RestController
@RequestMapping("/upload")
public class FileUploadController {

	@RequestMapping("/hello")
	public Object hello() {
		return "hello";
	}

	/**
	 * Description:单个文件上传
	 *
	 * @param filename
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午10:47:18
	 * @version 1.0
	 */
	@RequestMapping("/fileUpload")
	public Object fileUpload(@RequestParam(value = "file") MultipartFile filename) throws Exception {
		String fileName = filename.getOriginalFilename();
		int suffixIndex = fileName.lastIndexOf(".");
		String suffix = fileName.substring(suffixIndex + 1);
		long size = filename.getSize();
		long maxImg = 20 * 1024 * 1024;// 20MB
		String coverImg = null;
		if (suffix.equals("jpg") || suffix.equals("png")) {
			if (size > maxImg) {
				return CtlStatus.failed("图片最大不能超过20MB!");
			} else {
				String name = new Date().getTime() + Math.random() * 100 + "." + suffix; // 时间戳+随机数
																							// 保证图片唯一
				coverImg = "D:/Documents/HBuilderProject/毕设前端/images/" + name;// 保存的路径
				String adminImg = "C:/Users/Administrator/Desktop/毕设/layuiCMS-master/images/" + name;
				filename.transferTo(new File(coverImg));
				FileInputStream fis = new FileInputStream(coverImg);
				// 打开输出流
				FileOutputStream fos = new FileOutputStream(adminImg);
				// 读取和写入信息
				int len = 0;
				// 创建一个字节数组，当做缓冲区
				byte[] b = new byte[1024];
				while ((len = fis.read(b)) != -1) {
					fos.write(b);
				}
				// 关闭流 先开后关 后开先关
				fos.close(); // 后开先关
				fis.close(); // 先开后关
			}
		} else {
			return CtlStatus.failed("未上传图片或图片格式错误");
		}
		return CtlStatus.success().put("coverImg", coverImg);
	}

	/**
	 * Description:上传多个文件
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月15日 下午4:44:03
	 * @version 1.0
	 */
	@RequestMapping("/many")
	public Object many(HttpServletRequest request) throws Exception {
		String coverImg = null;// 封面图路径
		String moviePath = null;// 电影播放路径
		String playTime = null; // 播放时长
		int searchTime = 0;
		String mp4Path = null;// 将其他格式的视频转化为mp4后的路径
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			String fileName = file.getOriginalFilename();
			int suffixIndex = fileName.lastIndexOf(".");
			String suffix = fileName.substring(suffixIndex + 1);
			long size = file.getSize();
			long maxImg = 20 * 1024 * 1024;// 20MB
			long maxVideo = 970 * 1024 * 1024; // 970MB
			if (i == 0) {
				if (suffix.equals("jpg") || suffix.equals("png")) {
					if (size > maxImg) {
						return CtlStatus.failed("图片最大不能超过20MB!");
					} else {
						coverImg = "D:/Documents/HBuilderProject/毕设前端/images/" + new Date().getTime()
								+ Math.random() * 100 + "." + suffix;// 保存的路径
						file.transferTo(new File(coverImg));
					}
				} else {
					return CtlStatus.failed("未上传图片或图片格式错误");
				}
			}
			if (i == 1) {
				if (suffix.equals("mp4") || suffix.equals("avi")) {
					if (size > maxVideo) {
						return CtlStatus.failed("视频最大不能超过970MB！");
					} else {
						String name = "D:/Documents/HBuilderProject/毕设前端/video/" + new Date().getTime()
								+ Math.random() * 100 + ".";
						// String name = "e:/video/" + new Date().getTime() +
						// Math.random() * 100 + ".";
						moviePath = name + suffix;
						File source = new File(moviePath);
						file.transferTo(source);
						Encoder encoder = new Encoder();
						long ls;
						if (suffix.equals("mp4")) {// 当格式为mp4时不做转码处理
							ls = encoder.getInfo(source).getDuration() / 1000; // 获取播放时长
							mp4Path = moviePath;
						} else {
							mp4Path = name + "mp4";
							File target = new File(mp4Path);
							MultimediaFormatConversionUtils.convertMultimediaFormat(source, target, "libmp3lame",
									"msmpeg4v2", "mp4");
							source.delete();
							ls = encoder.getInfo(target).getDuration() / 1000; // 获取播放时长
						}
						int hour = (int) (ls / 3600);
						int minute = (int) (ls % 3600) / 60;
						int second = (int) (ls - hour * 3600 - minute * 60);
						playTime = hour + "时" + minute + "分" + second + "秒";
						searchTime = hour * 60 + minute;
					}
				} else {
					return CtlStatus.failed("未上传视频或视频格式错误");
				}
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (!CMyString.isEmpty(coverImg) && !CMyString.isEmpty(coverImg) && !CMyString.isEmpty(coverImg)) {
			map.put("coverImg", coverImg);
			map.put("moviePath", mp4Path);
			map.put("playTime", playTime);
			map.put("searchTime", searchTime);
		} else {
			return CtlStatus.failed("路径存储出现问题，请重试");
		}
		return CtlStatus.success(map);
	}

	@RequestMapping("/manyFile")
	public Object manyFile(@RequestParam(value = "imgfile") MultipartFile imgFile,
			@RequestParam(value = "videofile") MultipartFile videofile) throws Exception {
		String coverImg = null;// 封面图路径
		String moviePath = null;// 电影播放路径
		String playTime = null; // 播放时长
		int searchTime = 0;
		String mp4Path = null;// 将其他格式的视频转化为mp4后的路径
		List<MultipartFile> files = new LinkedList<>();
		files.add(imgFile);
		files.add(videofile);
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			String fileName = file.getOriginalFilename();
			int suffixIndex = fileName.lastIndexOf(".");
			String suffix = fileName.substring(suffixIndex + 1);
			long size = file.getSize();
			long maxImg = 20 * 1024 * 1024;// 20MB
			long maxVideo = 970 * 1024 * 1024; // 970MB
			if (i == 0) {
				if (suffix.equals("jpg") || suffix.equals("png")) {
					if (size > maxImg) {
						return CtlStatus.failed("图片最大不能超过20MB!");
					} else {
						coverImg = "D:/Documents/HBuilderProject/毕设前端/images/" + new Date().getTime()
								+ Math.random() * 100 + "." + suffix;// 保存的路径
						file.transferTo(new File(coverImg));
					}
				} else {
					return CtlStatus.failed("未上传图片或图片格式错误");
				}
			}
			if (i == 1) {
				if (suffix.equals("mp4")) {// || suffix.equals("avi")
					if (size > maxVideo) {
						return CtlStatus.failed("视频最大不能超过970MB！");
					} else {
						String name = "D:/Documents/HBuilderProject/毕设前端/video/" + new Date().getTime()
								+ Math.random() * 100 + ".";
						moviePath = name + suffix;
						File source = new File(moviePath);
						file.transferTo(source);
						Encoder encoder = new Encoder();
						long ls;
						if (suffix.equals("mp4")) {// 当格式为mp4时不做转码处理
							ls = encoder.getInfo(source).getDuration() / 1000; // 获取播放时长
							mp4Path = moviePath;
						} else {
							mp4Path = name + "mp4";
							File target = new File(mp4Path);
							MultimediaFormatConversionUtils.convertMultimediaFormat(source, target, "libmp3lame",
									"msmpeg4v2", "mp4");
							source.delete();
							ls = encoder.getInfo(target).getDuration() / 1000; // 获取播放时长
						}
						int hour = (int) (ls / 3600);
						int minute = (int) (ls % 3600) / 60;
						int second = (int) (ls - hour * 3600 - minute * 60);
						playTime = hour + "时" + minute + "分" + second + "秒";
						searchTime = hour * 60 + minute;
					}
				} else {
					return CtlStatus.failed("未上传视频或视频格式错误");
				}
			}
		}
		Map<String, Object> map = new HashMap<>();
		if (!CMyString.isEmpty(coverImg) && !CMyString.isEmpty(coverImg) && !CMyString.isEmpty(coverImg)) {
			map.put("coverImg", coverImg);
			map.put("moviePath", mp4Path);
			map.put("playTime", playTime);
			map.put("searchTime", searchTime);
		} else {
			return CtlStatus.failed("路径存储出现问题，请重试");
		}
		return CtlStatus.success(map);
	}

}
