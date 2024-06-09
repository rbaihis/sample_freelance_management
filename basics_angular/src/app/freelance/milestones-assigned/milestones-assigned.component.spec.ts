import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilestonesAssignedComponent } from './milestones-assigned.component';

describe('MilestonesAssignedComponent', () => {
  let component: MilestonesAssignedComponent;
  let fixture: ComponentFixture<MilestonesAssignedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MilestonesAssignedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MilestonesAssignedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
