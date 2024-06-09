import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilestoneUpdateComponent } from './milestone-update.component';

describe('MilestoneUpdateComponent', () => {
  let component: MilestoneUpdateComponent;
  let fixture: ComponentFixture<MilestoneUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MilestoneUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MilestoneUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
