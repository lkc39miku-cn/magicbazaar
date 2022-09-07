package com.a243.magicbazaar.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {

    /**
     * 上传单张图片
     *
     * @param file file
     * @return 文件保存路径
     */
    public static String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        StringBuilder stringBuilder = new StringBuilder();
        if (!"".equals(originalFilename)) {
            // 2.文件保存路径
            stringBuilder.append("/image/");

            try {
                File f = new File("D:/Idea-Project" + stringBuilder.toString());
                if (!f.exists()) {
                    f.mkdirs();
                }

                // 3.文件名:重名文件覆盖，唯一不重复
                stringBuilder.append(UUID.randomUUID().toString());
                assert originalFilename != null;
                stringBuilder.append(originalFilename.substring(originalFilename.lastIndexOf(".")));

                // 4.上传
                file.transferTo(new File("D:/Idea-Project/" + stringBuilder.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // image/uuid.jpg
        return stringBuilder.toString();
    }

    /**
     * 上传多张图片
     *
     * @param files files
     * @return 返回所有图片的保存路径，分号分割
     */
    public static String upload(MultipartFile[] files) {
        StringBuilder stringBuilder = new StringBuilder();
        for (MultipartFile file : files) {
            stringBuilder.append(upload(file));
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }
}