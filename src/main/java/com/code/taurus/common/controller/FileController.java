package com.code.taurus.common.controller;

import com.code.taurus.api.enums.FileTypeEnum;
import com.code.taurus.common.model.GlobalFileModel;
import com.code.taurus.common.service.impl.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 控制器： 文档图片处理接口
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Global: 文件处理")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("添加文件")
    @PostMapping
    public GlobalFileModel createFile(
            @RequestParam @ApiParam(value = "文件类型", required = true) FileTypeEnum fileType,
            @RequestParam @ApiParam(value = "文件", required = true) MultipartFile file) {
        return new GlobalFileModel()
                .setFilePath(fileService.fileUpload(file, fileType))
                .setFileName(file.getOriginalFilename());
    }

    @ApiOperation("添加多文件")
    @PostMapping("/multi")
    public List<GlobalFileModel> createFiles(
            @RequestParam @ApiParam(value = "文件类型", required = true) FileTypeEnum fileType,
            @RequestParam("files") @ApiParam(value = "文件", required = true) MultipartFile[] files) {
        List<GlobalFileModel> fileModelList = new ArrayList<>();
        Arrays.stream(files).forEach(file ->
                fileModelList.add(new GlobalFileModel()
                        .setFilePath(fileService.fileUpload(file, fileType))
                        .setFileName(file.getOriginalFilename())));
        return fileModelList;
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public void delete(@RequestParam @ApiParam(value = "文件路径", required = true) String url) {
        fileService.fileRemove(Collections.singletonList(url));
    }


}
