package com.example.user.interfaces.freelance;


import com.example.user.dto.freelance.GigUpdateDto;
import com.example.user.Entities.freelance.Gig;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface IGigService {


        public Gig getOneGig (Long id);

    //------------------------------------------------------
    void gigViewDataUpdateBeforeReturn(Gig gig);

    public List<Gig> getGigs ();
        public Page<Gig> getGigsPaginated (int pageNumber, int pageSize, String sortOrder, Float minPrice, Float maxPrice) ;


        public Gig saveGig(Gig gig);

        public Gig updateGig (Long id , GigUpdateDto gigDto) throws IOException;

        public boolean deleteGig (Long id) ;

}
