package com.example.mbn.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String upload(MultipartFile file, String folderName) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            String folderPath = new File(uploadDir + "/" + folderName + "/").getAbsolutePath() + "/";

            File dest = new File(folderPath + fileName);
            dest.getParentFile().mkdirs(); // 없으면 디렉토리 생성
            file.transferTo(dest);

            return "/uploads/" + folderName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
}
