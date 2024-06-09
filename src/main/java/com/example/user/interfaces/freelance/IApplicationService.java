package com.example.user.interfaces.freelance;

import com.example.user.dto.freelance.AppUpdateDto;
import com.example.user.Entities.freelance.Application;

import java.io.IOException;
import java.util.List;

public interface IApplicationService {

    //--**************************---------------------*********************************
    void updateApplicationValuesBeforeReturn(Application app);

    public Application getApplication (Long id) ;
    public List<Application> getAllApplications () ;

    public Application addApplicationNoMajorRelationReference_savesInDb(Application application) throws IOException;
    public Application updateApplication (Long idApp, AppUpdateDto appDto) throws IOException;
    public void updateApplicationWonToggling_NoDbSaving(Application application ) ;

    public boolean deleteApplication (Application applicationFetched) throws IOException;

}
