package com.tnpy.mes.service.documentService;

import com.tnpy.common.utils.web.TNPYResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:15
 */
public interface IDocumentService {
    public TNPYResponse getDocType();
    public TNPYResponse upload(MultipartFile fileUpload, int type, String summary, String creator);
    public String downLoad(HttpServletResponse response, @RequestParam String filelocation) throws UnsupportedEncodingException;
    public TNPYResponse documentSelect(int type,String summary,String creator,String startTime,String endTime);
}
