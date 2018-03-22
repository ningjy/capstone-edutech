/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edutechservices;

//import edutechentities.common.GroupEntity;
import edutechentities.common.AttachmentEntity;
import edutechentities.common.ScheduleItemEntity;
import edutechentities.module.LessonEntity;
import edutechsessionbeans.CommonRESTMgrBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

/**
 *
 * @author nanda88
 */
@RequestScoped
@Path("lesson")
public class LessonREST {
    
    @EJB
    CommonRESTMgrBean cmb;
    
    @Context
    private ServletContext context;
    @Context 
    private HttpServletRequest request;
    @Context 
    private HttpServletResponse response;
    
    @GET 
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ScheduleItemEntity> getAllLessons() {
        return cmb.getAllLessons();
    }
    
    @GET 
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ScheduleItemEntity getOneLesson(@PathParam("id") String id){
        return cmb.getOneLesson(Long.valueOf(id));
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ScheduleItemEntity createLesson(LessonEntity lesson) {
        return cmb.createLesson(lesson);
    }
    
    @DELETE 
    @Path("{id}") 
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ScheduleItemEntity> deleteLesson(@PathParam("id") String id) {
        cmb.deleteLesson(id);
        return cmb.getAllLessons();
    }
    
    @PUT 
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ScheduleItemEntity editLesson(@PathParam("id") String id, ScheduleItemEntity replacement) {
        return cmb.editLesson(id, replacement);
    }
    
    @POST
    @Path("attachment/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AttachmentEntity> uploadLessonAttachment(@PathParam("id") String id) throws IOException, ServletException, FileUploadException, Exception {
        String title ="";
        String fileType="";
        String fileName="";
        String subFolder = "";
        
        String appPath = context.getRealPath("");
        String truncatedAppPath = appPath.replace("dist" + File.separator + "gfdeploy" + File.separator
                + "EduTech" + File.separator + "EduTechWebApp-war_war", "");
        
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(isMultipart){
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Configure a repository (to ensure a secure temp location is used)
            File repository = (File) context.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
            
            // Process the uploaded items
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                
                if (item.isFormField()) {
                    if(item.getFieldName().trim().equalsIgnoreCase("title")){
                        title=item.getString();
                    }else if(item.getFieldName().trim().equalsIgnoreCase("type")){
                        fileType=item.getString();
                        //0: documents, 1: images, 2: audios, 3: videos
                        switch(fileType){
                            case "0":
                                subFolder = "document";
                                break;
                            case "1":
                                subFolder = "images";
                                break;
                            case "2":
                                subFolder = "audios";
                                break;
                            case "3":
                                subFolder = "videos";
                                break;
                            default:
                                break;
                        }
                    }
                }else{
                    fileName = item.getName();
                    System.out.println("file name is "+fileName);
                    //START OF FILE UPLOAD
                    
                    //where to save file.
                    String fileDir = truncatedAppPath + "EduTechWebApp-war" + File.separator + "web" + File.separator
                            + "uploads" + File.separator + "edutech" + File.separator + "lesson" + File.separator + id
                            + File.separator + subFolder;
                    
                    //creates directory path if not present.
                    Files.createDirectories(Paths.get(fileDir));
                    // Process a file upload
                    File uploadedFile = new File(fileDir + File.separator + fileName);
                    item.write(uploadedFile);
                    InputStream uploadedStream = item.getInputStream();
                    uploadedStream.close();
                    // END OF FILE UPLOAD
                }
            }
            System.out.println("title is "+title);
            System.out.println("type is "+fileType);
            //create new attachment entity
            AttachmentEntity att = new AttachmentEntity();
            att.setTitle(title);
            att.setFileName(fileName);
            att.setType(Integer.valueOf(fileType));
            return cmb.uploadLessonAttachment(id,att);
        }
        return cmb.getAllAttachments();
    }
    
    @GET
    @Path("attachment/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces("application/zip")
    public Response downloadAllLessonAttachments(@PathParam("id") String id) throws IOException {
        //Declare the directory which the zip will be created at.
        String zipPath = context.getRealPath("").replace("dist" + File.separator + "gfdeploy" + File.separator
                + "EduTech" + File.separator + "EduTechWebApp-war_war", "")
                .concat("EduTechWebApp-war" + File.separator + "web" + File.separator
                            + "uploads" + File.separator + "edutech" + File.separator + "lesson" + File.separator + id);
        //for debugging
        System.out.println(zipPath);
        //create new file at that location.
        File zipFile = new File(zipPath+File.separator+"allResources.zip");
        ZipUtil.pack(new File(zipPath), zipFile);
        
        return Response
            .ok(FileUtils.readFileToByteArray(zipFile))
            .type("application/zip")
            .header("Content-Disposition", "attachment; filename=\"filename.zip\"")
            .build();
    }

}
