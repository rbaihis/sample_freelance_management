package com.example.user.dto.freelance;

import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Gig;
import lombok.*;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GigUpdateDto {
    private Long ownerId;

    private Long id;
    private String walletId;
    private List<FileDoc> files;

    public static void dtoToEntityMapperFileAndWalletId(Gig gig , GigUpdateDto gigDto){
        if (gig == null || gigDto == null)
            return;

        if(gigDto.getFiles() != null)
            gig.getFiles().addAll(gigDto.getFiles());

        gig.setWalletId(gigDto.getWalletId());
    }



}
