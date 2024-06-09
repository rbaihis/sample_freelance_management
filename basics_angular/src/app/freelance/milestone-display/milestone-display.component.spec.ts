import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilestoneDisplayComponent } from './milestone-display.component';

describe('MilestoneDisplayComponent', () => {
  let component: MilestoneDisplayComponent;
  let fixture: ComponentFixture<MilestoneDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MilestoneDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MilestoneDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
