package com.ruoyi.web.ftp;

/**
 * TODO
 *
 * @author
 * @Description:
 * @date 2022/8/18 15:07
 */

import java.io.*;
import java.util.*;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTP {

    private String Control_Encoding = "UTF-8";

    private FTPClient client = null;

    private String host = "";
    private int port = 21;
    private String user = "";
    private String password = "";
    private String ftpPath = "/";
    private String configFile = "ftp-config.properties";
    // FTP名称
    private String ftpName;
    // 属性集
    private ResourceBundle property = null;
    Properties properties = new Properties();

    @SuppressWarnings("unused")
    private FTP() {
    }

    public FTP(String host, int port, String user, String pwd) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = pwd;
    }


    public FTP(String ftpName) {
        this.ftpName = ftpName;
        setArg(configFile);
    }
    /**
     * 设置参数
     *
     * @param configFile 参数的配置文件
     */
    private void setArg(String configFile) {
        try {
            InputStream resourceAsStream = FTP.class.getClassLoader().getResourceAsStream(configFile);

            try {
                properties.load(resourceAsStream);
            } catch (IOException e) {
                e.printStackTrace();
            }


            user = properties.getProperty(ftpName + "username");
            password = properties.getProperty(ftpName + "password");
            host = properties.getProperty(ftpName + "host");
            port = Integer.parseInt(properties.getProperty(ftpName + "port"));
//            property = ResourceBundle.getBundle(configFile);
//            user = property.getString(ftpName + "username");
//            password = property.getString(ftpName + "password");
//            host = property.getString(ftpName + "host");
//            port = Integer.parseInt(property.getString(ftpName + "port"));
        } catch (Exception e) {
            System.out.println("配置文件 " + configFile + " 无法读取！");
        }
    }

    /**
     * 获取当前FTP所在目录位置
     *
     * @return
     */
    public String getHome() {
        return this.ftpPath;
    }

    /**
     * 连接FTP Server
     *
     * @throws IOException
     */
    public static final String ANONYMOUS_USER = "anonymous";

    public void connect() throws Exception {
        if (client == null) {
            client = new FTPClient();
        }
        // 已经连接
        if (client.isConnected()) {
            return;
        }

        // 编码
        client.setControlEncoding(Control_Encoding);
        client.setDataTimeout(180000);
        client.enterLocalActiveMode(); //主动模式    //不调用这个方法 调用listFiles()方法时会出现阻塞 不继续往下执行
        // client.enterLocalPassiveMode(); 被动模式
        try {
            // 连接FTP Server
            client.connect(this.host, this.port);
            // 登陆
            if (this.user == null || "".equals(this.user)) {
                // 使用匿名登陆
                client.login(ANONYMOUS_USER, ANONYMOUS_USER);
            } else {
                client.login(this.user, this.password);
            }
            // 设置文件格式
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取FTP Server 应答
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw new Exception("connection FTP fail!");
            }

            FTPClientConfig config = new FTPClientConfig(client.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            client.configure(config);
            // 使用被动模式设为默认
            client.enterLocalPassiveMode();
            // 二进制文件支持
            client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            // 设置模式
            client.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);

        } catch (IOException e) {
            throw new Exception("connection FTP fail! " + e);
        }
    }

    /**
     * 断开FTP连接
     *
     * @param
     * @throws IOException
     */
    public void close() {
        if (client != null && client.isConnected()) {
            try {
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件列表
     *
     * @return
     */
    public List<FTPFile> list() {
        List<FTPFile> list = null;
        try {
            client.enterLocalActiveMode(); //主动模式    //不调用这个方法 调用listFiles()方法时会出现阻塞 不继续往下执行
            // client.enterLocalPassiveMode(); 被动模式
            FTPFile ff[] = client.listFiles(ftpPath);
            if (ff != null && ff.length > 0) {
                list = new ArrayList<FTPFile>(ff.length);
                Collections.addAll(list, ff);
            } else {
                list = new ArrayList<FTPFile>(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 切换目录
     *
     * @param path
     *            需要切换的目录
     * @param forcedIncrease
     *            如果目录不存在，是否增加
     */
    public void switchDirectory(String path, boolean forcedIncrease) {
        try {
            if (path != null && !"".equals(path)) {
                boolean ok = client.changeWorkingDirectory(path);
                if (ok) {
                    this.ftpPath = path;
                } else if (forcedIncrease) {
                    // ftpPath 不存在，手动创建目录
                    StringTokenizer token = new StringTokenizer(path, "\\//");
                    while (token.hasMoreTokens()) {
                        String npath = token.nextToken();
                        client.makeDirectory(npath);
                        client.changeWorkingDirectory(npath);
                        if (ok) {
                            this.ftpPath = path;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public void createDirectory(String path) {
        try {
            if (path != null && !"".equals(path)) {
                boolean ok = client.changeWorkingDirectory(path);
                if (!ok) {
                    // ftpPath 不存在，手动创建目录
                    StringTokenizer token = new StringTokenizer(path, "\\//");
                    while (token.hasMoreTokens()) {
                        String npath = token.nextToken();
                        client.makeDirectory(npath);
                        client.changeWorkingDirectory(npath);
                    }
                }
            }
            client.changeWorkingDirectory(this.ftpPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除目录，如果目录中存在文件或者文件夹则删除失败
     *
     * @param path
     * @return
     */
    public boolean deleteDirectory(String path) {
        boolean flag = false;
        try {
            flag = client.removeDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public boolean deleteFile(String path) {
        boolean flag = false;
        try {
            flag = client.deleteFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件，上传文件只会传到当前所在目录
     *
     * @param localFile
     *            本地文件
     * @return
     */
    public boolean upload(File localFile) {
        return this.upload(localFile, "");
    }

    /**
     * 上传文件，会覆盖FTP上原有文件
     *
     * @param localFile
     *            本地文件
     * @param reName
     *            重名
     * @return
     */
    public boolean upload(File localFile, String reName) {
        boolean flag = false;
        String targetName = reName;
        // 设置上传后文件名
        if (reName == null || "".equals(reName)) {
            targetName = localFile.getName();
        }
        FileInputStream fis = null;
        try {
            // 开始上传文件
            fis = new FileInputStream(localFile);
            client.setControlEncoding(Control_Encoding);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalActiveMode(); //主动模式    //不调用这个方法 调用listFiles()方法时会出现阻塞 不继续往下执行
            // client.enterLocalPassiveMode(); 被动模式
            boolean ok = client.storeFile(targetName, fis);
            if (ok) {
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 下载文件，如果存在会覆盖原文件
     *
     * @param ftpFileName
     *            文件名称，FTP上的文件名称
     * @param savePath
     *            保存目录，本地保存目录
     * @return
     */
    public boolean download(String ftpFileName, String savePath) {
        boolean flag = false;

        File dir = new File(savePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream fos = null;
        try {
            String saveFile = dir.getAbsolutePath() + File.separator + ftpFileName;
            fos = new FileOutputStream(new File(saveFile));
            boolean ok = client.retrieveFile(ftpFileName, fos);
            if (ok) {
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String args[]) {



        // 创建FTP对象
        FTP ftp = new FTP("ATTATEST_");
        try {
            // 连接FTP
            ftp.connect();

            // 移动工作空间、切换目录
            System.out.println("当前位置：" + ftp.getHome());
            ftp.switchDirectory("/test", true);
            System.out.println("当前位置：" + ftp.getHome());

            // 查询目录下的所有文件夹以及文件
            List<FTPFile> list = ftp.list();
            System.out.println("|-- " + ftp.getHome());
            for (FTPFile f : list) {
                System.out.println(" |-- [" + (f.getType() == 0 ? "文件" : "文件夹") + "]" + f.getName());
            }

            // 上传文件
            boolean r1 = ftp.upload(new File("E:\\Z_FTP\\file\\BookCabinet_1.0.2_20220823-2014.apk"), "测试文件2.apk");
//            boolean r1 = ftp.upload(new File("E:\\Z_pic\\01.jpg"), "测试.png");
            System.out.println("上传文件：" + r1);

            // 下载文件
//            boolean r2 = ftp.download("测试.png", "C:\\Users\\ZY\\Desktop");
//            System.out.println("下载文件：" + r2);

            // 删除文件
//            boolean r3 = ftp.deleteFile("/test/测试.png");
//            System.out.println("删除文件：" + r3);
//
//            // 删除目录
//            boolean r4 = ftp.deleteDirectory("/test");
//            System.out.println("删除目录：" + r4);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ftp.close();
    }

}

