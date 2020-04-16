package com.taurus.common.controller;

import com.google.common.collect.Lists;
import com.taurus.api.enums.FileTypeEnum;
import com.taurus.common.model.GlobalFileVO;
import com.taurus.common.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
    private IFileService fileService;

    @ApiOperation("添加文件")
    @PostMapping
    public GlobalFileVO createFile(
            @RequestParam @ApiParam(value = "文件类型" ,required = true) FileTypeEnum fileType,
            @RequestParam @ApiParam(value = "文件" ,required = true) MultipartFile file) {
        return  new GlobalFileVO()
        .setFdFilePath(fileService.fileUpload(file,fileType))
        .setFdFileName(file.getOriginalFilename());
    }

    @ApiOperation("添加多文件")
    @PostMapping("/multi")
    public List<GlobalFileVO> createFiles(
            @RequestParam @ApiParam(value = "文件类型" ,required = true)  FileTypeEnum fileType,
             @RequestParam("files")  @ApiParam(value = "文件" ,required = true) MultipartFile[] files) {
        List<GlobalFileVO> fileVOList = Lists.newArrayList();
        Arrays.stream(files).forEach(file ->
                fileVOList.add(new GlobalFileVO()
                    .setFdFilePath(fileService.fileUpload(file, fileType))
                    .setFdFileName(file.getOriginalFilename())));
        return fileVOList;
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public void delete(@RequestParam  @ApiParam(value = "文件路径",required = true) String url){
        fileService.fileRemove(url);
    }


}
