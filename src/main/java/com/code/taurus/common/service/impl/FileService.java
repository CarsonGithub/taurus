package com.code.taurus.common.service.impl;

import com.code.taurus.api.enums.FileTypeEnum;
import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * 服务: 文件资源相关
 *
 * @author 郑楷山
 **/

@Service
@Slf4j
public class FileService {

    @Value("${taurus.file-root-path}")
    private String fileRootPath;

    /**
     * 获取文件上传根目录
     **/
    public String getFileRootPath() {
        return fileRootPath;
    }

    /**
     * 判断文件类型
     **/
    public boolean fileSuffix(String fileName, FileTypeEnum fileTypeEnum) {
        String[] fileNameSplit = fileName.split(Pattern.quote("."));
        String suffix = fileNameSplit[fileNameSplit.length - 1].toLowerCase();
        switch (fileTypeEnum.getType()) {
            case "image":
                return CommonConstant.IMAGE_SUFFIX_SET.contains(suffix);
            case "video":
                return CommonConstant.VIDEO_SUFFIX_SET.contains(suffix);
            case "script":
                return CommonConstant.SCRIPT_SUFFIX_SET.contains(suffix);
            case "doc":
                return CommonConstant.DOC_SUFFIX_SET.contains(suffix);
            default:
                return false;
        }
    }

    /**
     * 上传文件并获取路径
     **/
    public String fileUpload(MultipartFile multipartFile, FileTypeEnum fileTypeEnum) {
        if (multipartFile == null) {
            return "";
        }
        String fileName = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException(ExceptionEnum.UPLOAD_FILE_NAME_NULL_ERROR);
        }
        Consumer<MultipartFile> checkFile = (file) -> {
            if (!fileSuffix(fileName, fileTypeEnum)) {
                throw new BusinessException(ExceptionEnum.UPLOAD_SUFFIX_ERROR);
            }
            if (file.getSize() > fileTypeEnum.getSize()) {
                throw new BusinessException(ExceptionEnum.UPLOAD_SIZE_LIMIT);
            }
        };
        checkFile.accept(multipartFile);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        String currentMonth = dateTimeFormatter.format(LocalDate.now());
        String fileNameFormat = "%s" + currentMonth + "-%s.%s";
        String urlFormat = CommonConstant.UPLOAD_FILE_PATH + fileTypeEnum.getPath() + currentMonth + "-%s.%s";
        String[] fileNameSplit = Objects.requireNonNull(multipartFile.getOriginalFilename()).split(Pattern.quote("."));
        String saveFileName = UUID.randomUUID().toString();
        String suffix = fileNameSplit[fileNameSplit.length - 1];
        File diskFile = new File(String.format(fileNameFormat, getFileRootPath() + fileTypeEnum.getPath(), saveFileName, suffix));
        if (!diskFile.exists()) {
            diskFile.mkdirs();
        }
        try {
            multipartFile.transferTo(diskFile);
        } catch (IOException e) {
            throw new BusinessException(ExceptionEnum.SERVER_ERROR, e);
        }
        return String.format(urlFormat, saveFileName, suffix);
    }

    /**
     * 文件移除
     **/
    public void fileRemove(List<String> toRemoveList) {
        if (CollectionUtils.isEmpty(toRemoveList)) {
            return;
        }
        toRemoveList.forEach(path -> {
            path = getFileRootPath() + path.substring(CommonConstant.UPLOAD_FILE_PATH.length());
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    log.info("已移除文件:" + path);
                } else {
                    throw new BusinessException(ExceptionEnum.FILE_FOUND_ERROR);
                }
            }
        });
    }

}
