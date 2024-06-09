package com.example.user.interfaces.freelance;

import com.example.user.Entities.freelance.FileDoc;

import java.io.IOException;
import java.util.List;

public interface IFileDocService {



    /**
     * assign a unique fileName to the property of FileDoc entity
     * @param file FileDoc entityClass
     */
    public void addUniqueFileName( FileDoc file ) ;


    /**
     * paths of FileDoc should have uniquePath before passing it (toAvoid storing duplicate), run method  this.addUniquePath( FileDoc file )  first to get unique path
     * @param files List<FileDoc>
     * @throws IOException file Related problem write,read,Save
     */
    public void savePhysFiles(List<FileDoc> files) throws IOException ;

    /**
     * paths of FileDoc should be unique before passing it , run method  this.addUniquePath( FileDoc file )  first to get unique path
     * @param file FileDoc
     * @throws IOException file Related problem write,read,Save
     */
    public void saveOnePhyFile(FileDoc file) throws IOException ;



    /**
     * This will delete all staticFiles by path if exist
     * is will delete the elements from the array passed
     * @param files is a List<FileDoc> u wish to Delete its Static Files</>
     * @return return empty List<FileDoc> if all files re deleted, if not empty list returned==> error in path file can't be found
     * @throws IOException when IO_Operations fails related to file (initialised || read || locate || delete)
     */
    public List<FileDoc> deletePhysFiles(List<FileDoc> files) throws IOException ;
    //--------------
    /**
     *
     * @param file FileDoc object contains the path to file as attribute
     * @return true if a file exist and deleted , false if file does not exist ==> not deleted
     * @throws IOException when IO_Operations fails related to file (initialised || read || locate || delete)
     */
    public boolean deleteOnePhysFile(FileDoc file) throws IOException ;
}
