package com.tnpy.mes.controller;

import com.tnpy.common.Enum.DirectoryEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.DocumentMapper;
import com.tnpy.mes.model.mysql.Document;
import com.tnpy.mes.service.documentService.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/20 13:08
 */

@RestController
@RequestMapping("/api")
public class DocumentController {
    @Autowired
    private IDocumentService documentService;

    @Autowired
    private DocumentMapper documentMapper;


    @ResponseBody
    @RequestMapping(value = "/getdoctypetype")
    public TNPYResponse getDocType() {
       return  documentService.getDocType();
    }
    @PostMapping("/documentupload")
    public TNPYResponse upload(MultipartFile fileUpload,int type,String summary,String creator){
      //  System.out.println(fileUpload.toString());
        TNPYResponse response = new TNPYResponse();
        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        //获取文件后缀名
       // String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
       // fileName = UUID.randomUUID()+suffixName;
        //指定本地文件夹存储图片
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        String filePath = DirectoryEnum.FileStoreLocation.UploadDocument.getName()  + dateFormat.format(date) + "/";
        try {
            File dir = new File(filePath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            //将图片保存到static文件夹里

          //  fileUpload.transferTo(new File(filePath+fileName));

            FileOutputStream out = new FileOutputStream(filePath+fileName);
            out.write(fileUpload.getBytes());
            out.flush();
            out.close();

            Document document = new Document();
            document.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            document.setName(fileName);
            document.setSummary(summary);
            document.setLocation(filePath+fileName);
            document.setCreator(creator);
            document.setType(type);

            document.setCreatetime(new Date());
            documentMapper.insert(document);
            response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            response.setMessage("上传成功！");
            return response;
        } catch (Exception e) {
           // e.printStackTrace();
            response.setMessage("上传失败！" + e.getMessage());
            return response;
        }
    }
    @RequestMapping("/downloadFile")
    public String downLoad(HttpServletResponse response, @RequestParam String filelocation) throws UnsupportedEncodingException {
     //   String filename="createSQL.sql";
     //   String filePath = "D:/upload" ;
        File file = new File(  filelocation);
        String filename = filelocation.split("/")[filelocation.split("/").length -1];
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
           // response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
            response.setContentType("multipart/form-data;charset=UTF-8");
            response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(filename, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
              //  System.out.println("----------file download failed" + e.getMessage());
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         //   System.out.println("----------file download" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping("/documentSelect")
    public TNPYResponse documentSelect(int type,String summary,String creator,String startTime,String endTime){
       return  documentService.documentSelect(type, summary, creator, startTime, endTime);
    }
}
