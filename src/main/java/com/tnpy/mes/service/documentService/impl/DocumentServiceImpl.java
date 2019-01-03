package com.tnpy.mes.service.documentService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.DirectoryEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.DocTypeMapper;
import com.tnpy.mes.mapper.mysql.DocumentMapper;
import com.tnpy.mes.model.mysql.DocType;
import com.tnpy.mes.model.mysql.Document;
import com.tnpy.mes.service.documentService.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:15
 */
@Service("documentService")
public class DocumentServiceImpl implements IDocumentService {
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private DocTypeMapper docTypeMapper;

    public TNPYResponse getDocType() {
        List<DocType> docTypeList = docTypeMapper.selectAllDict();
        TNPYResponse result = new TNPYResponse();

        result.setData(JSONObject.toJSON(docTypeList).toString());
        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
        return  result;
    }

    public TNPYResponse upload(MultipartFile fileUpload, int type, String summary, String creator){
        System.out.println(fileUpload.toString());
        TNPYResponse response = new TNPYResponse();
        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        //获取文件后缀名
        // String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        // fileName = UUID.randomUUID()+suffixName;
        //指定本地文件夹存储图片
        String filePath = DirectoryEnum.FileStoreLocation.UploadDocument.getName();
        try {
            File dir = new File(filePath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            //将图片保存到static文件夹里
            fileUpload.transferTo(new File(filePath+fileName));
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

    public TNPYResponse documentSelect(int type,String summary,String creator,String startTime,String endTime){
        TNPYResponse response = new TNPYResponse();
        StringBuffer filter = new StringBuffer();
        try {
            if(type > 0 )
            {
                filter.append(" and type =" + type + " ");
            }
            if(!StringUtils.isEmpty(summary))
            {
                filter.append(" and summary like '%" + summary + "%' ");
            }
            if(!StringUtils.isEmpty(creator))
            {
                filter.append(" and creator like '%" + creator + "%' ");
            }
            if(!StringUtils.isEmpty(startTime))
            {
                filter.append(" and createTime >= '" + startTime + "' ");
            }

            if(!StringUtils.isEmpty(endTime))
            {
                filter.append(" and createTime <= '" + endTime + "' ");
            }
            String str  = filter.toString();
            if(!StringUtils.isEmpty(str))
            {
                str = " where " + str.substring(4);
            }
            str += " " + "order by createTime desc";
            List<Document> documents = documentMapper.selectByFilter(str);
            response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            response.setData(JSONObject.toJSON(documents).toString());
            return response;
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
            response.setMessage("查询失败！" + e.getMessage());
            return response;
        }
    }
}
