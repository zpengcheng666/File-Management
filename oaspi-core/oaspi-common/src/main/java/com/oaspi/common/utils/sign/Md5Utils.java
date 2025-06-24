package com.oaspi.common.utils.sign;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Md5加密方法
 * 
 * @author oaspi
 */
public class Md5Utils
{
    private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);


    private static byte[] md5(String s)
    {
        MessageDigest algorithm;
        try
        {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes("UTF-8"));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        }
        catch (Exception e)
        {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[])
    {
        if (hash == null)
        {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++)
        {
            if ((hash[i] & 0xff) < 0x10)
            {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s)
    {
        try
        {
            return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            log.error("not supported charset...{}", e);
            return s;
        }
    }

    // 已与前端Spark-md5生成的结果核对一致
    public static String calculateFileMD5(File file) {
        return calculateFileMD5(file.getAbsolutePath());
    }
    public static String calculateFileMD5(String filePath) {
        try {
            // 创建一个MessageDigest实例，指定算法为MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // 打开文件输入流
            InputStream fis = new FileInputStream(filePath);

            // 创建一个缓冲区用于读取文件
            byte[] buffer = new byte[1024];
            int numRead;

            // 逐块读取文件内容并更新摘要
            while ((numRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, numRead);
            }

            // 关闭输入流
            fis.close();

            // 转换摘要为16进制字符串
            return toHexString(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将字节数组转换为16进制字符串
    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
