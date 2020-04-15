package com.taurus.common.service;

import com.taurus.api.enums.FileTypeEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理
 *
 * @author 郑楷山
 **/
public interface IFileService {

    String getFileRootPath();

    boolean fileSuffix(String fileName, FileTypeEnum fileTypeEnum);

    String fileUpload(MultipartFile multipartFile, FileTypeEnum fileTypeEnum);

    boolean fileRemove(String fileURI);
}
