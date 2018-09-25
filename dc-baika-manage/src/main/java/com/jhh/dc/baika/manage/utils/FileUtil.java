package com.jhh.dc.baika.manage.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.*;

/**
 * 文件操作类
 * @author conly.wang 2015-11-04
 */
public class FileUtil {
	

	private static final transient Log log = LogFactory.getLog(FileUtil.class);
	
	public static String safeWindowsFileName(String fileName) {
		if (null != fileName) {
			return fileName.replaceAll("[\\/:*?\"<>|]", "");
		}
		return null;
	}

	public static String readClassPathResourceToString(Class<?> classPackageAsResourcePath, String fileName) {
		String path = ClassUtils.classPackageAsResourcePath(classPackageAsResourcePath) + '/' + fileName;
		return readClassPathResourceToString(path);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String readClassPathResourceToString(String path) {

		ClassPathResource classPathResource = null;
		InputStream inputStream = null;

		try {
			classPathResource = new ClassPathResource(path);
			inputStream = classPathResource.getInputStream();
			// 读取不了文件中的中文内容，所以修改 begin
			// return IOUtils.readStringFromStream(inputStream);
			return IoUtil.readStringFromStream(inputStream);
			// 读取不了文件中的中文内容，所以修改 end
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e, e);
				}
				inputStream = null;
			}
			if (null != classPathResource) {
				classPathResource.exists();
			}
		}
		return null;
	}

	public static void removeAllFilesUnderDirectory(String dir) {
		File fileDir = new File(dir);
		if (fileDir.exists() && fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
		}
	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}
	/**
	 * 删除N天以前的数据
	 * @param fileName
	 */
	public static void deleteNDayAgoFile(String fileName,int n) {
		File file = new File(fileName);
		int days = (int) ((Calendar.getInstance().getTimeInMillis()-file.lastModified())/1000/60/60/24);
		if(days > n){
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
	}

	public static ByteArrayOutputStream getByteArrayOutputStream(String filePath) {
		File file = new File(filePath);
		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			out.write(buffer);
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}					
			} catch (Exception e2) {
				log.error(e2, e2);
			}
		}
		return out;
	}

	public static String getChineseFileName(String fileName) {
		String fileEncoding = System.getProperty("file.encoding");
		fileEncoding = fileEncoding != null ? fileEncoding.toUpperCase() : null;
		if (EncodingUtil.ENCODING_UTF8.equals(fileEncoding)) {
			return fileName; // UTF-8，直接返回
		}
		if (EncodingUtil.isWindowsPlatform()) {
			return fileName; // Windows，直接返回
		}
		try {
			if (EncodingUtil.isSimplfiedChineseEncoding(fileEncoding)) {
				// Unix/Linux，并且是中文字符集，直接返回
				return fileName;
			} else {
				// Unix/Linux，并且不是中文字符集，需要转换
				// 参考 http://www.iteye.com/problems/80104
				// Unix上如果系统的file.encoding=UTF-8时，需要在JVM里设置-Dfile.encoding=ISO8859-1，否则还是有乱码问题
				return new String(fileName.getBytes(EncodingUtil.ENCODING_GBK), EncodingUtil.ENCODING_ISO8859_1);
			}
		} catch (UnsupportedEncodingException e) {
			return fileName;
		}
	}

	public static boolean move(String oldFilePathName, String newDirName) {
		File oldFile = new File(oldFilePathName);
		if (!oldFile.exists()) {
			return false;
		}
		File newDir = new File(newDirName);
		if (!newDir.exists()) {
			newDir.mkdirs();
		}
		File newFile = new File(newDirName + File.separator + oldFile.getName());
		return oldFile.renameTo(newFile);
	}

	public static void moveAllFiles(String oldDirName, String newDirName) {
		File newDir = new File(newDirName);
		if (!newDir.exists()) {
			newDir.mkdirs();
		}

		File oldDir = new File(oldDirName);
		File[] fileArray = oldDir.listFiles();
		for (int i = 0; i < fileArray.length; i++) {
			File oldFile = fileArray[i];
			File newFile = new File(newDirName + "/" + oldFile.getName());
			if (!newFile.exists()) {
				oldFile.renameTo(newFile);
			}
		}
	}

	/**
	 * 把文件或文件夹重命名
	 * 
	 * @param filePathName
	 */
	public static File rename(String filePathName, String newName) {
		File f = new File(filePathName);
		File newFile = null;
		if (f.exists()) {
			String path = filePathName.substring(0, filePathName.lastIndexOf(File.separator));
			newFile = new File(path + File.separator + newName);
			if (!newFile.exists()) {
				if (f.renameTo(newFile)) {
					return newFile;
				}
			}
		}
		return null;
	}

	public static List<File> list(String dirName) {
		File dir = new File(dirName);
		File[] fileArray = dir.listFiles();
		if (null != fileArray) {
			return Arrays.asList(fileArray);
		} else {
			return new ArrayList<File>();
		}		
	}

	/**
	 * 把文本文件切割为多个文件
	 * 
	 * @param file
	 * @param fileEncoding
	 *            文件编码
	 * @param splitSize
	 *            分割后每个文件大小，单位为字节
	 * @param headCount
	 *            分割后的每个文件要保留的文件头行数
	 * @return
	 */
	public static List<File> splitTxtFile(File file, String fileEncoding, int splitSize, int headCount) {
		String[] heads = FileUtil.head(file, fileEncoding, headCount);

		List<File> files = new ArrayList<File>();
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			if (file.length() <= splitSize) {
				files.add(file);
				return files;
			}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileEncoding));
			String line = null;
			int byteCount = 0;
			int fileIndex = 1;

			String subFileFullName = file.getAbsolutePath() + ".";
			File subFile = new File(subFileFullName + fileIndex);
			bw = new BufferedWriter(new FileWriter(subFile));

			while (null != (line = br.readLine())) {
				byteCount += line.getBytes().length;
				if (byteCount > splitSize) {
					bw.close();
					bw = null;
					files.add(subFile);

					byteCount = 0;
					fileIndex++;
					subFile = new File(subFileFullName + fileIndex);
					bw = new BufferedWriter(new FileWriter(subFile));
					// 添加文件头
					if (null != heads) {
						for (int i = 0; i < heads.length; i++) {
							String str = heads[i];
							byteCount += str.getBytes().length;
							bw.write(str + "\r\n");
						}
					}
				}
				bw.write(line + "\r\n");
			}

			files.add(subFile);
		} catch (Exception e) {
			log.error(e, e);
			return null;
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					log.error(e, e);
				}
				br = null;
			}
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error(e, e);
				}
				bw = null;
			}
		}
		file.delete();

		return files;
	}

	/**
	 * 提取文件的地start行开始，剩下的如果大于nums行，则取nums行，如果不够nums行，则取到end
	 * 
	 * @param file
	 * @param fileEncoding
	 * @return
	 */
	public static String[] fixedLinesRead(File file, String fileEncoding, int start,int nums) {
		int localstart = start -1;//start 是人的概念，第一行，程序的第一行概念是第0行
		int localend = localstart + nums-1;
		BufferedReader br = null;
		List<String> strs = new ArrayList<String>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileEncoding));
			String line = null;
			int count = 0;
			while ((count <= localend) && (null != (line = br.readLine()))) {
				if(count>=localstart&&line.trim()!=""&&line!=null&&line.length()!=0){
					strs.add(line);
				}
				count++;
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e, e);
		} catch (FileNotFoundException e) {
			log.error(e, e);
		} catch (IOException e) {
			log.error(e, e);
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					log.error(e, e);
				}
				br = null;
			}
		}
		log.info("fileUTil:programming start->" + localstart+";logical start:" + start+"||||end->" +localend+";--->total read size:" + strs.size());
		String[] ret = new String[strs.size()];
		return strs.toArray(ret);
	}
	/**
	 * 提取文件的前max行
	 * 
	 * @param file
	 * @param fileEncoding
	 * @param max
	 * @return
	 */
	public static String[] head(File file, String fileEncoding, int max) {
		BufferedReader br = null;
		List<String> strs = new ArrayList<String>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileEncoding));
			String line = null;
			int count = 0;
			while ((count < max) && (null != (line = br.readLine()))) {
				strs.add(line);
				count++;
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e, e);
		} catch (FileNotFoundException e) {
			log.error(e, e);
		} catch (IOException e) {
			log.error(e, e);
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					log.error(e, e);
				}
				br = null;
			}
		}
		String[] ret = new String[strs.size()];
		return strs.toArray(ret);
	}
	
	public static String readFileAsString(File file) {
		byte[] bytes = readFileAsBytes(file);
		return new String(bytes);
	}
	
	public static String readFileAsString(File file, String encoding) {
		byte[] bytes = readFileAsBytes(file);
		try {
			return new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			return "不支持的编码类型" + encoding;
		}
	}
	
	public static byte[] readFileAsBytes(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int length = fis.available();
			byte[] bytes = new byte[length];
			fis.read(bytes);			
			return bytes;
		} catch (FileNotFoundException e) {
			log.error(e, e);
		} catch (IOException e) {
			log.error(e, e);
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error(e, e);
				}
				fis = null;
			}
		}
		return new byte[0];
	}
	
	public static void save(String content, File file) throws IOException {
		byte[] bytes = content.getBytes();
		save(bytes, file);
	}

	public static void save(byte[] content, File file) throws IOException {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bis = new BufferedInputStream(new ByteArrayInputStream(content));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		}catch (Exception e) {
			log.error(e, e);
		} finally {
			if (null != bos) {
				bos.close();
			}
			if (null != bis) {
				bis.close();
			}
		}
	}
	
	public static void save(ByteArrayInputStream bais, File file) throws IOException {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bis = new BufferedInputStream(bais);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		} finally {
			if (null != bos) {
				bos.close();
			}
			if (null != bis) {
				bis.close();
			}
		}
	}
	
	public static void cleanDir(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
			return;
		}
		if (!dir.isDirectory()) {
			return;
		}
		if (!dir.canWrite()) {
			return;
		}
		File[] files = dir.listFiles();
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isFile()) {
				file.delete();
			} else {
				cleanDir(file);
			}
		}
	}
	
	public static BufferedWriter openTxtFileForWrite(File f) {
		return openTxtFileForWrite(f, "GBK");
	}
	
	public static BufferedWriter openTxtFileForWrite(File f, String encoding) {
		return openTxtFileForWrite(f, encoding, false);
	}
	
	public static BufferedWriter openTxtFileForWrite(File f, String encoding, boolean withBOM) {
		if (null == f) {
			return null;
		}
		if (f.exists()) {
			f.delete();				
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			log.error(e, e);
			return null;
		}
		BufferedWriter bw = null;
		try {
			FileOutputStream fos = new FileOutputStream(f);
			if (withBOM && "UTF-8".equalsIgnoreCase(encoding)) {
				fos.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
			}
			bw = new BufferedWriter(new OutputStreamWriter(fos, encoding));
		} catch (FileNotFoundException e) {
			log.error(e, e);
			return null;
		} catch (UnsupportedEncodingException e) {
			log.error(e, e);
			return null;
		} catch (IOException e) {
			log.error(e, e);
			return null;
		}
		return bw;
	}
	
	public static void closeFileWriter(Writer writer) {
		if (null != writer) {
			try {
				writer.close();
			} catch (IOException e) {
				log.error(e, e);
			}
			writer = null;
		}
	}
	
	public static boolean mkdir(String dirPath) throws IOException {
		boolean ret = false;
		File f = new File(dirPath);
		if (!f.exists()) {
			ret = f.mkdirs();
		} else {
			if (f.isFile()) {
				throw new IOException("路径是一个文件，不能创建为目录：" + dirPath);
			}
		}
		return ret;
	}

	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileExtension(String fileName){
		if (fileName == null || "".equals(fileName.trim()) || fileName.indexOf(".") == -1) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).trim();
	}
	
	/**
	 * 根据指定的路径，获取该路径下的所有指定文件的路径
	 * @param strPath		目录路径
	 * @param resultList	返回的结果集，递归调用
	 * @param filter		文件过滤器
	 * @throws RuntimeException 
	 */
	public static List<String> refreshFileList(String strPath, List<String> resultList, FileFilter filter) throws Exception {
		File dir = new File(strPath);
		if (!dir.isDirectory()) {
			throw new RuntimeException("The parameters \"" + strPath + "\" is not a valid directory path.");
		} 
		if (resultList == null) {
			resultList = new ArrayList<String>();
		}
		File[] files = dir.listFiles(filter);
		for(File f : files) {
			if(f.isDirectory()) {
				refreshFileList(f.getAbsolutePath(), resultList, filter);
			} else {
				resultList.add(f.getAbsolutePath());
			}
		}
		return resultList;
	}
    public static String reBuildFileName(String originName) {
        int index = originName.lastIndexOf(".");
        if(Detect.isPositive(index)){
        	long l = RandomUtils.nextLong(0, 1000000L);

            StringBuilder s = new StringBuilder();
            s.append(new Date().getTime());
            s.append("_");
            s.append(l);
            s.append(originName.substring(index, originName.length()));
            return s.toString();
        }
        return null ;
    }

