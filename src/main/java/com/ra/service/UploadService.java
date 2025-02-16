package com.ra.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadService {
 private final Cloudinary cloudinary;
 public String uploadFile(MultipartFile file){
  // lay ten file goc
  String originalFileName = file.getOriginalFilename();

  if(originalFileName != null && originalFileName.contains(".")){
   originalFileName = originalFileName.substring(0,originalFileName.lastIndexOf("."));
  }
  // thêm tên file vào tham số upload
  Map uploadParams = ObjectUtils.asMap(
          "public_id",originalFileName
  );
  // up file lên cloud
  try {
   Map uploadResult = cloudinary.uploader().upload(file.getBytes(),uploadParams);
   return uploadResult.get("url").toString();
  } catch (IOException e) {
   throw new RuntimeException(e);
  }
 }

}
