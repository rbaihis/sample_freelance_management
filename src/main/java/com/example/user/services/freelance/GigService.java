package com.example.user.services.freelance;


import com.example.user.dto.freelance.GigUpdateDto;
import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Gig;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.interfaces.freelance.IGigService;
import com.example.user.repository.freelance.GigRepository;

import com.example.user.repository.freelance.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class GigService implements IGigService {
    private final GigRepository repo;
    private final MilestoneRepository repoMilestone;
    private final FileDocService fileDocService;
    private final MilestoneService serviceMilestone;

    @Autowired
    GigService(GigRepository repo, MilestoneRepository repoMilestone, FileDocService fileDocService, MilestoneService milestoneService){
        this.repo = repo;
        this.repoMilestone = repoMilestone;
        this.fileDocService = fileDocService;
        this.serviceMilestone = milestoneService;
    }

    //------------------------------------------------------
    @Override
    public void gigViewDataUpdateBeforeReturn(Gig gig){
        gig.setOwnerId( gig.getOwnerUser().getId());
        gig.setOwnerName( gig.getOwnerUser().getName());
        if(gig.getApplications()!=null)
            for (Application app : gig.getApplications()) {
                app.setGigId(app.getGig().getId());
                //change on integration
                app.setOwnerId(app.getOwnerUser().getId());
                app.setOwnerName(app.getOwnerUser().getName());
            }
        for (Milestone ms: gig.getMilestones())
            this.serviceMilestone.updateViewMilestoneFieldsBeforeReturn(ms);


    }




    /**
     *
     * @return list of all Gigs in DB , use it only for testing Purpose
     */
    @Override
    public List<Gig> getGigs () {

        Sort sortBySubmitDay  = Sort.by(Sort.Direction.DESC , "submitDate");
        // Define sorting direction
        List<Gig> gigs = repo.findAll(sortBySubmitDay);
        if (!gigs.isEmpty())
            for (Gig gig : gigs)
                this.gigViewDataUpdateBeforeReturn(gig);
        System.out.println( "pageReturned: "+  "getGigsCalled");
        return gigs;
    }

    /**
     *
     * @param pageNumber number Of Page We wish To Paginate
     * @param pageSize number of gigs to display in the current Page
     * @param sortOrder accepts "asc" or "ASC" as String else if null or whateverText ==> Sort.Direction.DESC
     * @return Page of_Gig_Entity
     */
    @Override
    public Page<Gig> getGigsPaginated (int pageNumber, int pageSize, String sortOrder , Float minPrice, Float maxPrice) {



        //only to avoid null passed by developer as null
        if(sortOrder == null  )
            sortOrder = "DESC";

        Sort.Direction direction =
                sortOrder.equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, "submitDate");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        //if search used  Search:
        if(maxPrice!=null || minPrice != null){
            float max=Float.MAX_VALUE;
            float min=0f;
            if(maxPrice!=null)
                max=maxPrice;
            if(minPrice!=null)
                min=minPrice;
            return repo.findByMaxPriceBetween(min,max,pageable);
        }


        Page<Gig> page = repo.findAll(pageable);
        if(page.hasContent())
            for (Gig g : page.getContent())
                this.gigViewDataUpdateBeforeReturn(g);

        System.out.println( "pageReturned: "+   page);

        return  page;
    }

    /**
     * fetch a gig from db by its id(primary_key_number)
     * @param id type.Long
     * @return type.Gig if gig exist else null
     */
    @Override
    public Gig getOneGig (Long id) {
        Gig gig = repo.findById(id).orElse(null);
        if(gig != null)
            this.gigViewDataUpdateBeforeReturn(gig);
        return gig;
    }



    /**
     * used for internal use logic
     * Saved only  if gig.getProjectStart().isAfter(LocalDate.now()
     * @param gig type.Gig
     * @return type.Gig with its id .
     */
    public Gig saveGig(Gig gig){
        if (gig.getProjectStart().isBefore(LocalDate.now()))
            return null;
        return repo.save(gig);
    }



    /**
     *
     * @param id type.Long
     * @param gigDto type.GigUpdateDto only files can Be added
     * @return Gig type.Gig afterUpdate .
     */
    @Override
    public Gig updateGig (Long id ,GigUpdateDto gigDto) throws IOException {
        Gig gigFetched = repo.findById(id).orElse(null);

        if(gigFetched == null  )
            return null;
        if (gigFetched.getProjectStart().isBefore(LocalDate.now()))
            return null;

        //add UniquePath to files if any is added
        if (gigDto.getFiles() != null){
            for (FileDoc file : gigDto.getFiles())
                this.fileDocService.addUniqueFileName(file);
        }


        //fields to update allowed in GigLogic UseCase inDTO are files Only orAnd WalletId
        GigUpdateDto.dtoToEntityMapperFileAndWalletId(gigFetched , gigDto );

        //files should have UniquePath added in a previous Step
        this.fileDocService.savePhysFiles(gigFetched.getFiles());

        return repo.save(gigFetched);
    }

    /**
     * pre delete method are defined in Listener for file deletion off all relationship with physical files
     * @param id type.Long hn
     * @return true only if gigFetched.isGigAssigned == false ==> delete allowed , else it can't be deleted
     */
    @Override
    public boolean deleteGig (Long id) {
        Gig gigFetched = repo.findById(id).orElse(null);
        /*
        1/add logic to verify gig-belongs to User Who create it
        */
        if (gigFetched == null) return false;


        boolean deletable =  !gigFetched.isGigAssigned() ;
        if( deletable ){
            // relies on Pre_listener to delete all gig.files of this entity and all others Cascade.Remove entities
            repo.delete(gigFetched);
            return true;
        }
        return false;
    }
}