//	/**
//	 * 上传图片
//	 * @param cmf
//	 * @return
//	 * @throws IOException
//	 */
//	public static  String uploadImg(CommonsMultipartFile cmf, String chilrenPath) throws IOException {
//		if(cmf!=null&&cmf.getInputStream().read()>-1&&Detect.notEmpty(cmf.getOriginalFilename())){
//			// 通过request获取项目实际运行目录,就可以将上传文件存放在项目目录了,不管项目部署在任何地方都可以
//			String fileRequestUrl = "";
//			String fileName= FileUtil.reBuildFileName(cmf.getOriginalFilename());
//			SFTPUtils sf = new SFTPUtils();
//			boolean isOk = sf.uploadFileToPictureServer(cmf.getInputStream(),fileName);
//			if(isOk){
//				return fileRequestUrl+fileName;
//			}else{
//				return "";
//			}
//		}
//		return "";
//	}
//
//	/**
//	 * 上传图片
//	 * @return
//	 * @throws Exception
//	 */
//	public static Map<String, Object> uploadProductImg(MultipartHttpServletRequest mhsr) throws Exception {
//		Assertion.notNull(mhsr, "参数不能为空");
//		String fileName = mhsr.getAttribute("fileName") + "";
//		List<Map<String,MultipartFile>> images = new ArrayList<Map<String,MultipartFile>>();
//		uploadProductParame(mhsr,images);
//		Assertion.notEmpty(images, "图片不存在");
//		for(Map<String,MultipartFile> map : images){
//			MultipartFile maxImg = map.get("porList");
//			MultipartFile minImg = map.get("porDetail");
//			MultipartFile square = map.get("porOther");
//			MultipartFile other = map.get("other");
//			if(maxImg != null && Detect.notEmpty(maxImg.getOriginalFilename())){
//				//列表图片
//				if(!Detect.notEmpty(fileName)){
//					fileName= FileUtil.reBuildFileName(maxImg.getOriginalFilename());
//				}
//				SFTPUtils.uploadFileToPictureServer(maxImg.getInputStream(), fileName,SftpPropertiesUtil.getProperties("FILE_IMG_PORLIST"));
//			}else if(minImg != null && Detect.notEmpty(minImg.getOriginalFilename())){
//				//详情图片
//				if(!Detect.notEmpty(fileName)){
//					fileName= FileUtil.reBuildFileName(minImg.getOriginalFilename());
//				}
//				SFTPUtils.uploadFileToPictureServer(minImg.getInputStream(), fileName,SftpPropertiesUtil.getProperties("FILE_IMG_PORDETAIL"));
//			}else if(square != null && Detect.notEmpty(square.getOriginalFilename())){
//				//其他图片
//				if(!Detect.notEmpty(fileName)){
//					fileName= FileUtil.reBuildFileName(square.getOriginalFilename());
//				}
//				SFTPUtils.uploadFileToPictureServer(square.getInputStream(), fileName,SftpPropertiesUtil.getProperties("FILE_IMG_POROTHER"));
//			}else if(other != null && Detect.notEmpty(other.getOriginalFilename())){
//				//其他图片
//				if(!Detect.notEmpty(fileName)){
//					fileName= FileUtil.reBuildFileName(other.getOriginalFilename());
//				}
//				SFTPUtils.uploadFileToPictureServer(other.getInputStream(), fileName,SftpPropertiesUtil.getProperties("FILE_IMG_OTHER"));
//			}
//		}
//		Map<String, Object> resultMap=new HashMap<String, Object>();
//		//获取访问路径
//		resultMap.put("fileName", fileName);
//		String fileUrl = SftpPropertiesUtil.getProperties("FILE_SERVERURL");
//		String max = SftpPropertiesUtil.getProperties("FILE_SERVERURL_PORLIST");
//		String min = SftpPropertiesUtil.getProperties("FILE_SERVERURL_PORDETAIL");
//		String square = SftpPropertiesUtil.getProperties("FILE_SERVERURL_POROTHER");
//		String other = SftpPropertiesUtil.getProperties("FILE_SERVERURL_OTHER");
//		resultMap.put("proListFileUrl", fileUrl + max + fileName);
//		resultMap.put("proDetailFileUrl", fileUrl + min + fileName);
//		resultMap.put("proOtherFileUrl", fileUrl + square + fileName);
//		resultMap.put("otherFileUrl", fileUrl + other + fileName);
//		return resultMap;
//	}
	
	/**
	 * 产品图片
	 * @param mhsr
	 * @return
	 * @throws Exception
	 */
	public static void uploadProductParame(MultipartHttpServletRequest mhsr, List<Map<String,MultipartFile>> images) throws Exception{
//		List<MultipartFile> filesMax = mhsr.getMultiFileMap().get("porList");
//		CommonsMultipartFile maxImg = (CommonsMultipartFile)Detect.firstOne(filesMax);
//		List<MultipartFile> filesMin = mhsr.getMultiFileMap().get("porDetail");
//		CommonsMultipartFile minImg = (CommonsMultipartFile)Detect.firstOne(filesMin);
//		List<MultipartFile> filesSquare = mhsr.getMultiFileMap().get("porOther");
//		CommonsMultipartFile square = (CommonsMultipartFile)Detect.firstOne(filesSquare);
//		List<MultipartFile> filesOther = mhsr.getMultiFileMap().get("other");
//		CommonsMultipartFile other = (CommonsMultipartFile)Detect.firstOne(filesOther);
		
		MultipartFile maxImg = mhsr.getMultiFileMap().getFirst("porList");
		MultipartFile minImg = mhsr.getMultiFileMap().getFirst("porDetail");
		MultipartFile square = mhsr.getMultiFileMap().getFirst("porOther");
		MultipartFile other = mhsr.getMultiFileMap().getFirst("other");
		
		Map<String,MultipartFile> map = null;
		if(maxImg != null){
			map = new HashMap<String,MultipartFile>();
			map.put("porList", maxImg);
			images.add(map);
		}
		if(minImg != null){
			map = new HashMap<String,MultipartFile>();
			map.put("porDetail", minImg);
			images.add(map);
		}
		if(square != null){
			map = new HashMap<String,MultipartFile>();
			map.put("porOther", square);
			images.add(map);
		}
		if(other != null){
			map = new HashMap<String,MultipartFile>();
			map.put("other", other);
			images.add(map);
		}
	}
    
}
