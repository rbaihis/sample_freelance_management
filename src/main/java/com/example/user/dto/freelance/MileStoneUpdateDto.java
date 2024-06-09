package com.example.user.dto.freelance;

import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Milestone;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MileStoneUpdateDto {
    //---------extra---------
    private Long freelancerId;
    private Long ownerId;

    private Long gigId;
    private Long chosenAppId;
    //-------relevant---------
    private Long id;
    private boolean updatable;
    private List<FileDoc> milestoneDeliverable ;

    //we can only add files on update of MileStone
    public static void dtoToEntityMapperMileStoneDeliverableFilesOnly(Milestone ms , MileStoneUpdateDto msDto){
        if (ms == null || msDto == null)
            return;

        if (msDto.getMilestoneDeliverable() != null )
            ms.getMilestoneDeliverable().addAll(msDto.getMilestoneDeliverable());
    }
}
