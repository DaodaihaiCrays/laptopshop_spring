package vn.hoidanit.laptopshop.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(UserService userService, ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String handleSaveUploadFile(MultipartFile file, String targetFolder) {
        if (file.isEmpty()) {
            return "";
        }
        String fileName = "";
        String fileDB = "";
        try {
            byte[] bytes;
            bytes = file.getBytes();

            // this.servletContext.getRealPath chỉ trả về folder webapp nên phải cộng thêm
            // path /resources/images
            String rootPath = this.servletContext.getRealPath("/resources/images");

            // File.separator = /
            File dir = new File(rootPath + File.separator + targetFolder);

            // Chưa có file đó thì tạo
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server
            fileName = dir.getAbsolutePath() + File.separator +
            +System.currentTimeMillis() + "-" + file.getOriginalFilename();
            fileDB = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(fileName);
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileDB;
    }
}
