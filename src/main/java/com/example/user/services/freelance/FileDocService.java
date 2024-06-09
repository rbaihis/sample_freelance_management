package com.example.user.services.freelance;


import com.example.user.Entities.freelance.FileDoc;
import com.example.user.interfaces.freelance.IFileDocService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class FileDocService implements IFileDocService {

    //get value from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Value("${base.url}")
    private String baseUrl;

    //to Assure Uniqueness and avoid RaceCondition in filePathCreation
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);



    //--------------Public-Methods---------------------------



    public void addUniqueFileName( FileDoc file ) {
        if(file.isSaved())
            return;
        if( file.getPath() !=null) // safety in case we do override the file name by a bug
            return;

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        file.setPath(timestamp + "_"
                + atomicCounter.incrementAndGet() +
                "_" + file.getName() + "." + file.getExtension());

        System.out.println("*******\n" +"generated-uniquePath :\n\t"+file.getPath() +"\n**********");

    }


    /**
     * paths of FileDoc should have uniquePath before passing it (toAvoid storing duplicate), run method  this.addUniquePath( FileDoc file )  first to get unique path
     * @param files List<FileDoc>
     * @throws IOException file Related problem write,read,Save
     */
    public void savePhysFiles(List<FileDoc> files) throws IOException {
        // Define the desired file permissions (read and write for owner, group, and others)
        for (FileDoc file : files) {
            saveOnePhyFile(file);
        }
    }

    /**
     * paths of filedoc should be unique before passing it , run method  this.adduniquepath( filedoc file )  first to get unique path
     * @param file filedoc
     * @throws IOException file related problem write,read,save
     */
    public void saveOnePhyFile(FileDoc file) throws IOException {
        if(file.isSaved())
            return;
        if(file.getContent() == null || file.getContent().isEmpty())
            return;
        if(file.getPath() == null || file.getPath().isEmpty())
            return;

        String base64Content = file.getContent();
        String uniqueFileName = file.getPath(); // path is unique

        // Construct the absolute file path by appending the unique file name to the upload directory
        Path filePath = Paths.get(uploadDir, uniqueFileName);

        //debug path
        System.out.println("**************\n"
                +"just Before Save  :\n\t"
                +filePath
                +"\n*********************");
        //end debug

        // Decode
        byte[] decodedContent = Base64.getDecoder().decode(base64Content);
        Files.write(filePath, decodedContent);

        // Set the file permissions
        file.setSaved(true);

    }



    /**
     * This will delete all staticFiles by path if exist
     * is will delete the elements from the array passed
     * @param files is a List<FileDoc> u wish to Delete its Static Files</>
     * @return return empty List<FileDoc> if all files re deleted, if not empty list returned==> error in path file can't be found
     * @throws IOException when IO_Operations fails related to file (initialised || read || locate || delete)
     */
    public List<FileDoc> deletePhysFiles(List<FileDoc> files) throws IOException{

        /*
            iterator is used to be able to delete file while deleting
         */
        Iterator<FileDoc> iterator = files.iterator();
        while (iterator.hasNext()) {
            FileDoc file = iterator.next();
            if (deleteOnePhysFile(file)) {
                iterator.remove();
            }
        }
        return files;
    }
    //--------------
    /**
     *
     * @param file FileDoc object contains the path to file as attribute
     * @return true if a file exist and deleted , false if file does not exist ==> not deleted
     * @throws IOException when IO_Operations fails related to file (initialised || read || locate || delete)
     */
    public boolean deleteOnePhysFile(FileDoc file) throws IOException {
        String fullPath= uploadDir+"\\"+file.getPath();
        //debug
        System.out.println("****** full path to delete :\n\t"+
                fullPath+"\n********");
        //
        Path path = Paths.get(fullPath);

        if ( !Files.exists(path) )
            return false;
        System.out.println("------\n"+
                "---File_Exist_will be deleted now :\n\t"+
                fullPath+"\n------");
        Files.delete(path);
        return true;
    }

}