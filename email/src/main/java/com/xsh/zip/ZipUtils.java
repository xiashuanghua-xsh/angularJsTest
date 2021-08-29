package com.xsh.zip;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 *
 * @author xsh
 * @Date 2021-08-29
 */
public class ZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);
    /**
     * buff大小
     */
    public static final Integer BUFF_SIZE = 1024;

    public static final Integer MEGA = 1024;

    /**
     * 是否保留文件原文件结构
     */
    public static final Boolean KEEP_DIR_STRUCTURE = true;

    public static void main(String[] args) {
        String inPutFilePath = "D:\\tset\\email_send";

        String outPutPath = "D:\\tset\\11111111\\aaa.zip";
        logger.info("start to zip file");
        toZip(inPutFilePath, outPutPath, "UTF-8", false);
        logger.info("end to zip file");

    }

    public static void toZip(String inputPathFile, String outPutPathFile, String charSet, Boolean isDelOrgFile) {
        ZipOutputStream zos = null;
        OutputStream os = null;
        try {
            File file = new File(inputPathFile);
            os = new FileOutputStream(new File(outPutPathFile));
            zos = new ZipOutputStream(os);
            if (!file.exists()) {
                System.out.println("文件不存在");
                return;
            }
            compress(file, zos, file.getName(), KEEP_DIR_STRUCTURE);
        } catch (IOException e) {
            logger.error("zip file is fail:", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    logger.error("ZipOutputStream close fail:", e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("OutputStream close fail:", e);
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param orgFilePath 文件路径
     */
    public void delOrgFile(String orgFilePath) {
        FileUtil.del(orgFilePath);
    }

    /**
     * @param inputFile        压缩源文件
     * @param zos              压缩文件输出流
     * @param path             压缩文件名称或者文件夹名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws IOException 异常
     */
    public static void compress(File inputFile, ZipOutputStream zos, String path, boolean keepDirStructure) throws IOException {
        byte[] buff = new byte[BUFF_SIZE];
        if (inputFile.isFile()) {
            zos.putNextEntry(new ZipEntry(path));
            int len = 0;
            FileInputStream fis = new FileInputStream(inputFile);
            while ((len = fis.read(buff)) != -1) {
                zos.write(buff, 0, len);
            }
            zos.closeEntry();
            fis.close();
        } else {
            File[] files = inputFile.listFiles();
            if (FileUtil.isDirEmpty(inputFile)) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(path + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : files) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, path + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }

                }
            }

        }

    }

    public void zip(String path) throws IOException {
        File file = new File(path);

        BufferedInputStream origin = null;
        byte data[] = new byte[BUFF_SIZE];
        FileInputStream fi = new FileInputStream(file);
        origin = new BufferedInputStream(fi, BUFF_SIZE);
        int count;
        int offset = 0;
        ZipOutputStream out = null;
        while (true) {
            if ((count = origin.read(data, 0, BUFF_SIZE)) != -1) {
                if (offset % (MEGA) == 0) {
                    FileOutputStream dest = new FileOutputStream(file.getName()
                            + ".(part" + (offset / (MEGA) + 1) + ").zip");
                    out = new ZipOutputStream(new BufferedOutputStream(dest));
                    ZipEntry entry = new ZipEntry(file.getName());
                    out.putNextEntry(entry);
                }
                out.write(data, 0, count);
                offset += 1024;
                if (offset % (MEGA) == 0 && offset != 0) {
                    out.close();
                }
            } else {
                out.close();
                break;
            }
        }
        origin.close();
    }

    public void unzip(String[] path) throws IOException {
        File file = new File(path[0].split("\\.")[0] + "."
                + path[0].split("\\.")[1]);

        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(file));
        byte data[] = new byte[BUFF_SIZE];
        int offset = 0;
        for (int i = 0; i < path.length; i++) {
            ZipInputStream unzipfile = new ZipInputStream(new FileInputStream(
                    path[0]));
            BufferedInputStream in = new BufferedInputStream(unzipfile);
            ZipEntry entry = unzipfile.getNextEntry();
            int count = 0;
            while ((count = in.read(data, 0, BUFF_SIZE)) != -1) {
                out.write(data, 0, count);
            }
            in.close();
        }
        out.close();
    }

}
