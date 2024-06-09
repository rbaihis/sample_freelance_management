package com.example.user.dto.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.FileDoc;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUpdateDto {
    private Long id;
    private Long gigId;
    private Long ownerId;

    private Float bid;
    private Float extraCost;
    private String walletId;
    private List<FileDoc> files;


    public static void EntityMapperToDto(Application app , AppUpdateDto appDto) {
        if (app == null || appDto == null)
            return;


        if (appDto.getExtraCost() != null)
            app.setExtraCost(appDto.getExtraCost());

        if (appDto.getBid() != null)
            app.setBid(appDto.getBid());

        if(appDto.getWalletId() !=null)
            app.setWalletId(appDto.getWalletId());

        if (appDto.getFiles()!= null )
            app.getFiles().addAll(appDto.getFiles());

    }
    




}
