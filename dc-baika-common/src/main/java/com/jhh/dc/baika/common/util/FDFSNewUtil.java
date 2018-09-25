package com.jhh.dc.baika.common.util;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yy
 * @project risk-common
 * @date 2017年7月11日 上午10:32:31
 * @description TODO fdfs文件存储 工具类
 * @tag
 * @company 上海金互行金融信息服务有限公司
 */
public class FDFSNewUtil {
    private FDFSNewUtil() {
    }

    static {
        init("fdfs_client.conf");
    }

    public static void init(String config) {
        try {
            ClientGlobal.init(config);
        } catch (Exception e) {
            throw new RuntimeException("初始化FDFS出错：", e);
        }
    }

    /**
     * @param file_buff     字节数
     * @param file_ext_name 后缀名
     * @return String
     * @throws Exception
     * @title uploadFile
     * @description 按字节上传文件
     * @author yy
     * @date 2017年7月11日 上午10:38:51
     */
    public static String uploadFile(byte[] file_buff, String file_ext_name) throws Exception {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            String filePath = storageClient1.upload_file1(file_buff, file_ext_name, null);
            if (filePath == null) {
                return null;
            }
            String responseFileName = "http://" + storageServer.getInetSocketAddress().getHostName() + "/" + filePath;
            return responseFileName;
        } catch (Exception e) {
            throw e;
        } finally {
            closeServer(trackerServer, storageServer);
        }
    }

    private static void closeServer(TrackerServer trackerServer, StorageServer storageServer) {
        if (storageServer != null) {
            try {
                storageServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (trackerServer != null) {
            try {
                trackerServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param remote_filename
     * @return boolean
     * @throws Exception
     * @title deleteFile
     * @description 删除文件
     * @author yy
     * @date 2017年7月11日 上午10:54:38
     */
    private static boolean delete_file(String remote_filename) throws Exception {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            String group_name = remote_filename.substring(0, remote_filename.indexOf("/"));
            remote_filename = remote_filename.substring(remote_filename.indexOf("/") + 1);
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            int i = storageClient1.delete_file(group_name, remote_filename);
            return i == 0;
        } catch (Exception e) {
            throw e;
        } finally {
            closeServer(trackerServer, storageServer);
        }
    }

    /**
     * @param local_filename 文件名
     * @param file_ext_name  后缀名
     * @return String
     * @throws Exception
     * @title uploadFile
     * @description 按名称上传文件
     * @author yy
     * @date 2017年7月11日 上午10:42:13
     */
    public static String uploadFile(String local_filename, String file_ext_name) throws Exception {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);

            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            String filePath = storageClient1.upload_file1(local_filename, file_ext_name, null);

            storageServer.close();
            trackerServer.close();

            if (filePath == null) {
                return null;
            }
            String responseFileName = "http://" + storageServer.getInetSocketAddress().getHostName() + "/" + filePath;
            return responseFileName;
        } catch (Exception e) {
            throw e;
        } finally {
            closeServer(trackerServer, storageServer);
        }
    }

    /**
     * @param remote_filename 文件名
     * @return String
     * @throws Exception
     * @title downloadFileAsString
     * @description 下载文件 ，返回默认编码 格式的字符串
     * @author yy
     * @date 2017年7月11日 上午10:43:39
     */
    public static String downloadString(String remote_filename) {
        return HttpUrlPost.sendGet(remote_filename, "");
    }

    /**
     * @param remote_filename
     * @return byte[]
     * @title downloadFile
     * @description 返回字节流
     * @author yy
     * @date 2017年7月11日 上午11:04:44
     */
    public static byte[] downloadFile(String remote_filename) throws Exception {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            String group_name = remote_filename.substring(0, remote_filename.indexOf("/"));
            remote_filename = remote_filename.substring(remote_filename.indexOf("/") + 1);
            byte[] fileContent = storageClient1.download_file(group_name, remote_filename);

            storageServer.close();
            trackerServer.close();

            return fileContent;
        } catch (Exception e) {
            throw e;
        } finally {
            closeServer(trackerServer, storageServer);
        }
    }


    /**
     * @param bytes
     * @return String
     * @title uploadBytes
     * @description 按字节流上传
     * @author yy
     * @date 2017年7月12日 下午2:22:13
     */
    public static String uploadBytes(byte[] bytes) {
        try {
            return uploadFile(bytes, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return String
     * @title uploadFile
     * @description 存储字符串
     * @author yy
     * @date 2017年7月11日 上午10:48:30
     */
    public static String uploadString(String stringContent) {
        try {
            return uploadFile(stringContent.getBytes(), "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> uploadStrings(String... stringContent) {
        List<String> list = new ArrayList<String>();
        try {
            for (String string : stringContent) {
                list.add(uploadFile(string.getBytes(), ""));
            }
            return list;
        } catch (Exception e) {
            try {//删掉已经成功上传的
                for (String string : list) {
                    delete_file(string);
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * @param remote_filename
     * @return boolean
     * @title deleteFile
     * @description 删除文件
     * @author yy
     * @date 2017年7月11日 上午11:07:05
     */
    public static boolean deleteFile(String remote_filename) {
        int i = 0;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            String group_name = remote_filename.substring(0, remote_filename.indexOf("/"));
            remote_filename = remote_filename.substring(remote_filename.indexOf("/") + 1);

            i = storageClient1.delete_file(group_name, remote_filename);
            storageServer.close();
            trackerServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i == 0;
    }
}
